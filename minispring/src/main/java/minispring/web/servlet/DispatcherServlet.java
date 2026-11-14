package minispring.web.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import minispring.context.ApplicationContext;
import minispring.web.bind.annotation.GetMapping;
import minispring.web.bind.annotation.RestController;

/**
 * すべてのリクエストを受け、URL に対応する {@link RestController} のメソッドを呼ぶ Servlet。
 */
public class DispatcherServlet extends HttpServlet {
    private record Handler(Object bean, Method method) {
    }

    private final Map<String, Handler> handlers = new HashMap<>();

    /**
     * {@link RestController} の Bean から {@link GetMapping} の付いたメソッドを探し、URL とメソッドの表を作る。
     *
     * @param context Bean をすべて作り終えた {@link ApplicationContext}
     */
    public DispatcherServlet(ApplicationContext context) {
        for (Object bean : context.getBeansWithAnnotation(RestController.class).values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                GetMapping mapping = method.getAnnotation(GetMapping.class);
                if (mapping != null) {
                    this.handlers.put(mapping.value(), new Handler(bean, method));
                }
            }
        }
    }

    /**
     * URL に対応するメソッドを呼び、戻り値を文字にしてレスポンスに書く。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @throws IOException レスポンスを書けなかったとき
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Handler handler = this.handlers.get(request.getRequestURI());
        if (!"GET".equals(request.getMethod()) || handler == null) {
            respond(response, HttpServletResponse.SC_NOT_FOUND, "Not Found");
            return;
        }
        try {
            Object result = handler.method().invoke(handler.bean());
            respond(response, HttpServletResponse.SC_OK, String.valueOf(result));
        } catch (ReflectiveOperationException e) {
            respond(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    private void respond(HttpServletResponse response, int status, String body) throws IOException {
        response.setStatus(status);
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().write(body);
    }
}
