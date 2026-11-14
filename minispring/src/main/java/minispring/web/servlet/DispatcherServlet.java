package minispring.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import minispring.context.ApplicationContext;
import minispring.web.HttpRequestMethodNotSupportedException;
import minispring.web.method.HandlerMethod;
import minispring.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import minispring.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * すべてのリクエストを受け、{@link HandlerMapping} が探したメソッドを {@link HandlerAdapter} に呼ばせる Servlet。
 */
public class DispatcherServlet extends HttpServlet {
    private final ApplicationContext applicationContext;
    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;

    /**
     * Servlet を作る。
     *
     * @param applicationContext Bean をすべて作り終えた {@link ApplicationContext}
     */
    public DispatcherServlet(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Tomcat が最初のリクエストの前に呼ぶ初期化で、{@link HandlerMapping} と {@link HandlerAdapter} を作る。
     */
    @Override
    public void init() {
        initStrategies(this.applicationContext);
    }

    private void initStrategies(ApplicationContext context) {
        this.handlerMapping = new RequestMappingHandlerMapping(context);
        this.handlerAdapter = new RequestMappingHandlerAdapter();
    }

    /**
     * リクエストを {@link #doDispatch} に渡す。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @throws ServletException リクエストを受けるメソッドが例外を投げたとき
     * @throws IOException レスポンスを書けなかったとき
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doDispatch(request, response);
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HandlerMethod handler = this.handlerMapping.getHandler(request);
            if (handler == null) {
                noHandlerFound(response);
                return;
            }
            this.handlerAdapter.handle(request, response, handler);
        } catch (HttpRequestMethodNotSupportedException e) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void noHandlerFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
