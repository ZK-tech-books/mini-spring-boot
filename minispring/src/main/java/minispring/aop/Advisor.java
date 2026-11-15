package minispring.aop;

import minispring.aopalliance.intercept.MethodInterceptor;

/**
 * 差し込む処理を持つ interface。
 */
public interface Advisor {
    /**
     * 差し込む処理を返す。
     *
     * @return 差し込む処理
     */
    MethodInterceptor getAdvice();
}
