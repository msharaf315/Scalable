package NettyHTTP;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;

public class DAL {
	static MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb+srv://Andrew:Andrew1@cluster0.kdkeq.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"));	
	static MongoDatabase database = mongoClient.getDatabase("myFirstDatabase");
	static PostgreSQL postgres=new PostgreSQL();
	static ArrayList<JSONObject> dbres=new ArrayList<JSONObject>();

	private static void writeMongo(JSONObject request2,String collection ) {
		
		
	}

	
	public ArrayList<JSONObject> readMongo(JSONObject request2,String collection) {
		MongoCollection<org.bson.Document> col = database.getCollection(collection);
		if(request2.has("_id")) {
			ObjectId id= new ObjectId((String)request2.get("_id"));  
			BasicDBObject whereQuery = new BasicDBObject();
		    whereQuery.put("_id", id);
		    col.find(whereQuery).forEach(printBlock);		    
		}
		else {
		    col.find().forEach(printBlock);
		}
	    return dbres;
	}
	public ArrayList<JSONObject> readSQL(JSONObject request2,String table) {
		String query = "";
		if(request2.has("id")) {
			query = "SELECT * FROM \""+table+"\" WHERE id="+request2.get("id");
		}else if(request2.has("skip")&&request2.has("limit")) {
			
			query = "SELECT * FROM \""+table+"\" LIMIT "+request2.get("limit")+ " OFFSET "+ request2.get("skip");	
		}else {
			query = "SELECT * FROM \""+table+"\"";	
				
		}
		try {
			
			ResultSet rs = postgres.st.executeQuery(query);
	        ResultSetMetaData rsmd = rs.getMetaData();
	        while(rs.next()) {
	        	JSONObject obj = new JSONObject();
	            int numColumns = rsmd.getColumnCount();
	            for (int i=1; i<=numColumns; i++) {
	                String column_name = rsmd.getColumnName(i);
	                obj.put(column_name, rs.getObject(column_name));
	            }
	            
                dbres.add(obj);
	        }
//		      rs.close();
//		      postgres.st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbres;
		
		
	}
	public ArrayList<JSONObject> writeSQL(JSONObject request2,String table) {
		try {
			ResultSet rs = postgres.st.executeQuery("SELECT * FROM \"messageNotifications\" WHERE id=1");
	        ResultSetMetaData rsmd = rs.getMetaData();
	        JSONObject obj = new JSONObject();
	        while(rs.next()) {
	            int numColumns = rsmd.getColumnCount();
	            for (int i=1; i<=numColumns; i++) {
	                String column_name = rsmd.getColumnName(i);
	                obj.put(column_name, rs.getObject(column_name));
	            }
                dbres.add(obj);
	        }
		      rs.close();
		      postgres.st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dbres;
		
		
	}
	
	static Block<org.bson.Document> printBlock = new Block<org.bson.Document>() {
        public void apply(final org.bson.Document document) {
        	JSONObject jsonObject = new JSONObject(document.toJson());
            dbres.add(jsonObject);
        }
    };
}
