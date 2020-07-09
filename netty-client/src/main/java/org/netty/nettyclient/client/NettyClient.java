package org.netty.nettyclient.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.netty.common.codec.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NettyClient {

    @Value("${netty.client.ip}")
    private String ip;
    @Value("${netty.client.port}")
    private int port;

    @Autowired
    ClientChannelInitializer clientChannelInitializer;

    @PostConstruct
    public void bind() {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(worker).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(clientChannelInitializer);

            ChannelFuture connect = b.connect(ip, port);
            connect.sync();
            connect.channel().writeAndFlush(new Message("hello"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
