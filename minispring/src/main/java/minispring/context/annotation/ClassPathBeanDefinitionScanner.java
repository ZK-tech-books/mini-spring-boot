package minispring.context.annotation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import minispring.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import minispring.beans.factory.config.BeanDefinition;
import minispring.context.ApplicationContext;
import minispring.stereotype.Component;

/**
 * パッケージの下から {@link Component} の付いたクラスを探し、BeanDefinition を登録するクラス。
 */
public class ClassPathBeanDefinitionScanner {
    private static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "minispring.context.annotation.internalAutowiredAnnotationProcessor";

    private final ApplicationContext registry;

    /**
     * スキャナを作る。
     *
     * @param registry 見つけた BeanDefinition の登録先
     */
    public ClassPathBeanDefinitionScanner(ApplicationContext registry) {
        this.registry = registry;
    }

    /**
     * パッケージの下のクラスから BeanDefinition を作って登録する。
     * <p>
     * 最後に、{@link AutowiredAnnotationBeanPostProcessor} の BeanDefinition も登録する。
     *
     * @param basePackages 探すパッケージ
     */
    public void scan(String... basePackages) {
        for (String basePackage : basePackages) {
            for (BeanDefinition candidate : findCandidateComponents(basePackage)) {
                this.registry.registerBeanDefinition(generateBeanName(candidate), candidate);
            }
        }
        this.registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,
                new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    /**
     * パッケージの下の {@code .class} ファイルを読み、Bean にするクラスの BeanDefinition を作る。
     *
     * @param basePackage 探すパッケージ
     * @return 見つけたクラスの BeanDefinition
     */
    private Set<BeanDefinition> findCandidateComponents(String basePackage) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(basePackage.replace('.', '/'));
            if (url == null) {
                throw new IllegalArgumentException("Package not found: " + basePackage);
            }
            if (!"file".equals(url.getProtocol())) {
                throw new IllegalArgumentException("Only class directories are supported: " + url);
            }

            Path root = Path.of(url.toURI());
            List<Path> classFiles;
            try (Stream<Path> files = Files.walk(root)) {
                classFiles = files.filter(file -> file.toString().endsWith(".class")).toList();
            }

            Set<BeanDefinition> candidates = new LinkedHashSet<>();
            for (Path classFile : classFiles) {
                String relativePath = root.relativize(classFile).toString();
                String simplePath = relativePath.substring(0, relativePath.length() - ".class".length());
                Class<?> type = Class.forName(basePackage + "." + simplePath.replace('/', '.'), false, loader);
                if (isCandidateComponent(type)) {
                    candidates.add(new BeanDefinition(type));
                }
            }
            return candidates;
        } catch (IOException | URISyntaxException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to scan package: " + basePackage, e);
        }
    }

    /**
     * クラスに {@link Component} か、{@link Component} を付けたアノテーションが付いているかを返す。
     *
     * @param type 調べるクラス
     * @return Bean にするクラスなら {@code true}
     */
    private boolean isCandidateComponent(Class<?> type) {
        if (type.isAnnotation()) {
            return false;
        }
        if (type.isAnnotationPresent(Component.class)) {
            return true;
        }
        for (Annotation annotation : type.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Component.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * クラス名の頭を小文字にして、Bean の名前を作る。
     *
     * @param definition 名前を付ける BeanDefinition
     * @return Bean の名前
     */
    private String generateBeanName(BeanDefinition definition) {
        String simpleName = definition.getBeanClass().getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
