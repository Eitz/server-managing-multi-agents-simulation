/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author eitz
 */
public class UsersPanel extends BlockPanel {
    
    List<UserPanel> usersPanels = new ArrayList<>();
    JLabel countUsers;
    
    public UsersPanel() {
        super("Users");
        titleLabel.setFont(new Font("sans", Font.BOLD, 13));
        prepareCount();
    }
    
    final void prepareCount() {
        countUsers = new JLabel("0");
        countUsers.setFont(new Font("sans", Font.BOLD, 13));
        countUsers.setBounds(25, 40, 50, 20);
        add(countUsers);
    }

    public synchronized UserPanel addUser(String site, String userName) {
        UserPanel userPanel = new UserPanel(site, userName);
        while(updatingPositions) {}
        usersPanels.add(userPanel);
        add(userPanel);
        updatePositions();
        int newCount = Integer.parseInt(countUsers.getText()) + 1;
        countUsers.setText(String.valueOf(newCount));
        return userPanel;
    }
    
    public UserPanel getUserPanel(String userName) {
        for (UserPanel up : usersPanels) {
            if (up.userName.equals(userName))
                return up;
        }
        return null;
    }
    
    boolean updatingPositions = false;
    
    public void updatePositions() {
        int i = 0;
        int j = 0;
        updatingPositions = true;
        for (UserPanel up : usersPanels) {
            if (j!=3) {
                up.setBounds(
                    12 + (j == 0 ? up.getWidth()-1 : 0) + i * up.getWidth() + i * -1,
                    14+j * up.getHeight() + j * -1,
                    up.getWidth(),
                    up.getHeight());
                i++;
                if (i==11 && j == 0 || i==12) {
                    i=0;
                    j++;
                }
                up.setVisible(true);
            } else {
                up.setVisible(false);
            }
        }
        updatingPositions = false;
    }
    
    public synchronized void removeUser(UserPanel userPanel) {
        remove(userPanel);
        while(updatingPositions) {}
        usersPanels.remove(userPanel);
        updatePositions();
        int newCount = Integer.parseInt(countUsers.getText()) - 1;
        countUsers.setText(String.valueOf(newCount));
    }

    public UserPanel addMaliciousUser(String site, String userName) {
        UserPanel userPanel = new UserPanel(site, "MalUser");
        userPanel.titleLabel.setForeground(Color.red);
        userPanel.setBorder(BorderFactory.createLineBorder(Color.red, 1));
        while(updatingPositions) {}
        usersPanels.add(0, userPanel);
        add(userPanel);
        updatePositions();
        int newCount = Integer.parseInt(countUsers.getText()) + 1;
        countUsers.setText(String.valueOf(newCount));
        return userPanel; 
   }
    
}
