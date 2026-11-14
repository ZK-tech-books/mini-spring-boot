package minispring.http.converter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * メソッドの戻り値を、文字や JSON などの形式に変えてレスポンスに書く interface。
 */
public interface HttpMessageConverter {
    /**
     * 指定した型の値を書けるかを返す。
     *
     * @param clazz 書く値の型
     * @return 書けるなら {@code true}
     */
    boolean canWrite(Class<?> clazz);

    /**
     * 値を形式に変えてレスポンスに書く。
     *
     * @param value 書く値
     * @param response レスポンス
     * @throws IOException レスポンスを書けなかったとき
     */
    void write(Object value, HttpServletResponse response) throws IOException;
}
