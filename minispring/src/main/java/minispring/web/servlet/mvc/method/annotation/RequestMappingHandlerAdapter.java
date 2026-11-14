package minispring.web.servlet.mvc.method.annotation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import minispring.http.converter.StringHttpMessageConverter;
import minispring.http.converter.json.JacksonJsonHttpMessageConverter;
import minispring.web.method.HandlerMethod;
import minispring.web.servlet.HandlerAdapter;

/**
 * リクエストを受けるメソッドを呼び、戻り値を {@link RequestResponseBodyMethodProcessor} に書かせるクラス。
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    private final RequestResponseBodyMethodProcessor returnValueHandler = new RequestResponseBodyMethodProcessor(
            List.of(new StringHttpMessageConverter(), new JacksonJsonHttpMessageConverter()));

    /**
     * メソッドを呼び、戻り値をレスポンスに書く。
     * <p>
     * 引数を持たないメソッドだけを呼ぶので、リクエストは使わない。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @param handler 呼ぶメソッド
     * @throws ServletException メソッドが例外を投げたとき
     * @throws IOException レスポンスを書けなかったとき
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws ServletException, IOException {
        if (!this.returnValueHandler.supportsReturnType(handler)) {
            throw new IllegalStateException("No return value handler for " + handler.getMethod());
        }
        this.returnValueHandler.handleReturnValue(invoke(handler), response);
    }

    private Object invoke(HandlerMethod handler) throws ServletException {
        try {
            return handler.getMethod().invoke(handler.getBean());
        } catch (InvocationTargetException e) {
            throw new ServletException(e.getCause());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to invoke " + handler.getMethod(), e);
        }
    }
}
