package api;

import annotation.Service;

/**
 * @author Cy
 * @date 2021/6/10 21:00
 */
@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye";
    }
}
