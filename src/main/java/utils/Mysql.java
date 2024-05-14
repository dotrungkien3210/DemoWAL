package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class Mysql {
    public String PROPERTIES_FILE = "config/mysql.properties";


    public Mysql() throws SQLException {

    }

    public void transaction(int eventID, String eventName,String eventStatus) throws SQLException {
        // Kết nối đến cơ sở dữ liệu
        Connection connection = DriverManager.getConnection("jdbc:mysql://10.3.106.254:3306/event_test1","kiendt","kiendt01");
        connection.setAutoCommit(false);
        Statement statement = null;
        ResultSet resultSet = null;

        File file = new File();
        try {
            String updatesql = "UPDATE event set eventName = ? WHERE eventID = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(updatesql);
            preparedStatement1.setString(1,eventName);
            preparedStatement1.setInt(2,eventID);

            String sql = "SELECT * FROM event WHERE eventID = ? FOR UPDATE NOWAIT";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
            preparedStatement2.setInt(1,eventID);

            ResultSet rs =  preparedStatement2.executeQuery();
            // Nếu có hàng phù hợp, nó sẽ được khóa cho việc cập nhật
            if (!rs.next()) {
                System.out.println("No results found.");
            }

            while (rs.next()) {
                // Thực hiện các hoạt động cập nhật trên hàng đã chọn
                file.WALs(eventID,eventStatus);
                System.out.println("Xử lý công việc");
                Thread.sleep(200000);
                preparedStatement1.executeUpdate();

                // Commit giao dịch
                connection.commit();
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    // Rollback nếu có lỗi
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            eventStatus = "SUCCESS";
            file.WALs(eventID,eventStatus);
            // Đóng ResultSet, Statement và Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true); // Đặt lại chế độ tự động commit
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(){
        String sqlInsert = "INSERT INTO event (eventID, eventName, eventStatus)\n" +
                "VALUES (?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE" +
                " eventName = VALUES(eventName)," +
                " eventStatus = VALUES(eventStatus)";
//        PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
//        preparedStatement.setInt(1,eventID);
//        preparedStatement.setString(2,eventName);
//        preparedStatement.setString(3,eventStatus);
        //            int rowsAffected = preparedStatement.executeUpdate();
    }

    public static int connect() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://10.3.106.254:3306/event_test1","kiendt","kiendt01");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from event");
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
            con.close();
        }catch(Exception e){ System.out.println(e);}

        return 0;

    }

    public static void main(String[] args) throws SQLException {
        Mysql mysql = new Mysql();
            mysql.transaction(6,"qaz","WAITING");
    }


}
