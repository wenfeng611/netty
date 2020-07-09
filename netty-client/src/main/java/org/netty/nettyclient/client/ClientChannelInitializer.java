package org.netty.nettyclient.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.codec.MessageDecoder;
import org.netty.common.codec.MessageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientChannelInitializer  extends ChannelInitializer<Channel> {


    @Autowired
    ClientMessageHandler clientMessageHandler;

    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        log.info("自定义初始化开始。。。。。。");

        // 空闲检测  客户端10秒钟发一次心跳
        channelPipeline.addLast(new IdleStateHandler(10, 0, 0));

        channelPipeline.addLast(new MessageEncoder());  //消息编码器
        channelPipeline.addLast(new MessageDecoder());  //消息解码器
        channelPipeline.addLast(clientMessageHandler);
    }


}
