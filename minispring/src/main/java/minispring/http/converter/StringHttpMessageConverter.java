package minispring.http.converter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文字を text/plain でレスポンスに書く {@link HttpMessageConverter}。
 */
public class StringHttpMessageConverter implements HttpMessageConverter {
    /**
     * 値が文字のときだけ書けると返す。
     *
     * @param clazz 書く値の型
     * @return 文字なら {@code true}
     */
    @Override
    public boolean canWrite(Class<?> clazz) {
        return String.class == clazz;
    }

    /**
     * 文字を text/plain でレスポンスに書く。
     *
     * @param value 書く文字
     * @param response レスポンス
     * @throws IOException レスポンスを書けなかったとき
     */
    @Override
    public void write(Object value, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write((String) value);
    }
}
