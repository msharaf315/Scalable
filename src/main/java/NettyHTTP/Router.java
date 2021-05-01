package NettyHTTP;

import java.util.ArrayList;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class Router extends SimpleChannelInboundHandler<Object>  {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		JSONObject request = (JSONObject)msg;
//		System.out.println(((JSONObject)msg).toString());
		String uri= (String) request.get("uri");
		request.remove("uri");
		switch (uri.split("/")[1]) {
		case "user": {
			User newUser = new User(uri.split("/")[2],request);
			newUser.route();
			ArrayList<JSONObject> rand=new ArrayList<JSONObject>();
			response(rand, ctx);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + uri.split("/")[1]);
			
		}
	}
	protected void response(ArrayList<JSONObject> jsons,ChannelHandlerContext ctx) {
		  ctx.fireChannelRead(jsons); 
	      ByteBuf content = Unpooled.copiedBuffer(jsons.toString(), CharsetUtil.UTF_8);  
	      FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);  
	      response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");  
	      response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
	      HTTPHandler.responseBody = response;
	}
	  


}
