package NettyHTTP;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Redis {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
//        for(int i =0;i<100000;i++) {
//
//            jedis.set("foo"+i, "bar");
//        }
//        String value = jedis.get("foo1");
//       System.out.print(value);
       
    
        //String task = jedis.rpop("queue#tasks");
        //SETSS


        //Hashes 
//        jedis.hset("user#1", "name", "Peter");
//        jedis.hset("user#1", "job", "politician");
//        		
//        String name = jedis.hget("user#1", "name");
//        		
        
        Map<String, String> fields = jedis.hgetAll("user#1");
//        String job = fields.get("job");
        System.out.println(fields);
    }

}
