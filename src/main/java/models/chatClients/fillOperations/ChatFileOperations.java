package models.chatClients.fillOperations;

import models.Message;

import java.util.List;

public interface ChatFileOperations {

    void writeMessages(List<Message> message);

    List <Message> readMessage();
}
