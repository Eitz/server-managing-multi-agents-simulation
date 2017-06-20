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
public final class StatsPanel extends BlockPanel {
    
    int count = 0;
    
    JLabel upperText;
    JLabel bottomText;
    JLabel countText;
    
    public StatsPanel() {
        super("Stats");
        setLayout(null);
        
        prepareCount();
        prepareStrings();
        setTasksCount(count);
                
        add(upperText);
        add(countText);
        add(bottomText);
        
        repaint();
    }
    
    void prepareCount() {
        countText = new JLabel(String.valueOf(this.count));
        countText.setFont(new Font("sans", Font.BOLD, 40));
        countText.setBounds(
                this.getWidth() / 2 - countText.getWidth() / 2 - 20,
                60, 600, 50);
    }
    
    
    void prepareStrings() {
        upperText = new JLabel("Right now");
        upperText.setBounds(this.getWidth() / 2 - 25, 40, 100, 20);
        bottomText = new JLabel("Active tasks running on site");
        bottomText.setBounds(this.getWidth() / 2 - 80, 105, 200, 20);
    }
    
    final public void setTasksCount(int c) {
        this.count = c;
        countText.setText(String.valueOf(this.count));
        countText.repaint();
    }
    
}
