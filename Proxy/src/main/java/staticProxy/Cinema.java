package staticProxy;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 21:15
 */
public class Cinema implements Movie{

    RealMovie realMovie;

    public Cinema(RealMovie realMovie){
        super();
        this.realMovie = realMovie;
    }

    @Override
    public void play() {
        guanggao(true);
        realMovie.play();
        guanggao(false);
    }

    public void guanggao(boolean isStart){
        if(isStart){
            System.out.println("广告1");
        } else System.out.println("广告2");
    }
}
