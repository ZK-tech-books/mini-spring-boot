package minispring.context;

import java.lang.annotation.Annotation;
import java.util.Map;
import minispring.beans.factory.config.BeanDefinition;
import minispring.beans.factory.config.BeanPostProcessor;
import minispring.beans.factory.support.DefaultListableBeanFactory;
import minispring.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * パッケージをスキャンして BeanDefinition を登録し、Bean をすべて作るクラス。
 * <p>
 * Bean を作って覚えておく処理は、{@link DefaultListableBeanFactory} に任せる。
 */
public class ApplicationContext {
    private final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
    private final ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this);

    /**
     * パッケージをスキャンし、見つけたクラスの Bean をすべて作る。
     *
     * @param basePackages スキャンするパッケージ
     */
    public ApplicationContext(String... basePackages) {
        this.scanner.scan(basePackages);
        refresh();
    }

    /**
     * BeanDefinition を {@link DefaultListableBeanFactory} に登録する。
     *
     * @param beanName Bean の名前
     * @param beanDefinition Bean の設計図
     */
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 指定したアノテーションがクラスに付いた Bean を、名前と組にして返す。
     *
     * @param annotationType 探すアノテーション
     * @return Bean の名前と Bean の組
     */
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return this.beanFactory.getBeansWithAnnotation(annotationType);
    }

    /**
     * BeanPostProcessor を先に作ってから、残りの Bean をすべて作る。
     */
    private void refresh() {
        registerBeanPostProcessors();
        this.beanFactory.preInstantiateSingletons();
    }

    /**
     * BeanPostProcessor を実装したクラスの Bean を作り、{@link DefaultListableBeanFactory} に加える。
     */
    private void registerBeanPostProcessors() {
        for (String beanName : this.beanFactory.getBeanNamesForType(BeanPostProcessor.class)) {
            this.beanFactory.addBeanPostProcessor((BeanPostProcessor) this.beanFactory.getBean(beanName));
        }
    }
}
