package minispring.web.servlet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import minispring.context.ApplicationContext;
import minispring.web.bind.annotation.GetMapping;
import minispring.web.bind.annotation.RestController;

public class DispatcherServlet implements HttpHandler {
    private record Handler(Object bean, Method method) {
    }

    private final Map<String, Handler> handlers = new HashMap<>();

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

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Handler handler = this.handlers.get(exchange.getRequestURI().getPath());
        if (!"GET".equals(exchange.getRequestMethod()) || handler == null) {
            respond(exchange, 404, "Not Found");
            return;
        }
        try {
            Object result = handler.method().invoke(handler.bean());
            respond(exchange, 200, String.valueOf(result));
        } catch (ReflectiveOperationException e) {
            respond(exchange, 500, "Internal Server Error");
        }
    }

    private void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
