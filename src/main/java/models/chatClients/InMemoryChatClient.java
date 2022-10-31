package models.chatClients;

import models.Message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private List<ActionListener> listenerLoggedUssersChanged = new ArrayList<>();

    private List<ActionListener> listenerLoggedMessageChanged = new ArrayList<>();

    public InMemoryChatClient(){
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println(getMessages());
        raiseEventLoggedMessagedChanged();
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessage(Message.USER_LOGGED_IN, userName);
        System.out.println("user logged in " + userName);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        System.out.println("user logged out");
        raiseEventLoggedUsersChanged();
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

    private void addSystemMessage(int type, String author){
        messages.add(new Message(type, author));
        raiseEventLoggedMessagedChanged();
    }
}
