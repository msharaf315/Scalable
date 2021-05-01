package NettyHTTP;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HTTPHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;
    private String requestBody;
    private long correlationId;
    static volatile FullHttpResponse responseBody;
    public static AttributeKey<String> MY_KEY = AttributeKey.valueOf("MY_KEY");
    ExecutorService executorService = Executors.newCachedThreadPool();


    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        ctx.fireChannelReadComplete();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;
            ctx.channel().attr(MY_KEY).set(request.uri().toString());
            if (HttpUtil. is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            ctx.fireChannelRead(content.copy());
            
        }
        if (msg instanceof LastHttpContent) {
            LastHttpContent trailer = (LastHttpContent) msg;
            writeresponse(trailer, ctx);
        }
    }


 
	private void writeresponse(HttpObject trailer, ChannelHandlerContext ctx) {
        
	     ctx.writeAndFlush(responseBody);
	}

	private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	cause.printStackTrace();
        ctx.close();
    }

}

