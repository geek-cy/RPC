package api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 在调用过程中从客户端传递给服务端的api实体
 * @Author Cy
 * @Date 2021/5/20 21:56
 */
@Data
@AllArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
