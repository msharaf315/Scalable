package NettyHTTP;
import java.sql.*;

public class PostgreSQL {
	   	  String url = "jdbc:postgresql://tai.db.elephantsql.com:5432/xouiehjd";
	      String username = "xouiehjd";
	      String password = "HaqX4ngqyBWo3PdyrEQRJooqfS5rMFm9";
	      Connection db;
	      Statement st;

	 public PostgreSQL() {
		  try {
	          Class.forName("org.postgresql.Driver");
	      }
	      catch (java.lang.ClassNotFoundException e) {
	          System.out.println(e.getMessage());
	      }
		    try {
		          db = DriverManager.getConnection(url, username, password);
		          st = db.createStatement();
		          }
		      catch (java.sql.SQLException e) {
		    	  e.printStackTrace();
		      }
	}


}