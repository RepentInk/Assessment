package Intel;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class connectDB {
      //declaring a variable to hold the connection
     Connection conn = null;
    public static Connection ConnecrDb(){
        try{
          Class.forName("org.sqlite.JDBC");
          Connection conn = DriverManager.getConnection("jdbc:sqlite:westpoint.sqlite"); 
          //JOptionPane.showMessageDialog(null, "Connection Established");
          return conn;
        }catch(Exception e){  
         JOptionPane.showMessageDialog(null, e);
         return null;
        }
         
    }
}
