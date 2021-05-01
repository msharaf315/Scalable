package NettyHTTP;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import UserService.GetUser;

public class User {
	static JSONObject request;
	static String uri;
	static DAL data=new DAL();
	
// rabbitMQ
	private final static String GET_PROFILE_QUEUE_NAME = "get_profile";
	private final static String USER_EXCHANGE_NAME =  "user_exchange";
	public User(String uri, JSONObject request) {
		this.request=request;
		this.uri=uri;
	}
	
	public void route() throws IOException, TimeoutException{
		Channel channel = connect(USER_EXCHANGE_NAME);
		switch (uri) {
		case "login": {
			 send(USER_EXCHANGE_NAME, GET_PROFILE_QUEUE_NAME, GET_PROFILE_QUEUE_NAME, request.toString().getBytes());
			 GetUser user=new GetUser();
			 user.deQueue(USER_EXCHANGE_NAME, GET_PROFILE_QUEUE_NAME);
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
	
	public static Channel connect(String EXCHANGE_NAME) throws IOException, TimeoutException
	{
		
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
        	
        return channel;
        }
	}
	
	public static void send(String EXCHANGE_NAME,
							String KEY, String QUEUE_NAME, byte[] payload) throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
        	channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        	channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        	channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, KEY);
        	channel.basicPublish(EXCHANGE_NAME, KEY, null, payload);	
        	
        
        }
	}
	

}
