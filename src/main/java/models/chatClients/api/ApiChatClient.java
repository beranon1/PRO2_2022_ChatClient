package models.chatClients.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.LocalDateTimaDeserializer;
import models.LocalDateTimaSerializer;
import models.Message;
import models.chatClients.ChatClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.print.attribute.standard.RequestingUserName;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ApiChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private List<ActionListener> listenerLoggedUssersChanged = new ArrayList<>();

    private List<ActionListener> listenerLoggedMessageChanged = new ArrayList<>();

    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";

    private String token;

    private Gson gson;


    public ApiChatClient(){
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimaSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimaDeserializer())
                .create();

        // druhé vlákno na refresh prihlasenych
        Runnable refreshData = ()->{
            Thread.currentThread().setName("RefreshData");
            try{
                while(true){
                    if (isAuthenticated()){
                        //refresh uživatelů
                        refreshLoggedUsers();
                        //refresh zpráv
                        refreshMessages();
                    }
                    // uspání == proběhne až za 2 vteřiny
                    TimeUnit.SECONDS.sleep(2);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        Thread refreshDataThread = new Thread(refreshData);
        refreshDataThread.start();
    }

    @Override
    public void sendMessage(String text) {

        try{
            SendMessageRequest msgRequest = new SendMessageRequest(token, text);

            String url = BASE_URL + "/api/Chat/SendMessage";
            HttpPost post = new HttpPost(url);

            String jsonBody = gson.toJson(msgRequest);

            StringEntity body = new StringEntity(
                    jsonBody,
                    "utf-8"
            );
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if (response.getStatusLine().getStatusCode() == 204){
                refreshMessages();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void login(String userName) {
        try{
            // dokumentace == http://fimuhkpro22021.aspifyhost.cz/swagger/index.html
            // Zjistíme v dokumentaci
            String url = BASE_URL + "/api/Chat/Login";
            // 4 základní požadavky get, post, delate, ....
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(
                    "\"" + userName + "\"",
                    "utf-8"
            );
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            // kody začínajíci 2 (200) jsou OK, 4 (400) něco chybí v dokmentaci
            if (response.getStatusLine().getStatusCode() == 200){
                token = EntityUtils.toString(response.getEntity());
                // odstranění uvozovek z tokenu
                token = token.replace("\"", "").trim();

                loggedUser = userName;
                System.out.println("user logged in " + userName);
                refreshLoggedUsers();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void logout() {
        try{
            // Zjistíme v dokumentaci
            String url = BASE_URL + "/api/Chat/Logout";
            // 4 základní požadavky get, post, delate, ....
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(
                    "\"" + token + "\"",
                    "utf-8"
            );
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            // kody začínajíci 2 (200) jsou OK, 4 (400) něco chybí viz. dokmentace
            if (response.getStatusLine().getStatusCode() == 204){
                token = null;
                loggedUser = null;
                loggedUsers.clear();
                System.out.println("user logged out");
                raiseEventLoggedUsersChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean isAuthenticated() {
        System.out.println("is authenticated: " + loggedUser != null);
        return loggedUser != null;
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionLisenerLogguedUsers(ActionListener toAdd) {
        listenerLoggedUssersChanged.add(toAdd);
    }

    @Override
    public void addActionLisenerMessageChanged(ActionListener toAdd) {
        listenerLoggedMessageChanged.add(toAdd);
    }

    private void raiseEventLoggedUsersChanged(){
        for (ActionListener al:
                listenerLoggedUssersChanged) {
            al.actionPerformed(
                    new ActionEvent(
                            this,
                            1,
                            "usersChanged"
                    )
            );
        }
    }

    private void raiseEventLoggedMessagedChanged(){
        for (ActionListener al:
                listenerLoggedMessageChanged) {
            al.actionPerformed(
                    new ActionEvent(
                            this,
                            1,
                            "messagesChanged"
                    )
            );
        }
    }

    private void addSystemMessage(int type, String author) {
        messages.add(new Message(type, author));
        raiseEventLoggedMessagedChanged();
    }

    private void refreshLoggedUsers(){
        try {
            String url = BASE_URL + "/api/Chat/GetLoggedUsers";
            HttpGet get = new HttpGet(url);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200){
                String jsonBody = EntityUtils.toString(response.getEntity());
                loggedUsers = gson.fromJson(
                        jsonBody,
                        new TypeToken<ArrayList<String>>(){}.getType()
                );
                raiseEventLoggedUsersChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshMessages(){
        try {
            String url = BASE_URL + "/api/Chat/GetMessages";
            HttpGet get = new HttpGet(url);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == 200){
                String jsonBody = EntityUtils.toString(response.getEntity());
                messages = gson.fromJson(
                        jsonBody,
                        new TypeToken<ArrayList<Message>>(){}.getType()
                );
                raiseEventLoggedMessagedChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

