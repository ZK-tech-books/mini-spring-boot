package minispring.beans.factory.annotation;

import java.lang.reflect.Field;
import minispring.beans.factory.BeanFactoryAware;
import minispring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import minispring.beans.factory.support.DefaultListableBeanFactory;

/**
 * {@link Autowired} の付いたフィールドに Bean を注入する {@link minispring.beans.factory.config.BeanPostProcessor}。
 * <p>
 * 注入する Bean は、{@link #setBeanFactory} で受け取った {@link DefaultListableBeanFactory} から取り出す。
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    /**
     * 注入する Bean を取り出す {@link DefaultListableBeanFactory} を受け取る。
     *
     * @param beanFactory この Bean を作った {@link DefaultListableBeanFactory}
     */
    @Override
    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * {@link Autowired} の付いたフィールドに、フィールドの型の Bean を入れる。
     * <p>
     * 入れる Bean がまだ作られていなければ、{@link DefaultListableBeanFactory#getBean(Class)} の中で作られる。
     *
     * @param bean {@code new} したばかりの Bean
     */
    @Override
    public void postProcessProperties(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean, this.beanFactory.getBean(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Failed to inject field: " + field, e);
                }
            }
        }
    }
}
