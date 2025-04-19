package com.example.gateway;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    public GatewayConfig() {
        logger.info("GatewayConfig is being initialized...");
    }

    @Bean
    public WebServerFactoryCustomizer<NettyReactiveWebServerFactory> nettyServerCustomizer() {
        logger.info("Creating nettyServerCustomizer bean...");
        return factory -> factory.addServerCustomizers(httpServer -> {
            logger.info("Customizing Netty server with maxHeaderSize=65536...");
            return httpServer
                    .httpRequestDecoder(spec -> spec.maxHeaderSize(65536)) // 64KB
                    .doOnConnection(connection -> {
                        logger.info("Adding header filter to remove Forwarded headers...");
                        connection.addHandlerFirst("headerFilter", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if (msg instanceof HttpRequest) {
                                    HttpRequest request = (HttpRequest) msg;
                                    logger.debug("Removing Forwarded headers from request...");
                                    request.headers().remove("Forwarded");
                                }
                                super.channelRead(ctx, msg);
                            }
                        });
                    });
        });
    }
}