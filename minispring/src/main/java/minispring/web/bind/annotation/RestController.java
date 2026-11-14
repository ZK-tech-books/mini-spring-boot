package minispring.web.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import minispring.stereotype.Controller;

/**
 * メソッドの戻り値をレスポンスの本文に書く {@link Controller} に付ける目印。
 */
@Controller
@ResponseBody
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestController {
}
