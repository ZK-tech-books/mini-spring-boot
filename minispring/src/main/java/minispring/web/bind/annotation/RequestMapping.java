package minispring.web.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * メソッドがどの HTTP メソッドのリクエストを受けるかを表す目印。
 * <p>
 * {@link GetMapping} のようなアノテーションに付けて使い、パスはそのアノテーションの {@code value} に書く。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RequestMapping {
    /**
     * 受ける HTTP メソッド。
     *
     * @return 受ける HTTP メソッド
     */
    RequestMethod[] method();
}
