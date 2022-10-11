package models.gui;

import javax.swing.*;
import javax.swing.text.BoxView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    // Atributy třídy dostupné pro všecjny metody
    JTextArea txtChat;

    public MainFrame(int width, int height){
        super("PRO2 2022 ChatClient");

        setSize(width,height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initGui();
        setVisible(true);
    }


    // Jpanel == div (HTML)
    // Swing dokumentace!!
    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());

        panelMain.add(initLoginPanel(), BorderLayout.NORTH);

        panelMain.add(initChatPanel(),BorderLayout.CENTER);

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
                System.out.println("btn ligin clik - " + txtInputUsername.getText());
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
        JScrollPane scrollPanel = new JScrollPane(txtChat);

        panel.add(scrollPanel);
/*      GENEROVANI ZPRAV
        for (int i = 0; i < 50; i++) {
            txtChat.append("Message " + i + "\n");
        }
*/
        return panel;
    }
    private JPanel initMessagePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField txtInputMessage = new JTextField("", 50);
        panel.add(txtInputMessage);

        JButton btnSendMessage = new JButton("Send");
        btnSendMessage.addActionListener(e -> {

            System.out.println("Button send klik - " + txtInputMessage.getText());

            txtChat.append(txtInputMessage.getText() + "\n");
            txtInputMessage.setText("");
        });
        panel.add(btnSendMessage);

        return panel;
    }

}
