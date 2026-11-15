package minispring.aopalliance.intercept;

/**
 * メソッドの呼び出しの前後に処理を差し込む interface。
 */
public interface MethodInterceptor {
    /**
     * 前後に処理を足し、{@link MethodInvocation#proceed()} で次に進める。
     *
     * @param invocation メソッドの呼び出し
     * @return 本物のメソッドの戻り値
     * @throws Throwable 差し込む処理か本物のメソッドが投げた例外
     */
    Object invoke(MethodInvocation invocation) throws Throwable;
}
