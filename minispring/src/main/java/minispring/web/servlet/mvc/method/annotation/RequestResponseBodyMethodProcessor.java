package minispring.web.servlet.mvc.method.annotation;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import minispring.http.converter.HttpMessageConverter;
import minispring.web.bind.annotation.ResponseBody;
import minispring.web.method.HandlerMethod;

/**
 * {@link ResponseBody} の付いたクラスのメソッドの戻り値を、書ける {@link HttpMessageConverter} でレスポンスに書くクラス。
 */
public class RequestResponseBodyMethodProcessor {
    private final List<HttpMessageConverter> messageConverters;

    /**
     * 戻り値を書く準備をする。
     *
     * @param messageConverters 戻り値を書ける型を探す順に並べた {@link HttpMessageConverter}
     */
    public RequestResponseBodyMethodProcessor(List<HttpMessageConverter> messageConverters) {
        this.messageConverters = messageConverters;
    }

    /**
     * メソッドを持つクラスに、直接かほかのアノテーションを通して {@link ResponseBody} が付いているかを返す。
     *
     * @param handler 呼ぶメソッド
     * @return 付いていれば {@code true}
     */
    public boolean supportsReturnType(HandlerMethod handler) {
        Class<?> beanType = handler.getBean().getClass();
        return beanType.isAnnotationPresent(ResponseBody.class)
                || Arrays.stream(beanType.getAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(ResponseBody.class));
    }

    /**
     * 戻り値の型を書ける最初の {@link HttpMessageConverter} で、戻り値をレスポンスに書く。
     *
     * @param returnValue メソッドの戻り値
     * @param response レスポンス
     * @throws IOException レスポンスを書けなかったとき
     */
    public void handleReturnValue(Object returnValue, HttpServletResponse response) throws IOException {
        for (HttpMessageConverter converter : this.messageConverters) {
            if (converter.canWrite(returnValue.getClass())) {
                converter.write(returnValue, response);
                return;
            }
        }
        throw new IllegalStateException("No converter for " + returnValue.getClass());
    }
}
