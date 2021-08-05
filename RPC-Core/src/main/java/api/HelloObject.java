package api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cy
 * @date 2021/6/21 15:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HelloObject {

    private Integer id;
    private String message;
}
