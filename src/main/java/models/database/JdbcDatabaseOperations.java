package models.database;

import models.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDatabaseOperations implements DatabaseOperations {

    private final Connection connection;

    public JdbcDatabaseOperations(String driver, String url) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        this.connection = DriverManager.getConnection(url);
    }

    @Override
    public void addMessage(Message message) {
        try {
            String sql = "INSERT INTO ChatMessages (author, text, created)" +
                    "VALUES ("
                    + "''" + message.getAuthor() + "''"
                    + "''" + message.getText() + "''"
                    + "''" + Timestamp.valueOf(message.getCreated()) + "''"
                    + ");";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ChatMessages";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            /**naplnění pole zprávami
            *
            */
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return messages;
    }
}
