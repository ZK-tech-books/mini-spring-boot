package minispring.transaction.interceptor;

import minispring.aop.Pointcut;
import minispring.aop.PointcutAdvisor;
import minispring.aopalliance.intercept.MethodInterceptor;

/**
 * {@link minispring.transaction.annotation.Transactional} の付いたメソッドに、
 * {@link TransactionInterceptor} を差し込むと決める {@link PointcutAdvisor}。
 */
public class BeanFactoryTransactionAttributeSourceAdvisor implements PointcutAdvisor {
    private final Pointcut pointcut = new TransactionAttributeSourcePointcut();
    private final MethodInterceptor advice = new TransactionInterceptor();

    /**
     * {@link TransactionAttributeSourcePointcut} を返す。
     *
     * @return 差し込む場所を決める {@link Pointcut}
     */
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    /**
     * {@link TransactionInterceptor} を返す。
     *
     * @return 差し込む処理
     */
    @Override
    public MethodInterceptor getAdvice() {
        return this.advice;
    }
}
