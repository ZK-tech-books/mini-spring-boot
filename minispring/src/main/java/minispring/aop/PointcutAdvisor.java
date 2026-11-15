package minispring.aop;

/**
 * 差し込む処理と、差し込む場所を決める {@link Pointcut} の組を持つ interface。
 */
public interface PointcutAdvisor extends Advisor {
    /**
     * 差し込む場所を決める {@link Pointcut} を返す。
     *
     * @return 差し込む場所を決める {@link Pointcut}
     */
    Pointcut getPointcut();
}
