package minispring.web.servlet.mvc.method;

import java.util.Set;
import minispring.web.bind.annotation.RequestMethod;

/**
 * メソッドが受けるリクエストの条件。
 *
 * @param path 受けるパス
 * @param methods 受ける HTTP メソッド
 */
public record RequestMappingInfo(String path, Set<RequestMethod> methods) {
    /**
     * リクエストの HTTP メソッドを受けるかを返す。
     *
     * @param method リクエストの HTTP メソッド
     * @return 受けるなら {@code true}
     */
    public boolean matchesMethod(String method) {
        return this.methods.stream().anyMatch(requestMethod -> requestMethod.name().equals(method));
    }
}
