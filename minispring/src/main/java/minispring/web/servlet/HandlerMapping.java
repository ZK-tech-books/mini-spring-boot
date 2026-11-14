package minispring.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import minispring.web.HttpRequestMethodNotSupportedException;
import minispring.web.method.HandlerMethod;

/**
 * リクエストを受けるメソッドを探す interface。
 */
public interface HandlerMapping {
    /**
     * リクエストを受けるメソッドを返す。
     *
     * @param request リクエスト
     * @return リクエストを受けるメソッド。パスに対応するメソッドがなければ {@code null}
     * @throws HttpRequestMethodNotSupportedException パスは合うが HTTP メソッドが合わないとき
     */
    HandlerMethod getHandler(HttpServletRequest request) throws HttpRequestMethodNotSupportedException;
}
