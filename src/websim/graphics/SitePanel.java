/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import websim.agents.SiteAgent;

/**
 *
 * @author eitz
 */
public class SitePanel extends JPanel {
    
    SiteAgent agent;
    TitlePanel title;
    
    public String site;
    
    public StatsPanel stats;
    public ConsolePanel console;
    public AgentsPanel agents;
    public UsersPanel users;
    public LogPanel log;
    
    public SitePanel(SiteAgent myAgent) {
        this.agent = myAgent;
        this.site = agent.getLocalName();
        setSize(new Dimension(600, 710));
        setLayout(null);
        setBackground(Color.DARK_GRAY);
        prepareTitle();
        prepareBlocks();        
    }

    private void prepareTitle() {
        title = new TitlePanel(this.site);
        title.setBounds(0, 0, getWidth(), 49);
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
        
        console = new ConsolePanel();
        console.setBounds(300, 0, 300, 300);
        wrapper.add(console);
        
        log = new LogPanel();
        log.setBounds(0, 150, 300, 300);
        wrapper.add(log);
        
        agents = new AgentsPanel(this.agent.getLocalName());
        agents.setBounds(300, 300, 300, 150);
        wrapper.add(agents);
        
        users = new UsersPanel();
        users.setBounds(0, 450, 600, 210);
        wrapper.add(users);
        add(wrapper);
    }
}
