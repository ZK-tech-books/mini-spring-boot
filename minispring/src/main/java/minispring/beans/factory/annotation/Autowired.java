package minispring.beans.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bean を注入するフィールドに付ける目印。
 * <p>
 * {@link AutowiredAnnotationBeanPostProcessor} が、フィールドの型の Bean を探して入れる。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {
}
