package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    // TODO: retrieve all messages from the message table.
    // Returns a list of messages if found any, null if no records
    // in message table
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sqlString = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                ));
            }

            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    // TODO: retrieve a message from the message table by message_id
    // Returns message obj if found a records with provided ID,
    // otherwise returns null
    public Message getMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sqlString = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setInt(1, message_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                );
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    // TODO: retrieve a message from the message table by posted_by
    // Returns a list of messages by author_id if found any, null if no records
    // in message table
    public List<Message> getMessagesPostedByUser(int author_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sqlString = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setInt(1, author_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                ));
            }

            return messages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    // TODO: add new message to the message table
    // Returns an ID of a created message if a record
    // successfully added to DB, otherwise returns 0
    public int addMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sqlString = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getInt("message_id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }
    
    // TODO: delete a message by ID
    // Remove the record from table message by message_id. Returning message obj
    // if the message with such id exist, otherwise return null
    public void deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sqlString = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setInt(1, message_id);
            
            preparedStatement.execute();
            return;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // TODO: update a message by ID
    // Update the record from table message by message_id. Returning message_id
    // if the message with such id exist, otherwise return 0
    public int updateMessage(Message message, int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sqlString = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message_id);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getInt("message_id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }
}
