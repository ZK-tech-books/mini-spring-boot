package minispring.aop.framework.autoproxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import minispring.aop.Advisor;
import minispring.aop.Pointcut;
import minispring.aop.PointcutAdvisor;
import minispring.aop.framework.DynamicAdvisedInterceptor;
import minispring.aopalliance.intercept.MethodInterceptor;
import minispring.beans.factory.BeanFactoryAware;
import minispring.beans.factory.config.BeanPostProcessor;
import minispring.beans.factory.support.DefaultListableBeanFactory;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Bean を作り終えるたびに、差し込む処理がある Bean をプロキシに包む {@link BeanPostProcessor}。
 * <p>
 * どの処理を差し込むかは {@link Advisor} の Bean が決めるので、このクラスはトランザクションのことを知らない。
 */
public class InfrastructureAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    /**
     * {@link Advisor} の Bean を取り出す {@link DefaultListableBeanFactory} を受け取る。
     *
     * @param beanFactory この Bean を作った {@link DefaultListableBeanFactory}
     */
    @Override
    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 差し込む処理がある Bean なら、プロキシに包んで返す。
     *
     * @param bean 作り終えた Bean
     * @return プロキシか、元の Bean
     */
    @Override
    public Object postProcessAfterInitialization(Object bean) {
        return wrapIfNecessary(bean);
    }

    private Object wrapIfNecessary(Object bean) {
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }
        List<PointcutAdvisor> advisors = findEligibleAdvisors(bean.getClass());
        if (advisors.isEmpty()) {
            return bean;
        }
        return createProxy(bean, advisors);
    }

    /**
     * AOP を組み立てる部品のクラスかを返す。部品自身はプロキシに包まない。
     *
     * @param beanClass 調べるクラス
     * @return 部品のクラスなら {@code true}
     */
    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advisor.class.isAssignableFrom(beanClass)
                || Pointcut.class.isAssignableFrom(beanClass)
                || MethodInterceptor.class.isAssignableFrom(beanClass);
    }

    /**
     * {@link Advisor} の Bean のうち、クラスのどれかのメソッドに処理を差し込むものを返す。
     *
     * @param beanClass 調べるクラス
     * @return 処理を差し込む {@link Advisor}
     */
    private List<PointcutAdvisor> findEligibleAdvisors(Class<?> beanClass) {
        List<PointcutAdvisor> eligibleAdvisors = new ArrayList<>();
        for (String advisorName : this.beanFactory.getBeanNamesForType(Advisor.class)) {
            PointcutAdvisor advisor = (PointcutAdvisor) this.beanFactory.getBean(advisorName);
            for (Method method : beanClass.getDeclaredMethods()) {
                if (advisor.getPointcut().matches(method)) {
                    eligibleAdvisors.add(advisor);
                    break;
                }
            }
        }
        return eligibleAdvisors;
    }

    /**
     * Byte Buddy で Bean のクラスのサブクラスを作り、そのインスタンスをプロキシとして返す。
     * <p>
     * サブクラスは、Bean のクラスに書かれたメソッドをすべて上書きし、呼ばれたら {@link DynamicAdvisedInterceptor} に渡す。
     *
     * @param bean 本物の Bean
     * @param advisors 差し込む処理を持つ {@link Advisor}
     * @return プロキシ
     */
    private Object createProxy(Object bean, List<PointcutAdvisor> advisors) {
        Class<?> beanClass = bean.getClass();
        try {
            return new ByteBuddy()
                    .subclass(beanClass)
                    .method(ElementMatchers.isDeclaredBy(beanClass))
                    .intercept(InvocationHandlerAdapter.of(new DynamicAdvisedInterceptor(bean, advisors)))
                    .make()
                    .load(beanClass.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create proxy for " + beanClass, e);
        }
    }
}
