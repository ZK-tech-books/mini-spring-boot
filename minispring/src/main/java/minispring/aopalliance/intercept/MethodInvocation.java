package minispring.aopalliance.intercept;

import java.lang.reflect.Method;

/**
 * プロキシのメソッドの呼び出し。差し込む処理はこれを受け取り、{@link #proceed()} で次に進める。
 */
public interface MethodInvocation {
    /**
     * 呼ばれたメソッドを返す。
     *
     * @return 呼ばれたメソッド
     */
    Method getMethod();

    /**
     * 次の差し込む処理を呼ぶ。差し込む処理が残っていなければ、本物のメソッドを呼ぶ。
     *
     * @return 本物のメソッドの戻り値
     * @throws Throwable 差し込む処理か本物のメソッドが投げた例外
     */
    Object proceed() throws Throwable;
}
