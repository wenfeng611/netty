package org.netty.nettyserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class NettyServer {

    @Autowired
    private ServerChannelInitializer serverChannelInitializer;

    @Value("${netty.port}")
    private int port;

    private Channel channel;

    //Rector线程模型 主从
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();

    //启动nettyserver
    @PostConstruct
    public void startserver() {
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)        // 服务端 accept 队列的大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                    .childOption(ChannelOption.TCP_NODELAY, true)  //允许较小的数据包的发送，降低延迟;
                    .childHandler(serverChannelInitializer);             //自定义channel初始化逻辑

            ChannelFuture sync = server.bind(port).sync();
            channel = sync.channel();
            log.info("netty server 启动成功 端口是：{}",port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            boss.shutdownGracefully();
//            worker.shutdownGracefully();
        }
    }

    @PreDestroy
    public void shutdown() {
        // 关闭 Netty Server
        if (channel != null) {
            channel.close();
        }
        // 优雅关闭两个 EventLoopGroup 对象
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }
}
