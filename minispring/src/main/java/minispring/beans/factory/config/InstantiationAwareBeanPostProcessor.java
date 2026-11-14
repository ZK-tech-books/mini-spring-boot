package minispring.beans.factory.config;

/**
 * Bean を {@code new} した直後に、フィールドへ値を入れる {@link BeanPostProcessor}。
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
    /**
     * {@code new} したばかりの Bean のフィールドに値を入れる。
     *
     * @param bean {@code new} したばかりの Bean
     */
    void postProcessProperties(Object bean);
}
