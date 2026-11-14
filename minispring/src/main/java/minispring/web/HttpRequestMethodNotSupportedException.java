package minispring.web;

import jakarta.servlet.ServletException;

/**
 * パスに対応するメソッドはあるが、HTTP メソッドが合わないときに投げる例外。
 */
public class HttpRequestMethodNotSupportedException extends ServletException {
    /**
     * 例外を作る。
     *
     * @param method リクエストの HTTP メソッド
     */
    public HttpRequestMethodNotSupportedException(String method) {
        super("Request method '" + method + "' is not supported");
    }
}
