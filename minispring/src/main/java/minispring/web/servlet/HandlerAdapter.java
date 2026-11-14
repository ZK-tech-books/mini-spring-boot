package minispring.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import minispring.web.method.HandlerMethod;

/**
 * リクエストを受けるメソッドを呼び、結果をレスポンスに書く interface。
 */
public interface HandlerAdapter {
    /**
     * メソッドを呼び、戻り値をレスポンスに書く。
     *
     * @param request リクエスト
     * @param response レスポンス
     * @param handler 呼ぶメソッド
     * @throws ServletException メソッドが例外を投げたとき
     * @throws IOException レスポンスを書けなかったとき
     */
    void handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws ServletException, IOException;
}
