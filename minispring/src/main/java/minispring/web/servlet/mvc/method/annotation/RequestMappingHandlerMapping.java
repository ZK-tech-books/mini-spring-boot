package minispring.web.servlet.mvc.method.annotation;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import minispring.context.ApplicationContext;
import minispring.stereotype.Controller;
import minispring.web.HttpRequestMethodNotSupportedException;
import minispring.web.bind.annotation.RequestMapping;
import minispring.web.method.HandlerMethod;
import minispring.web.servlet.HandlerMapping;
import minispring.web.servlet.mvc.method.RequestMappingInfo;

/**
 * {@link Controller} の Bean から {@link RequestMapping} の付いたメソッドを集め、リクエストの条件とメソッドの表を作るクラス。
 */
public class RequestMappingHandlerMapping implements HandlerMapping {
    private final Map<RequestMappingInfo, HandlerMethod> registry = new LinkedHashMap<>();

    /**
     * {@link Controller} の Bean から表を作る。
     *
     * @param context Bean をすべて作り終えた {@link ApplicationContext}
     */
    public RequestMappingHandlerMapping(ApplicationContext context) {
        for (Object bean : context.getBeansWithAnnotation(Controller.class).values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                RequestMappingInfo info = getMappingForMethod(method);
                if (info != null) {
                    this.registry.put(info, new HandlerMethod(bean, method));
                }
            }
        }
    }

    /**
     * パスと HTTP メソッドが合うメソッドを返す。
     *
     * @param request リクエスト
     * @return リクエストを受けるメソッド。パスに対応するメソッドがなければ {@code null}
     * @throws HttpRequestMethodNotSupportedException パスは合うが HTTP メソッドが合わないとき
     */
    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
        boolean pathMatched = false;
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : this.registry.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            if (!info.path().equals(request.getRequestURI())) {
                continue;
            }
            if (info.matchesMethod(request.getMethod())) {
                return entry.getValue();
            }
            pathMatched = true;
        }
        if (pathMatched) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod());
        }
        return null;
    }

    /**
     * {@link RequestMapping} を付けたアノテーションをメソッドから探し、リクエストの条件を作る。
     * <p>
     * パスはそのアノテーションの {@code value} から、HTTP メソッドは {@link RequestMapping} の {@code method} から読む。
     *
     * @param method 調べるメソッド
     * @return リクエストの条件。アノテーションがなければ {@code null}
     */
    private RequestMappingInfo getMappingForMethod(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            RequestMapping requestMapping = annotation.annotationType().getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                return new RequestMappingInfo(readPath(annotation), Set.of(requestMapping.method()));
            }
        }
        return null;
    }

    private String readPath(Annotation annotation) {
        try {
            return (String) annotation.annotationType().getMethod("value").invoke(annotation);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to read path from " + annotation, e);
        }
    }
}
