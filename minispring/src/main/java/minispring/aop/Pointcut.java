package minispring.aop;

import java.lang.reflect.Method;

/**
 * 処理を差し込む場所を決める interface。
 */
public interface Pointcut {
    /**
     * メソッドに処理を差し込むかを返す。
     *
     * @param method 調べるメソッド
     * @return 差し込むなら {@code true}
     */
    boolean matches(Method method);
}
