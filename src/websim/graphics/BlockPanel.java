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
import javax.swing.border.Border;

/**
 *
 * @author eitz
 */
public class BlockPanel extends JPanel {
    
    String title;
    int blockHeight;
    
    JLabel titleLabel;
    
    public BlockPanel(String title) {
        this.title = title;
        
        prepare();
        prepareTitle();
        
        repaint();        
    }  
    
    final void prepare() {
        setLayout(null);
        setSize(new Dimension(290, 150));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 8, true));
        setBackground(Color.white);
    }
    
    final void prepareTitle() {
        titleLabel = new JLabel(" " + this.title);
        titleLabel.setFont(new Font("sans", Font.BOLD, 20));
        titleLabel.setForeground(Color.darkGray);
        titleLabel.setBounds(10, 10, 280, 40);
        add(titleLabel);
    }
}
