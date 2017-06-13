/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import websim.AgentManager;
import websim.graphics.ContentPanel;
import websim.graphics.SitePanelGraphic;

/**
 *
 * @author eitz
 */

public final class UIManager extends JFrame {
    
    private static UIManager instance;
    private static AgentManager agentManager;
    
    private static final List<SitePanelGraphic> sitePanels = new ArrayList<SitePanelGraphic>();
    
    synchronized public static UIManager getInstance() {
        if (instance == null) instance = new UIManager();
        return instance;
    }
    
    private static ContentPanel contentPanel;

    public static ContentPanel getContentPanel() {
        return contentPanel;
    }

    public void setAgentManager(AgentManager am) {
        agentManager = am;
    }

    void prepareGUI() {
        Dimension size = new Dimension(1200,700);
        setSize(size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);        
        toFront();
        
        // add elements
        contentPanel = prepareContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        // add(prepareButtonsPanel(), BorderLayout.NORTH);
        
        repaint();
        
        SwingUtilities.invokeLater(() -> { 
            setVisible(true);
        });        
    }
    
    private UIManager() {
        prepareGUI();
    }
    
    
    ContentPanel prepareContentPanel() {
        return new ContentPanel();
    }
    
    JPanel prepareButtonsPanel() {
        JPanel buttonsBar = new JPanel();
        
        buttonsBar.setBackground(Color.darkGray);
		
        JButton btnTest = new JButton("[TEST] Add Random User");
        buttonsBar.add(btnTest);
        btnTest.addActionListener((ActionEvent e) -> {
            agentManager.addUser();
        });
        
        return buttonsBar;
    }
    
    public void addSite(SitePanelGraphic sitePanel) {
        System.out.println("OK, TRYING TO ADD!");
        sitePanels.add(sitePanel);
        sitePanel.setBounds((sitePanels.size()-1) * 600, 0, 600, 700);
        getContentPanel().add(sitePanel);
        sitePanel.repaint();
    }
}
