package org.netty.nettyclient.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.codec.MessageDecoder;
import org.netty.common.codec.MessageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientChannelInitializer  extends ChannelInitializer<Channel> {

    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        log.info("自定义初始化开始。。。。。。");
        channelPipeline.addLast(new MessageEncoder());  //消息编码器
        channelPipeline.addLast(new MessageDecoder());  //消息解码器
    }
}
