package client.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty重用Channel
 * @author Cy
 * @date 2021/6/7 23:30
 */
@Slf4j
public class ChannelProvider {
    private final Map<String, Channel> channelMap;

    public ChannelProvider(){
        channelMap = new ConcurrentHashMap<>();
    }


    // InetSocketAddress封装了IP地址与端口
    public Channel get(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        if(channelMap.containsKey(key)){
            Channel channel = channelMap.get(key);
            if(channel != null && channel.isActive()){
                return channel;
            } else channelMap.remove(key);
        }
        return null;
    }

    public void set(InetSocketAddress inetSocketAddress,Channel channel){
        String key = inetSocketAddress.toString();
        channelMap.put(key,channel);
    }

    public void remove(InetSocketAddress inetSocketAddress){
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("Channel map Size:[{}]",channelMap.size());
    }
}
