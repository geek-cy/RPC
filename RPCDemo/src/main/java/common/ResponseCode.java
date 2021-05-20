package common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 方法调用的响应状态码
 * @Author Cy
 * @Date 2021/5/20 22:20
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200,"调用方法成功"),
    FAIL(500,"调用方法失败"),
    METHOD_NOT_FOUND(500,"未找到指定方法"),
    CLASS_NOT_FOUND(500,"未找到指定类");

    private final int code;
    private final String message;
}
