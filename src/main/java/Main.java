import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.fillOperations.ChatFileOperations;
import models.chatClients.fillOperations.jsonChatFileOperations;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {

        ChatFileOperations chatFileOperations = new jsonChatFileOperations();

        ChatClient chatClient = new FileChatClient(chatFileOperations);

        MainFrame window = new MainFrame(800,600, chatClient);

       // test();

    }

    private static void test(){
        ChatClient client = new InMemoryChatClient();

        client.login("Langer");

        client.sendMessage("Message 01");
        client.sendMessage("Hello");

        client.logout();
    }
}
