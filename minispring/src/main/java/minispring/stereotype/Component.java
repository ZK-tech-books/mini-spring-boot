package minispring.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bean にするクラスに付ける目印。
 * <p>
 * {@link minispring.context.annotation.ClassPathBeanDefinitionScanner} は、この目印が付いたクラスと、
 * この目印を付けたアノテーションが付いたクラスを Bean にする。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}
