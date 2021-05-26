package staticProxy;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 21:14
 */
public class RealMovie implements Movie{
    @Override
    public void play() {
        System.out.println("开始播放电影");
    }
}
