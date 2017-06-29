/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.*;
import websim.AgentManager;
import websim.UIManager;

/**
 *
 * @author eitz
 */
public final class TitlePanel extends JPanel {
    
    String site;
    boolean onlineStatus;
    
    JLabel title;
    JLabel subtitle;
    JLabel statusView;
    JButton buttonAddUsers;
    JButton buttonAddMalUser;
    
    public TitlePanel(String site) {
        this.site = site;
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        prepareTitle();
        prepareSubtitle();
        prepareStatus();
        prepareButtons();
        setOnlineStatus(true);
        
        add(subtitle);
        add(statusView);
        add(title);
        add(buttonAddUsers);
        add(buttonAddMalUser);
        
        repaint();
    }
    
    void prepareTitle() {
        title = new JLabel(this.site);
        title.setFont(new Font("sans", Font.BOLD, 28));
        title.setBounds(10, 15, 600, title.getPreferredSize().height);
    }
    
    
    void prepareSubtitle() {
        subtitle = new JLabel("Website");
        subtitle.setBounds(10, 2, 100, 20);
    }
    
    void prepareStatus() {
        statusView = new JLabel();
        statusView.setFont(new Font("monospaced", Font.BOLD, 11));
        statusView.setBounds(80, 2, 100, 20);
    }
    
    void setOnlineStatus(boolean s) {
        onlineStatus = s;
        if (onlineStatus) {
            statusView.setText("online");
            statusView.setForeground(Color.BLACK);
        } else {
            statusView.setForeground(Color.red);
            statusView.setText("offline");
        }
        statusView.repaint();
    }
    
    void prepareButtons() {
        int leftBasePx = 465;
        buttonAddUsers = new JButton("Add 30 users");
        buttonAddUsers.setBounds(
            leftBasePx,
            15,
            buttonAddUsers.getPreferredSize().width,
            buttonAddUsers.getPreferredSize().height
        );
        buttonAddUsers.addActionListener((ActionEvent ae) -> {
            for (int i=0; i<30; i++)
                AgentManager.getInstance().addUser(site);            
        });
        
        buttonAddMalUser = new JButton("Add MaliciousUser");
        buttonAddMalUser.setBounds(
            leftBasePx - buttonAddMalUser.getPreferredSize().width - 10,
            15,
            buttonAddMalUser.getPreferredSize().width,
            buttonAddMalUser.getPreferredSize().height
        );
        buttonAddMalUser.addActionListener((ActionEvent ae) -> {
            AgentManager.getInstance().addMaliciousUser(site);          
        });
    }
}
