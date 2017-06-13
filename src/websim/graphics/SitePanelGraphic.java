/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import websim.agents.SiteAgent;

/**
 *
 * @author eitz
 */
public class SitePanelGraphic extends JPanel {
    
    SiteAgent agent;
    TitlePanel title;
    
    BlockPanel stats;
    BlockPanel console;
    BlockPanel machines;
    BlockPanel agents;
    BlockPanel applications;
    BlockPanel users;
    BlockPanel log;
    
    public SitePanelGraphic(SiteAgent myAgent) {
        this.agent = myAgent;
        setSize(new Dimension(600, 700));
        setLayout(null);
        setBackground(Color.blue);
        prepareTitle();
        prepareBlocks();        
        
    }

    private void prepareTitle() {
        title = new TitlePanel(agent.getLocalName());
        title.setBounds(0, 0, getWidth(), 55);
        add(title);
    }

    private void prepareBlocks() {
        
        JPanel wrapper = new JPanel();
        wrapper.setLayout(null);
        wrapper.setBounds(0, title.getHeight(), getWidth(), getHeight() - title.getHeight());
        // wrapper.setBackground(Color.blue);
        
        stats = new StatsPanel();
        stats.setBounds(0, 0, 300, 150);
        wrapper.add(stats);
        
        console = new StatsPanel();
        console.setBounds(300, 0, 300, 150);
        wrapper.add(console);
        
        
        machines = new StatsPanel();
        machines.setBounds(0, 150, 300, 150);
        wrapper.add(machines);
        
        agents = new StatsPanel();
        agents.setBounds(300, 150, 300, 150);
        wrapper.add(agents);
        
        applications = new StatsPanel();
        applications.setBounds(0, 300, 300, 150);
        wrapper.add(applications);
                
        log = new StatsPanel();
        log.setBounds(300, 300, 300, 150);
        wrapper.add(log);
        
        users = new StatsPanel();
        users.setBounds(0, 450, 600, 150);
        wrapper.add(users);
        
        add(wrapper);
    }
}
