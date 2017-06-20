/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import sun.awt.X11.XConstants;
import websim.components.Server;
import websim.components.ServerInformation;

/**
 *
 * @author eitz
 */
public class ConsolePanel extends BlockPanel {
    
    List<JLabel> computers = new ArrayList<>();
    List<JLabel> processorUsages = new ArrayList<>();
    
    public ConsolePanel() {
        super("Computers [cpu usage]");
        prepareConsoleTitle();
        prepareConsole();
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10));
    }
    
    final void prepareConsoleTitle() {
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.DARK_GRAY);
        titleLabel.setBackground(Color.GRAY);
    }
    
    final void prepareConsole() {
        setBackground(Color.black);
        setForeground(Color.white);
    }
    
    public void setComputerInformation(ServerInformation[] serverInformation) {
        for (JLabel c : computers)
            remove(c);
        for (JLabel pu : processorUsages)
            remove(pu);
        
        int i = 0;
        for (ServerInformation serverInfo : serverInformation) {
            i++;            
            JLabel computer = new JLabel(serverInfo.name);
            computer.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            computer.setForeground(Color.cyan);
            computer.setBounds(20, 50 + 20 * i, 30, 20);
            add(computer);
            computers.add(computer);
            
            JLabel processorUsage = new JLabel(prepareCPUText(serverInfo.processorUsage));
            processorUsage.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            processorUsage.setForeground(Color.white);
            processorUsage.setBounds(50, 50 + 20 * i, 280, 20);
            add(processorUsage);
            processorUsages.add(processorUsage);    
        }
        validate();
        repaint();
    }

    
    String prepareCPUText(BigDecimal count) {
        int bars = count.intValue();
        bars = bars / 4;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < bars; i++) {
            sb.append("|");
        }
        if (bars == 25) {
            sb.deleteCharAt(sb.lastIndexOf("|"));
        }
        int padding = 25 - bars;
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        
        sb.append(count.toString());
        sb.append("%");
        sb.append("]");
        return sb.toString();
    }
    
    
}
