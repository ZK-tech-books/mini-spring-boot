package minispring.web.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GET のリクエストを受けるメソッドに付ける目印。
 */
@RequestMapping(method = RequestMethod.GET)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetMapping {
    /**
     * 受けるパス。
     *
     * @return 受けるパス
     */
    String value();
}
