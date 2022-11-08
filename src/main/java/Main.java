import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.fillOperations.ChatFileOperations;
import models.chatClients.fillOperations.jsonChatFileOperations;
import models.database.DBImitializer;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {

        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";

        String databaseUrl = "jdbc:derby:ChatClientDb_skB";

        DBImitializer dbImitializer = new DBImitializer(databaseDriver, databaseUrl);
        dbImitializer.init();

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
