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
import org.apache.commons.io.IOUtils;



public class GetUser implements Runnable {
	static DAL data=new DAL();
	
	private final static String GET_PROFILE_QUEUE_NAME = "get_profile";
	private final static String USER_EXCHANGE_NAME =  "user_exchange";
	
	
	public static ArrayList<JSONObject> getUser(JSONObject request) throws IOException, TimeoutException {	
		return data.readSQL(request, "Users");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void deQueue(String EXCHANGE_NAME, String QUEUE_NAME) throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
	     byte[] data;
	    
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	    	JSONObject json=new JSONObject(new String(delivery.getBody()));
	    	ArrayList<JSONObject> array = null;
			try {
				array = getUser(json);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
	    	System.out.print(array.toString());
	    };
	    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
	    
	}
	
	

}
