package es.instavino.wine.db.hive;


import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
 
public class HiveInstagramRetriever {
  private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
 
  public static void main(String[] args) throws SQLException {
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    }
    Connection con = DriverManager.getConnection("jdbc:hive://localhost:10000/default", "", "");
    Statement stmt = con.createStatement();
   
    String filepath = "/home/g5/Test.1440209859078.json";
    String sql = "load data local inpath '" + filepath + "' into table instagram";
    System.out.println("Running: " + sql);
    ResultSet res = stmt.executeQuery(sql);
 
    // select * query
    sql = "SELECT b.captionText, b.tags FROM instagram a LATERAL VIEW json_tuple(a.value,'captionText','tags') b AS captionText, tags";
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(String.valueOf(res.getString(1)) + "\t" + res.getString(2));
    }
 
  }
}
