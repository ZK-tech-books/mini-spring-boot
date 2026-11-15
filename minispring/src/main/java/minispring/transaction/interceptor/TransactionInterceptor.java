package minispring.transaction.interceptor;

import minispring.aopalliance.intercept.MethodInterceptor;
import minispring.aopalliance.intercept.MethodInvocation;

/**
 * メソッドの前後で、トランザクションを始めることと、確定か取り消しを出力する {@link MethodInterceptor}。
 * <p>
 * DB がないので、トランザクションの実体は扱わない。
 */
public class TransactionInterceptor implements MethodInterceptor {
    /**
     * トランザクションを始めると出力してから次に進め、例外がなければ確定、あれば取り消しと出力する。
     *
     * @param invocation メソッドの呼び出し
     * @return 本物のメソッドの戻り値
     * @throws Throwable 本物のメソッドが投げた例外
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        System.out.println("Begin transaction: " + methodName);
        try {
            Object result = invocation.proceed();
            System.out.println("Commit transaction: " + methodName);
            return result;
        } catch (Throwable e) {
            System.out.println("Rollback transaction: " + methodName);
            throw e;
        }
    }
}
