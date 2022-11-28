import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.api.ApiChatClient;
import models.chatClients.fillOperations.ChatFileOperations;
import models.chatClients.fillOperations.jsonChatFileOperations;
import models.database.DBImitializer;
import models.gui.MainFrame;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";

        String databaseUrl = "jdbc:derby:ChatClientDb_skB";

        DBImitializer dbImitializer = new DBImitializer(databaseDriver, databaseUrl);
        dbImitializer.init();

        ChatFileOperations chatFileOperations = new jsonChatFileOperations();

        // ukladaní do souboru
        //ChatClient chatClient = new FileChatClient(chatFileOperations);

        // připojení na server
        ChatClient chatClient = new ApiChatClient();



        // nemá vlic na aplikaci
        Class<ApiChatClient> reflExample = ApiChatClient.class;
        List<Field> fields = getAllField(reflExample);

        for (Field f:fields
             ) {
            System.out.println(f.getName());
        }




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

    private static List<Field> getAllField(Class<?> cls){
        List<Field> fields = new ArrayList<>();

        for (Field f : cls.getDeclaredFields()){
            fields.add(f);
        }
        return fields;
    }
}
