package org.netty.nettyserver.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.codec.MessageDecoder;
import org.netty.common.codec.MessageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//Channel 创建时，实现自定义的初始化逻辑
@Component
@Slf4j
public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    @Autowired
    MessageHandler messageHandler;

    //自定义的初始化逻辑
    protected void initChannel(Channel channel) throws Exception {
        log.info("自定义初始化开始。。。。。。");
        ChannelPipeline channelPipeline = channel.pipeline();
        //心跳检测
        channelPipeline.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));

        channelPipeline.addLast(new MessageEncoder());  //消息编码器
        channelPipeline.addLast(new MessageDecoder());  //消息解码器
        channelPipeline.addLast(messageHandler);  //业务处理器
    }
}
