package minispring.beans.factory.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import minispring.beans.factory.BeanFactoryAware;
import minispring.beans.factory.config.BeanDefinition;
import minispring.beans.factory.config.BeanPostProcessor;
import minispring.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * BeanDefinition をもとに Bean を作り、作った Bean を名前で覚えておくクラス。
 * <p>
 * Bean は {@link #getBean(String)} で初めて求められたときに作る。
 * 作るときは、{@code new} し、BeanPostProcessor にフィールドへ注入させ、
 * {@link BeanFactoryAware} を実装していれば自分自身を渡す。
 */
public class DefaultListableBeanFactory {
    private final Map<String, BeanDefinition> beanDefinitionMap = new LinkedHashMap<>();
    private final Map<String, Object> singletonObjects = new HashMap<>();
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /**
     * BeanDefinition を名前と組にして登録する。
     *
     * @param beanName Bean の名前
     * @param beanDefinition Bean の設計図
     */
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    /**
     * Bean を作るたびに呼ぶ BeanPostProcessor を加える。
     *
     * @param beanPostProcessor 加える BeanPostProcessor
     */
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * 指定した型の Bean になる BeanDefinition の名前を、登録した順に返す。
     *
     * @param type 探す型
     * @return 見つかった Bean の名前
     */
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> names = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionMap.entrySet()) {
            if (type.isAssignableFrom(entry.getValue().getBeanClass())) {
                names.add(entry.getKey());
            }
        }
        return names.toArray(String[]::new);
    }

    /**
     * 指定したアノテーションが、直接かほかのアノテーションを通してクラスに付いた Bean を、名前と組にして返す。
     *
     * @param annotationType 探すアノテーション
     * @return Bean の名前と Bean の組
     */
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        Map<String, Object> beans = new LinkedHashMap<>();
        for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionMap.entrySet()) {
            if (isAnnotated(entry.getValue().getBeanClass(), annotationType, new HashSet<>())) {
                beans.put(entry.getKey(), getBean(entry.getKey()));
            }
        }
        return beans;
    }

    private boolean isAnnotated(Class<?> type, Class<? extends Annotation> annotationType, Set<Class<?>> visited) {
        if (type.isAnnotationPresent(annotationType)) {
            return true;
        }
        for (Annotation annotation : type.getAnnotations()) {
            Class<? extends Annotation> metaType = annotation.annotationType();
            if (visited.add(metaType) && isAnnotated(metaType, annotationType, visited)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定した型の Bean を返す。
     *
     * @param requiredType 求める Bean の型
     * @param <T> 求める Bean の型
     * @return 見つかった Bean
     * @throws IllegalStateException 指定した型の Bean が1つではないとき
     */
    public <T> T getBean(Class<T> requiredType) {
        String[] names = getBeanNamesForType(requiredType);
        if (names.length != 1) {
            throw new IllegalStateException(
                    "Expected 1 bean of " + requiredType.getName() + " but found " + names.length);
        }
        return requiredType.cast(getBean(names[0]));
    }

    /**
     * 指定した名前の Bean を返す。まだ作っていなければ、ここで作って覚えておく。
     *
     * @param beanName Bean の名前
     * @return Bean
     */
    public Object getBean(String beanName) {
        Object bean = this.singletonObjects.get(beanName);
        if (bean == null) {
            bean = createBean(beanName, this.beanDefinitionMap.get(beanName));
            this.singletonObjects.put(beanName, bean);
        }
        return bean;
    }

    /**
     * 登録したすべての BeanDefinition から、登録した順に Bean を作る。
     */
    public void preInstantiateSingletons() {
        for (String beanName : this.beanDefinitionMap.keySet()) {
            getBean(beanName);
        }
    }

    /**
     * BeanDefinition から Bean を作る。
     *
     * @param beanName Bean の名前
     * @param beanDefinition Bean の設計図
     * @return 作った Bean
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = createBeanInstance(beanName, beanDefinition);
        populateBean(bean);
        initializeBean(bean);
        return bean;
    }

    /**
     * BeanDefinition のクラスを、引数のないコンストラクタで {@code new} する。
     *
     * @param beanName Bean の名前
     * @param beanDefinition Bean の設計図
     * @return {@code new} したばかりの Bean
     */
    private Object createBeanInstance(String beanName, BeanDefinition beanDefinition) {
        try {
            Constructor<?> constructor = beanDefinition.getBeanClass().getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate bean: " + beanName, e);
        }
    }

    /**
     * {@link InstantiationAwareBeanPostProcessor} に、Bean のフィールドへ値を入れさせる。
     *
     * @param bean {@code new} したばかりの Bean
     */
    private void populateBean(Object bean) {
        for (BeanPostProcessor beanPostProcessor : this.beanPostProcessors) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor processor) {
                processor.postProcessProperties(bean);
            }
        }
    }

    /**
     * フィールドに値を入れ終えた Bean を仕上げる。
     *
     * @param bean フィールドに値を入れ終えた Bean
     */
    private void initializeBean(Object bean) {
        invokeAwareMethods(bean);
    }

    /**
     * Bean が {@link BeanFactoryAware} を実装していれば、自分自身を渡す。
     *
     * @param bean フィールドに値を入れ終えた Bean
     */
    private void invokeAwareMethods(Object bean) {
        if (bean instanceof BeanFactoryAware beanFactoryAware) {
            beanFactoryAware.setBeanFactory(this);
        }
    }
}
