package minispring.aop.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import minispring.aop.PointcutAdvisor;
import minispring.aopalliance.intercept.MethodInterceptor;

/**
 * プロキシのメソッドが呼ばれたときに Byte Buddy から呼ばれ、差し込む処理を集めて本物のメソッドまで進めるクラス。
 */
public class DynamicAdvisedInterceptor implements InvocationHandler {
    private final Object target;
    private final List<PointcutAdvisor> advisors;

    /**
     * プロキシの呼び出しを受ける準備をする。
     *
     * @param target 本物の Bean
     * @param advisors この Bean に差し込む処理を持つ Advisor
     */
    public DynamicAdvisedInterceptor(Object target, List<PointcutAdvisor> advisors) {
        this.target = target;
        this.advisors = advisors;
    }

    /**
     * 呼ばれたメソッドに合う差し込む処理を集め、{@link ReflectiveMethodInvocation} で順に呼ぶ。
     *
     * @param proxy プロキシ
     * @param method 呼ばれたメソッド
     * @param args メソッドの引数
     * @return 本物のメソッドの戻り値
     * @throws Throwable 差し込む処理か本物のメソッドが投げた例外
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<MethodInterceptor> chain = new ArrayList<>();
        for (PointcutAdvisor advisor : this.advisors) {
            if (advisor.getPointcut().matches(method)) {
                chain.add(advisor.getAdvice());
            }
        }
        return new ReflectiveMethodInvocation(this.target, method, args, chain).proceed();
    }
}
