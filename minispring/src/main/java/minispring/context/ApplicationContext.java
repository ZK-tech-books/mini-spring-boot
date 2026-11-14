package minispring.context;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import minispring.beans.factory.annotation.Autowired;
import minispring.stereotype.Component;

public class ApplicationContext {
    private final List<Object> beans = new ArrayList<>();

    public ApplicationContext(String basePackage) {
        try {
            for (Class<?> type : findComponents(basePackage)) {
                Constructor<?> constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
                beans.add(constructor.newInstance());
            }
            for (Object bean : beans) {
                injectFields(bean);
            }
        } catch (ReflectiveOperationException | IOException | URISyntaxException e) {
            throw new IllegalStateException("Failed to create beans in package: " + basePackage, e);
        }
    }

    public <T> T getBean(Class<T> type) {
        List<Object> matches = beans.stream().filter(type::isInstance).toList();
        if (matches.size() != 1) {
            throw new IllegalArgumentException(
                    "Expected 1 bean of " + type.getName() + " but found " + matches.size());
        }
        return type.cast(matches.getFirst());
    }

    private List<Class<?>> findComponents(String basePackage)
            throws IOException, URISyntaxException, ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = basePackage.replace('.', '/');
        URL url = loader.getResource(packagePath);
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

        List<Class<?>> components = new ArrayList<>();
        for (Path classFile : classFiles) {
            String relativePath = root.relativize(classFile).toString();
            String simplePath = relativePath.substring(0, relativePath.length() - ".class".length());
            String className = basePackage + "." + simplePath.replace('/', '.');
            Class<?> type = Class.forName(className, false, loader);
            if (type.isAnnotationPresent(Component.class)) {
                components.add(type);
            }
        }
        return components;
    }

    private void injectFields(Object bean) throws IllegalAccessException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                field.set(bean, getBean(field.getType()));
            }
        }
    }
}
