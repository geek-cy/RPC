import dynamic.*;
import org.junit.Test;
import staticProxy.Cinema;
import staticProxy.Movie;
import staticProxy.RealMovie;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 21:02
 */
public class TestProxy {

    @Test
    public void test(){
        // 被代理类
        RealMovie realmovie = new RealMovie();
        // 代理类聚合被代理类
        // 继承同一个接口
        Movie movie = new Cinema(realmovie);
        movie.play();
    }


    public static void main(String[] args) {
        // 被代理类
        MaoTai maoTai = new MaoTai();
        WuLiangYe wuLiangYe = new WuLiangYe();
        Cigarette cigarette = new Cigarette();
        //  InvocationHandler的实现类
        InvocationHandler i = new GuiTai(maoTai);
        InvocationHandler j = new GuiTai(wuLiangYe);
        InvocationHandler k = new GuiTai(cigarette);
        // 创建代理类
        // (接口)Proxy.newProxyInstance(代理实例类加载器,代理实例接口,InvocationHandler实现类)
        Sell o = (Sell)Proxy.newProxyInstance(MaoTai.class.getClassLoader(), MaoTai.class.getInterfaces(), i);
        Sell s = (Sell)Proxy.newProxyInstance(MaoTai.class.getClassLoader(),MaoTai.class.getInterfaces(),j);
        SellCigarette l = (SellCigarette)Proxy.newProxyInstance(Cigarette.class.getClassLoader(),Cigarette.class.getInterfaces(),k);
        // 被代理类即代理的实例调用方法，代理会通知转发给内部的InvocationHandler的实现类
        o.sell();
        s.sell();
        l.sell();

        ProxyClient proxyClient = new ProxyClient();
        ProxyInterface proxy = proxyClient.getProxy(ProxyInterface.class);
        proxy.use();
    }
}
