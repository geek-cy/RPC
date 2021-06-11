package api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端调用的实体
 * @Author Cy
 * @Date 2021/6/4 19:19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HelloObject {

    private Integer id;
    private String message;
}
