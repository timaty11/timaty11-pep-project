package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    // Retrieve all messages from DB
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    // Retrieve message by its id. Returns message obj if record with such message_id
    // exists, null otherwise
    public Message getMessageByID(int message_id) {
        return messageDAO.getMessageByID(message_id);
    }

    // Retrieve message by its author_id. Returns messages obj list if records with such account_id
    // exists, null otherwise
    public List<Message> getMessagesByAutohrID(int author_id) {
        return messageDAO.getMessagesPostedByUser(author_id);
    }

    // Add message to a DB
    public Message addMessage(Message enteredMessage) {
        int created_id = messageDAO.addMessage(enteredMessage);

        return messageDAO.getMessageByID(created_id);
    }

    // Delete message from a DB
    public Message deleteMessage(int message_id) {
        Message message = messageDAO.getMessageByID(message_id);

        if (message != null) {
            messageDAO.deleteMessage(message_id);
            return message;
        } else {
            return null;
        }   
    }

    // Update message in a DB
    public Message updateMessage(Message message, int message_id) {
        int updateMessage_id = messageDAO.updateMessage(message, message_id);

        return messageDAO.getMessageByID(updateMessage_id);
    }
}
