package org.netty.nettyserver.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.codec.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable //可以被多个 Channel 使用
public class MessageHandler extends SimpleChannelInboundHandler<Message> {

    @Autowired
    private ChannelManager channelManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection");
        // 添加到管理器中
        channelManager.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        // 从管理器中移除
        channelManager.remove(ctx.channel());
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
       log.info("handle message: {}",message.getContent());
    }
}
