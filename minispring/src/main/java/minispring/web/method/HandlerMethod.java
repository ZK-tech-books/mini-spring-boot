package minispring.web.method;

import java.lang.reflect.Method;

/**
 * リクエストを受けるメソッドと、そのメソッドを持つ Bean の組。
 */
public class HandlerMethod {
    private final Object bean;
    private final Method method;

    /**
     * 組を作る。
     *
     * @param bean メソッドを持つ Bean
     * @param method リクエストを受けるメソッド
     */
    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    /**
     * メソッドを持つ Bean を返す。
     *
     * @return メソッドを持つ Bean
     */
    public Object getBean() {
        return this.bean;
    }

    /**
     * リクエストを受けるメソッドを返す。
     *
     * @return リクエストを受けるメソッド
     */
    public Method getMethod() {
        return this.method;
    }
}
