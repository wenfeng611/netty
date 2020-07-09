package org.netty.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        int readableBytes = byteBuf.readableBytes();
//        byte[] bytes = new byte[readableBytes];
//        byteBuf.readBytes(bytes);
//        list.add(new Message(new String(bytes)));  //转成Message

        //拆包沾包问题
        int readerIndex = byteBuf.readerIndex();
        int readableBytes = byteBuf.readableBytes();
        //协议自己的内容，我们自定义类长度+内容，长度还是个int，所以读取的内容起码是4个字节
        if (readableBytes > 4) {
            int dataLength = byteBuf.readInt();
            int readableData = byteBuf.readableBytes();
            if (dataLength <= readableData) {
                byte[] bytes = new byte[dataLength];
                byteBuf.readBytes(bytes);
                list.add(new Message(new String(bytes)));
                log.info("解析到一条消息：channel id: {} meaasge: {}",channelHandlerContext.channel().id(), new Message(new String(bytes)));
            } else {
                //读取内容不够，还原位置
                byteBuf.readerIndex(readerIndex);
            }
        } else {
            return;
        }
    }
}
