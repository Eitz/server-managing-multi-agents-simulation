/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author eitz
 */
public class UserPanel extends JPanel {
    
    JLabel titleLabel;
    List<JLabel> tasksLabels = new ArrayList<>();
    JLabel moreTasksLabel;
    
    String site;
    String userName;
    
    public UserPanel(String site, String userName) {
        
        this.site = site;
        this.userName = userName;
        
        prepare();
        prepareTitle();
        prepareMoreTasks();
        
        repaint();        
    }  
    
    final void prepare() {
        setLayout(null);
        setSize(new Dimension(48, 62));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setBackground(Color.white);
    }
    
    final void prepareMoreTasks() {
        moreTasksLabel = new JLabel("+0 tasks");
        moreTasksLabel.setFont(new Font("sans", Font.PLAIN, 8));
        moreTasksLabel.setBounds(
            this.getWidth() / 2 - moreTasksLabel.getPreferredSize().width / 2,
            45,
            moreTasksLabel.getPreferredSize().width,
            moreTasksLabel.getPreferredSize().height+6);
        moreTasksLabel.setVisible(false);
        add(moreTasksLabel);
    }
    
    final void prepareTitle() {
        titleLabel = new JLabel(this.userName);
        titleLabel.setFont(new Font("sans", Font.BOLD, 8));
        titleLabel.setBounds(
            this.getWidth() / 2 - titleLabel.getPreferredSize().width / 2,
            2,
            titleLabel.getPreferredSize().width,
            titleLabel.getPreferredSize().height+6);
        add(titleLabel);
    }
    
    public void addTask(String id, String text, Color color) {
        JLabel newTask = new JLabel(text);
        newTask.setFont(new Font("sans", Font.ITALIC, 7));
        newTask.setToolTipText(id);
        newTask.setForeground(color);
        tasksLabels.add(0, newTask);
        newTask.setBounds(
                0,
                0,
                newTask.getPreferredSize().width,
                newTask.getPreferredSize().height);
        add(newTask);
        updateTasks();
    }
    
    public void updateTask(String id, String text, Color color) {
        JLabel task = null;
        for (JLabel t : tasksLabels) {
            if (id.equals(t.getToolTipText())) {
                task = t;
                break;
            }
        }
        if (task != null) {
            task.setText(text);
            task.setForeground(color);
        }
    }
    
    void updateTasks() {
        for (int i = 0; i < Math.min(3, tasksLabels.size()); i++) {
            JLabel task = tasksLabels.get(i);
            task.setBounds(
                5,
                17 + i * task.getHeight(),
                task.getWidth(),
                task.getHeight());
        }
        if (tasksLabels.size() > 3) {
            for (int i = 3; i < tasksLabels.size(); i++) {
                JLabel task = tasksLabels.get(i);
                task.setVisible(false);
            }
            moreTasksLabel.setVisible(true);
            moreTasksLabel.setText(
                String.valueOf(tasksLabels.size() - 3) + "+ tasks"
            );
        }
    }
    
}
