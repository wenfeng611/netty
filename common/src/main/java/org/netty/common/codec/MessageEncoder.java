package org.netty.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
    o.netty.channel.ChannelPipelineException: org.netty.common.codec.MessageEncoder is not a @Sharable handler, so can't be added or removed multiple times.
    不用单例 再用的地方new 出来
 */

@Slf4j
public class MessageEncoder extends MessageToByteEncoder<Message> {

    //编码 可以写入长度 这样读的时候读取指定得长度
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        byte[] content = JSON.toJSONBytes(message);
        log.info(message.getContent());
        //byteBuf.writeInt(content.length);
        byteBuf.writeBytes(content);
    }
}
