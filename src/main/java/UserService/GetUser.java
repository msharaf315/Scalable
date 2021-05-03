package UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import NettyHTTP.DAL;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
//import org.apache.commons.io.IOUtils;



public class GetUser implements Runnable {
	static DAL data=new DAL();
	
	private final static String GET_PROFILE_QUEUE_NAME = "get_profile";
	private final static String USER_EXCHANGE_NAME =  "user_exchange";
	static Channel channel;
	
	public static ArrayList<JSONObject> getUser(JSONObject request) throws IOException, TimeoutException {	
		return data.readSQL(request, "Users");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public GetUser() throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    this.channel = connection.createChannel();
	    channel.exchangeDeclare(USER_EXCHANGE_NAME, "direct");
	    channel.queueDeclare(GET_PROFILE_QUEUE_NAME + "_RESPONSE", true, false, false, null);
    	channel.queueBind( GET_PROFILE_QUEUE_NAME + "_RESPONSE", USER_EXCHANGE_NAME, GET_PROFILE_QUEUE_NAME + "_RESPONSE");
    	
    	DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	    	JSONObject json=new JSONObject(new String(delivery.getBody()));
	    	ArrayList<JSONObject> array = null;
			try {
				array = getUser(json);
				System.out.println(array.get(0).toString());
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] payload = array.toString().getBytes();
			
			channel.basicPublish(USER_EXCHANGE_NAME,  GET_PROFILE_QUEUE_NAME + "_RESPONSE", null, payload);	
		};
	    
	    channel.basicConsume(GET_PROFILE_QUEUE_NAME + "_REQUEST", true, deliverCallback, consumerTag -> { });
	    
	}
	
	
	
	
	

}
