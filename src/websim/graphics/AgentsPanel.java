/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author eitz
 */
public final class AgentsPanel extends BlockPanel {
    
    String site;
    
    AgentPanel devOpsPanel;
    AgentPanel secPanel;
    
    public AgentsPanel(String site) {
        super(" Agents");
        this.site = site;
        setLayout(null);
        
        prepareDevOpsPanel();
        prepareSecPanel();
                
        add(devOpsPanel);
        add(secPanel);
        
        repaint();
    }
    
    public AgentPanel getDevOpsPanel() {
        return devOpsPanel;
    }
    
    public AgentPanel getSecPanel() {
        return secPanel;
    }
    
    void prepareDevOpsPanel() {
        devOpsPanel = new AgentPanel(site, "DevOpsAgent");
        devOpsPanel.setBounds(20, 40, devOpsPanel.getWidth(), devOpsPanel.getHeight());
    }
    
    void prepareSecPanel() {
        secPanel = new AgentPanel(site, "SecurityAgent");
        secPanel.setBounds(155, 40, secPanel.getWidth(), secPanel.getHeight());
    }
    
}
