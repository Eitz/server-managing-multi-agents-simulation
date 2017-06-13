/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author eitz
 */
public abstract class UIComponent extends Graphics2D {
    
    public Image image;
    public int x, y;
    
    public UIComponent(String path, int x, int y) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(UIComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.x = x;
        this.y = y;
    }
}
