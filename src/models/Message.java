package models;

import java.time.LocalDateTime;

public class Message {
    private String author;
    private String text;
    private LocalDateTime created;

    public static final int USER_LOGGED_IN = 1;
    public static final int USER_LOGGED_OUT = 2;

    private static final String AUTHOR_SYSEM = "System";

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        created = LocalDateTime.now();
    }

    public Message(int type, String username){
        this.author = AUTHOR_SYSEM;
        this.created = LocalDateTime.now();

        if (type == USER_LOGGED_IN){
            text = username + "has joined the chat";
        } else if (type == USER_LOGGED_OUT) {
            text = username + " odešel";
        }
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        if (author.equals(AUTHOR_SYSEM))
            return text + "\n";

        String s = author + "[" + created.toLocalDate() + "]\n";
        s += text + "\n";
        return s;
    }
}
