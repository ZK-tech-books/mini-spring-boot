package minispring.beans.factory.config;

/**
 * Bean をどのクラスから作るかを書いておくオブジェクト。
 * <p>
 * スキャンで見つけたクラスごとに作り、
 * {@link minispring.beans.factory.support.DefaultListableBeanFactory} に名前と組にして登録する。
 */
public class BeanDefinition {
    private final Class<?> beanClass;

    /**
     * BeanDefinition を作る。
     *
     * @param beanClass Bean を作るときに {@code new} するクラス
     */
    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Bean を作るときに {@code new} するクラスを返す。
     *
     * @return Bean のクラス
     */
    public Class<?> getBeanClass() {
        return this.beanClass;
    }
}
