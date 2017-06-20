/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.*;

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
    
    public TitlePanel(String site) {
        this.site = site;
        setLayout(null);
        setBackground(Color.LIGHT_GRAY);
        prepareTitle();
        prepareSubtitle();
        prepareStatus();        
        setOnlineStatus(true);
        
        add(subtitle);
        add(statusView);
        add(title);
        
        repaint();
    }
    
    void prepareTitle() {
        title = new JLabel(this.site);
        title.setFont(new Font("sans", Font.BOLD, 30));
        title.setBounds(10, 10, 600, 50);
    }
    
    
    void prepareSubtitle() {
        subtitle = new JLabel("Website");
        subtitle.setBounds(10, 4, 100, 20);
    }
    
    void prepareStatus() {
        statusView = new JLabel();
        statusView.setBounds(80, 4, 100, 20);
    }
    
    void setOnlineStatus(boolean s) {
        onlineStatus = s;
        if (onlineStatus) {
            statusView.setText("online");
            statusView.setForeground(Color.green);
        } else {
            statusView.setForeground(Color.red);
            statusView.setText("offline");
        }
        statusView.repaint();
    }
}
