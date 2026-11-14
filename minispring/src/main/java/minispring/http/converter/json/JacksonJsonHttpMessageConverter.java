package minispring.http.converter.json;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import minispring.http.converter.HttpMessageConverter;
import tools.jackson.databind.json.JsonMapper;

/**
 * オブジェクトを Jackson で JSON に変えてレスポンスに書く {@link HttpMessageConverter}。
 */
public class JacksonJsonHttpMessageConverter implements HttpMessageConverter {
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    /**
     * Jackson はどの型のオブジェクトも JSON に変えようとするので、常に書けると返す。
     *
     * @param clazz 書く値の型
     * @return 常に {@code true}
     */
    @Override
    public boolean canWrite(Class<?> clazz) {
        return true;
    }

    /**
     * オブジェクトを JSON に変えてレスポンスに書く。
     *
     * @param value 書くオブジェクト
     * @param response レスポンス
     * @throws IOException レスポンスを書けなかったとき
     */
    @Override
    public void write(Object value, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        this.jsonMapper.writeValue(response.getOutputStream(), value);
    }
}
