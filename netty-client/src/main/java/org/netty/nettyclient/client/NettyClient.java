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
import javax.annotation.PreDestroy;

@Component
public class NettyClient {

    @Value("${netty.client.ip}")
    private String ip;
    @Value("${netty.client.port}")
    private int port;

    @Autowired
    ClientChannelInitializer clientChannelInitializer;

    Channel channel;

    EventLoopGroup worker = new NioEventLoopGroup();

    @PostConstruct
    public void bind() {

        try {
            Bootstrap b = new Bootstrap();
            b.group(worker).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(clientChannelInitializer);

            ChannelFuture connect = b.connect(ip, port);
            connect.sync();
            connect.channel().writeAndFlush(new Message("aaaaa"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void shutdown() {
        // 关闭 Netty Client
        if (channel != null) {
            channel.close();
        }
        // 优雅关闭一个 EventLoopGroup 对象
        worker.shutdownGracefully();
    }
}
