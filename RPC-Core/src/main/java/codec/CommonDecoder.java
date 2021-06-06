package codec;

import entity.RpcRequest;
import entity.RpcResponse;
import enums.RpcType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import serializer.CommonSerializer;

import java.util.List;

/**
 * 将字节转为消息,缓存入站的消息，读完后再处理
 * @author Cy
 * @date 2021/6/5 21:02
 */
public class CommonDecoder extends ByteToMessageDecoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic = in.readInt();
        if(magic != MAGIC_NUMBER) throw new RuntimeException("无法识别的协议包");
        Class<?> clazz;
        int rpcType = in.readInt();
        if(rpcType == RpcType.REQUEST.getType()) clazz = RpcRequest.class;
        else if(rpcType == RpcType.RESPONSE.getType()) clazz = RpcResponse.class;
        else throw new RuntimeException("无法识别的数据包");
        int code = in.readInt();
        CommonSerializer serializer = CommonSerializer.getSerializer(code);
        if(serializer == null) throw new RuntimeException("不识别的反序列化器");
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        Object deserialize = serializer.deserialize(bytes, clazz);
        // 添加到解码消息的 List 中
        out.add(deserialize);
    }
}
