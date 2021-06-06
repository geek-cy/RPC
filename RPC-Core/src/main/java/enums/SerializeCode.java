package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Cy
 * @date 2021/6/5 20:56
 */
@AllArgsConstructor
@Getter
public enum SerializeCode {

    KRYO(0);

    private final int code;
}
