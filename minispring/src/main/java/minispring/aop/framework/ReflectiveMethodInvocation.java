package minispring.aop.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import minispring.aopalliance.intercept.MethodInterceptor;
import minispring.aopalliance.intercept.MethodInvocation;

/**
 * 差し込む処理を順に呼び、最後に本物のメソッドをリフレクションで呼ぶ {@link MethodInvocation}。
 */
public class ReflectiveMethodInvocation implements MethodInvocation {
    private final Object target;
    private final Method method;
    private final Object[] arguments;
    private final List<MethodInterceptor> interceptors;
    private int currentInterceptorIndex = 0;

    /**
     * メソッドの呼び出しを作る。
     *
     * @param target 本物の Bean
     * @param method 呼ばれたメソッド
     * @param arguments メソッドの引数
     * @param interceptors 順に呼ぶ差し込む処理
     */
    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments,
            List<MethodInterceptor> interceptors) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.interceptors = interceptors;
    }

    /**
     * 呼ばれたメソッドを返す。
     *
     * @return 呼ばれたメソッド
     */
    @Override
    public Method getMethod() {
        return this.method;
    }

    /**
     * 次の差し込む処理を呼ぶ。差し込む処理が残っていなければ、本物のメソッドを呼ぶ。
     *
     * @return 本物のメソッドの戻り値
     * @throws Throwable 差し込む処理か本物のメソッドが投げた例外
     */
    @Override
    public Object proceed() throws Throwable {
        if (this.currentInterceptorIndex == this.interceptors.size()) {
            return invokeJoinpoint();
        }
        MethodInterceptor interceptor = this.interceptors.get(this.currentInterceptorIndex++);
        return interceptor.invoke(this);
    }

    private Object invokeJoinpoint() throws Throwable {
        try {
            this.method.setAccessible(true);
            return this.method.invoke(this.target, this.arguments);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
