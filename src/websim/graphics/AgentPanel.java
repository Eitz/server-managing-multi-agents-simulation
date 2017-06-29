/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author eitz
 */
public class AgentPanel extends JPanel {
    
    JLabel titleLabel;
    JLabel firstSubtitleLabel;
    JLabel secondSubtitleLabel;
    JLabel statusLabel;
    JLabel configLabel;
    
    String site;
    String title;
    
    public AgentPanel(String site, String title) {
        
        this.site = site;
        this.title = title;
        
        prepare();
        prepareTitle();
        prepareSubtitles();
        prepareStatus();
        prepareConfiguration();
        
        repaint();        
    }  
    
    public void setActive() {
        titleLabel.setForeground(Color.WHITE);
        configLabel.setForeground(Color.WHITE);
        setBackground(Color.DARK_GRAY);
    }
    
    public void setInactive() {
        titleLabel.setForeground(Color.BLUE);
        configLabel.setForeground(Color.BLACK);
        setBackground(Color.WHITE);
    }
    
    final void prepare() {
        setLayout(null);
        setSize(new Dimension(125, 95));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setBackground(Color.white);
    }
    
    public void setStatus(String status) {
        setStatus(status, Color.black);
    }
    
    public void setStatus(String status, Color color) {
        statusLabel.setText(status);
        statusLabel.setForeground(color);
    }
    
    public void setConfiguration(String conf) {
        configLabel.setText(conf);
    }
    
    final void prepareStatus() {
        statusLabel = new JLabel("Not connected");
        statusLabel.setFont(new Font("sans", Font.PLAIN, 10));
        statusLabel.setBounds(
            10,
            37,
            getSize().width,
            statusLabel.getPreferredSize().height+5);
        add(statusLabel);
    }
    
    final void prepareConfiguration() {
        configLabel = new JLabel("None");
        configLabel.setFont(new Font("sans", Font.PLAIN, 8));
        configLabel.setBounds(
            10,
            68,
            getSize().width,
            configLabel.getPreferredSize().height+5);
        add(configLabel);
    }
    
    final void prepareTitle() {
        titleLabel = new JLabel(this.title);
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setFont(new Font("sans", Font.BOLD, 11));
        titleLabel.setBounds(
            this.getWidth() / 2 - titleLabel.getPreferredSize().width / 2,
            5,
            titleLabel.getPreferredSize().width,
            titleLabel.getPreferredSize().height+10);
        add(titleLabel);
    }
    
    final void prepareSubtitles() {
        firstSubtitleLabel = new JLabel("Status");
        firstSubtitleLabel.setFont(new Font("sans", Font.PLAIN, 10));
        firstSubtitleLabel.setBounds(
            10,
            25,
            firstSubtitleLabel.getPreferredSize().width,
            firstSubtitleLabel.getPreferredSize().height+5);
        firstSubtitleLabel.setForeground(Color.GRAY);
        add(firstSubtitleLabel);
        
        secondSubtitleLabel = new JLabel("Configuration");
        secondSubtitleLabel.setFont(new Font("sans", Font.PLAIN, 10));
        secondSubtitleLabel.setBounds(
            10,
            55,
            secondSubtitleLabel.getPreferredSize().width,
            secondSubtitleLabel.getPreferredSize().height+5);
        secondSubtitleLabel.setForeground(Color.GRAY);
        add(secondSubtitleLabel);
    }
}
