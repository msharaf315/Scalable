package NettyHTTP;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.rtsp.RtspResponseStatuses;
import io.netty.handler.codec.rtsp.RtspVersions;
import io.netty.util.CharsetUtil;

import org.json.JSONObject;

public class JSONHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf buffer = (ByteBuf) o;
        JSONObject jsonObject = new JSONObject(buffer.toString(CharsetUtil.UTF_8));
        String value = channelHandlerContext.channel().attr(HTTPHandler.MY_KEY).get();
        jsonObject.put("uri", value);
        channelHandlerContext.fireChannelRead(jsonObject);

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
   	ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	cause.printStackTrace();
        ctx.close();
    }

}
