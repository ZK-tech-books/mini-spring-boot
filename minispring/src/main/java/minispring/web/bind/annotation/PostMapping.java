package minispring.web.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * POST のリクエストを受けるメソッドに付ける目印。
 */
@RequestMapping(method = RequestMethod.POST)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostMapping {
    /**
     * 受けるパス。
     *
     * @return 受けるパス
     */
    String value();
}
