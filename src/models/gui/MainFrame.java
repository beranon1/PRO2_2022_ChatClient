package models.gui;

import models.Message;
import models.chatClients.ChatClient;

import javax.swing.*;
import javax.swing.text.BoxView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private final ChatClient chatClient;
    // Atributy třídy dostupné pro všecjny metody
    JTextArea txtChat;
    JTextField txtInputMessage;

    public MainFrame(int width, int height, ChatClient chatClient){
        super("PRO2 2022 ChatClient");

        setSize(width,height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.chatClient = chatClient;
        initGui();
        setVisible(true);
    }


    // Jpanel == div (HTML)
    // Swing dokumentace!!
    // Tabulka určitě ve zkoušce!!
    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());

        panelMain.add(initLoginPanel(), BorderLayout.NORTH);

        panelMain.add(initChatPanel(),BorderLayout.CENTER);

        panelMain.add(initLoggedUsersPanel(), BorderLayout.EAST);

        panelMain.add(initMessagePanel(),BorderLayout.SOUTH);

        add(panelMain);
    }
    private JPanel initLoginPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Username"));
            // txtInputUsername  == přístupný pouze pro tuto metodu initLoginPanel
        JTextField txtInputUsername = new JTextField("", 38);
        panel.add(txtInputUsername);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtInputUsername.getText();
                System.out.println("btn ligin clik - " + username);

                if (chatClient.isAuthenticated()){
                    //LogOut
                    chatClient.logout();
                    btnLogin.setText("Login");
                    txtInputUsername.setEditable(true);
                    txtChat.setEnabled(false);
                    txtInputMessage.setEnabled(false);
                }else {
                    //LogIn
                    if (username.length() < 1){
                        JOptionPane.showMessageDialog(
                                null,
                                "Enter your username",
                                "Error",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    chatClient.login(username);
                    btnLogin.setText("Logout");
                    txtInputUsername.setEditable(false);
                    txtChat.setEnabled(true);
                    txtInputMessage.setEnabled(true);
                }

            }
        });

        panel.add(btnLogin);

        return  panel;
    }
    private JPanel initChatPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        txtChat = new JTextArea();
        txtChat.setEditable(false);
        txtChat.setEnabled(false);
        JScrollPane scrollPanel = new JScrollPane(txtChat);

        panel.add(scrollPanel);
/*      GENEROVANI ZPRAV
        for (int i = 0; i < 50; i++) {
            txtChat.append("Message " + i + "\n");
        }
*/
        chatClient.addActionLisenerMessageChanged(e -> {
            refreshMessages();
        });
        return panel;
    }
    private JPanel initLoggedUsersPanel(){
        JPanel panel = new JPanel();

        Object[][] data = new Object[][]{
                {"0,0", "0,1"},
                {"1,0", "1,1"},
                {"aaa", "bbb"}
        };

        String[] colNames = new String[]{"Col1", "Col2"};

       // JTable tblLoggedUsers = new JTable(data, colNames);

        JTable tblLoggedUsers = new JTable();
        LoggedUsersTableModel loggedUsersTableModel = new LoggedUsersTableModel(chatClient);
        tblLoggedUsers.setModel(loggedUsersTableModel);

        JScrollPane scrollPane = new JScrollPane(tblLoggedUsers);
        scrollPane.setPreferredSize(new Dimension(250,500));
        panel.add(scrollPane);

        chatClient.addActionLisenerLogguedUsers(e -> {
            loggedUsersTableModel.fireTableDataChanged();
        });

        return panel;
    }
    private JPanel initMessagePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtInputMessage = new JTextField("", 50);
        txtInputMessage.setEnabled(false);
        panel.add(txtInputMessage);

        JButton btnSendMessage = new JButton("Send");
        /*
        btnSendMessage.addActionListener(e -> {

            System.out.println("Button send klik - " + txtInputMessage.getText());

            txtChat.append("txtInputMessage.getText()");

            txtInputMessage.setText("");
        });
*/
        btnSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msgText = txtInputMessage.getText();
                System.out.println("Button send klik - " + msgText);

                //txtChat.append(txtInputMessage.getText() + "\n");

                if (msgText.length() == 0)
                    return;
                if (!chatClient.isAuthenticated())
                    return;
                chatClient.sendMessage(msgText);

                txtInputMessage.setText("");

                //refreshMessages();

            }
        });
        panel.add(btnSendMessage);

        return panel;
    }
    private void refreshMessages(){
        if (!chatClient.isAuthenticated())
            return;

        txtChat.setText("");
        for (Message msg:
             chatClient.getMessages()) {
            txtChat.append(msg.toString());
            txtChat.append("\n");
        }
    }

}
