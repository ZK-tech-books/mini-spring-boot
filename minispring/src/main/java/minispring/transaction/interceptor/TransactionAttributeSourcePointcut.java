package minispring.transaction.interceptor;

import java.lang.reflect.Method;
import minispring.aop.Pointcut;
import minispring.transaction.annotation.Transactional;

/**
 * {@link Transactional} の付いたメソッドに処理を差し込むと決める {@link Pointcut}。
 */
public class TransactionAttributeSourcePointcut implements Pointcut {
    /**
     * メソッドに {@link Transactional} が付いているかを返す。
     *
     * @param method 調べるメソッド
     * @return 付いていれば {@code true}
     */
    @Override
    public boolean matches(Method method) {
        return method.isAnnotationPresent(Transactional.class);
    }
}
