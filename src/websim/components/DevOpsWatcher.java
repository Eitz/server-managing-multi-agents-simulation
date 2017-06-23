/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.components;

import websim.components.Server;
import java.math.BigDecimal;

/**
 *
 * @author eitz
 */
public class DevOpsWatcher {
    public String devOpsName;
    public BigDecimal minProcessorUsage;
    public BigDecimal maxProcessorUsage;
    
    int maxTicks;
    int spentTicks;

    public DevOpsWatcher(String devOpsName, BigDecimal minProcessorUsage, BigDecimal maxProcessorUsage, int maxTicks) {
        this.devOpsName = devOpsName;
        this.minProcessorUsage = minProcessorUsage;
        this.maxProcessorUsage = maxProcessorUsage;
        this.maxTicks = maxTicks;
    }
    
    public String getAlert(Server server) {
        
        String alert = null;
        
        if (server.getProcessorUsage().compareTo(minProcessorUsage) < 0 && server.isDegradable()) {
            alert = "min";
        }
        
        if (server.getProcessorUsage().compareTo(maxProcessorUsage) > 0) {
            alert = "max";
        }
        
        if (alert != null && tick() > maxTicks) {
            resetTicks();
            return alert;
        }
        else if (alert == null)
            resetTicks();
        
        return null;
    }
    
    int tick() {
        return ++spentTicks;
    }
    
    void resetTicks() {
        spentTicks = 0;
    }
    
    public String getStringRepresentation(boolean max) {
        
        String maximum;
        if (max) {
            maximum = String.valueOf(maxTicks);
        } else {
            maximum = String.valueOf(spentTicks);
        }        
        return "Max: " + maxProcessorUsage +
                ", Min: " + minProcessorUsage +
                ", " + maximum + "/" + maxTicks;
    }

    @Override
    public String toString() {
        return
            "{" +
                devOpsName +
                " - min:" + minProcessorUsage +
                ", max:" + maxProcessorUsage +
                ", maxTicks:" + maxTicks +
                ", curTick:" + spentTicks +
            "}";
    }    
    
}
