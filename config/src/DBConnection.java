import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static Connection connection;

    public static Connection createDBConnection(){
        try{
            // loading driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // get connection
            String url = "jdbc:mysql://localhost:3306/MyEvents";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);

        }catch (Exception error){
            error.printStackTrace();
        }
        return connection;
    }
}
