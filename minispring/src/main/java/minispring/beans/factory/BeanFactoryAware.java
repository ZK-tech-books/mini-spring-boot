package minispring.beans.factory;

import minispring.beans.factory.support.DefaultListableBeanFactory;

/**
 * {@link DefaultListableBeanFactory} を受け取りたい Bean が実装する interface。
 * <p>
 * これを実装した Bean を作ると、{@link DefaultListableBeanFactory} が自分自身を渡す。
 */
public interface BeanFactoryAware {
    /**
     * Bean を作った {@link DefaultListableBeanFactory} を受け取る。
     *
     * @param beanFactory この Bean を作った {@link DefaultListableBeanFactory}
     */
    void setBeanFactory(DefaultListableBeanFactory beanFactory);
}
