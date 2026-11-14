package minispring.beans.factory.config;

/**
 * Bean を作る途中で手を加えるクラスが実装する interface。
 * <p>
 * これを実装したクラスの Bean は、ほかの Bean より先に作られ、
 * {@link minispring.beans.factory.support.DefaultListableBeanFactory} が Bean を作るたびに呼ばれる。
 */
public interface BeanPostProcessor {
}
