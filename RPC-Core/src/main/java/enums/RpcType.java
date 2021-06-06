package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Cy
 * @date 2021/6/5 20:49
 */
@AllArgsConstructor
@Getter
public enum RpcType {
    REQUEST(0),
    RESPONSE(1);

    private final int type;
}
