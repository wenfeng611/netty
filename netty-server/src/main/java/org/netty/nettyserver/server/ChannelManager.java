package org.netty.nettyserver.server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.netty.common.codec.Message;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

//统一管理channel
@Component
@Slf4j
public class ChannelManager {

    //记录channelid 和channel 映射关系
    ConcurrentHashMap<ChannelId, Channel> channelIdAndChannelMap = new ConcurrentHashMap<ChannelId, Channel>();

    //添加channel
    public void add(Channel channel) {
        channelIdAndChannelMap.put(channel.id(), channel);
        log.info("添加一个连接：{}]", channel.id());
    }

    //移除channel
    public void remove(Channel channel) {
        // 移除 channel
        channelIdAndChannelMap.remove(channel.id());
        log.info("移除一个连接： {}]", channel.id());
    }

    public void sendMessageToAll(Message message) {
        for (Channel channel : channelIdAndChannelMap.values()) {
            if (!channel.isActive()) {
                log.info("channel inactive");
                return;
            }
            // 发送消息
            channel.writeAndFlush(message);
        }
    }
}
