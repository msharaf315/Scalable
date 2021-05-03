package NettyHTTP;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import UserService.GetUser;

public class User {
	static JSONObject request;
	static String uri;
	static DAL data=new DAL();
	static Channel channel;
// rabbitMQ
	private final static String GET_PROFILE_QUEUE_NAME = "get_profile";
	private final static String USER_EXCHANGE_NAME =  "user_exchange";
	public User(String uri, JSONObject request) throws IOException, TimeoutException {
		this.request=request;
		this.uri=uri;
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    this.channel = connection.createChannel();
	    channel.exchangeDeclare(USER_EXCHANGE_NAME, "direct");
	    GetUser user=new GetUser();
	}
	
	public void route() throws IOException, TimeoutException{
		switch (uri) {
		case "login": {
			 send(USER_EXCHANGE_NAME, GET_PROFILE_QUEUE_NAME, request.toString().getBytes());
			  DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				  	System.out.println("1");
			    	JSONObject json=new JSONObject(new String(delivery.getBody()));
			    	System.out.println("2");
			    	JSONArray array = null;
			    	System.out.println("3");
					array = new JSONArray(json);
					System.out.println("4");
			    };
			 channel.basicConsume(GET_PROFILE_QUEUE_NAME + "_RESPONSE", true, deliverCallback, consumerTag -> { });
			 break;
		}
		case "signup":{
//			return Signup(request);
			break;	
		}
		
		}
		
	}
	
	
	
	private static ArrayList<JSONObject> Signup(JSONObject request2) {
		return data.writeSQL(request2, "Users");
	}
	
	

	
	public static void send(String EXCHANGE_NAME, String QUEUE_NAME, byte[] payload) throws IOException, TimeoutException {
		
		System.out.println("1");
		BasicProperties props = new BasicProperties
                .Builder()
                .replyTo("QUEUE_NAME" + "_RESPONSE")
                .build();
			channel.queueDeclare(QUEUE_NAME + "_REQUEST", true, false, false, null);
			 channel.queueBind( QUEUE_NAME + "_REQUEST", EXCHANGE_NAME, QUEUE_NAME + "_REQUEST");
            channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME + "_REQUEST", props, payload);	
           
	}
	

}
