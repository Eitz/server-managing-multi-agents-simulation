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
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author eitz
 */
public class LogPanel extends BlockPanel {
    
    JTextPane textPane;
    StyledDocument doc;

    public LogPanel() {
        super("Log");
        prepareTextArea();
    }
    
    final void prepareTextArea() {
        textPane = new JTextPane();
        textPane.setFont(new Font("Sans", Font.PLAIN, 10));
        textPane.setEditable(false); // set textArea non-editable
        Dimension dmnsn = new Dimension(this.getWidth()-20, 245);
        textPane.setSize(dmnsn);
        DefaultCaret caret = (DefaultCaret)textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        doc = textPane.getStyledDocument();

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scroll.setSize(dmnsn);
        scroll.setBounds(
                15,
                40,
                scroll.getWidth(), scroll.getHeight());        
        add(scroll);
    }
    
    public void append(String who, String text) {
        append(who, text, Color.blue, Color.black);
    }
    
    public synchronized void append(String who, String text, Color whoColor, Color textColor) {
        Style style = textPane.addStyle("txt-style", null);
        StyleConstants.setBold(style, true);
        StyleConstants.setForeground(style, whoColor);

        try { doc.insertString(doc.getLength(), who + ": ",style); }
        catch (BadLocationException e){}
        
        StyleConstants.setBold(style, false);
        StyleConstants.setForeground(style, textColor);

        try { doc.insertString(doc.getLength(), text + "\n", style); }
        catch (BadLocationException e){}
    }
    
}
