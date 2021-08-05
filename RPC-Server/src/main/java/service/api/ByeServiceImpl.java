package service.api;

import annotation.Service;

/**
 * @author Cy
 * @date 2021/6/10 21:00
 */
@Service("ByeService")
public class ByeServiceImpl  {

    public String bye(String name) {
        return "bye";
    }
}
