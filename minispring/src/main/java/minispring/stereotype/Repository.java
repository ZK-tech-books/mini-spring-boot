package minispring.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * データを読み書きする repository のクラスに付ける目印。
 * <p>
 * {@link Component} が付いているので、このアノテーションを付けたクラスも Bean になる。
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repository {
}
