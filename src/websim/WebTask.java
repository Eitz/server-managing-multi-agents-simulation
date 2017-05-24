/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author eitz
 */
public class WebTask {
    public String user;
    public Long finishTime;
    public BigDecimal processorUsage;
    public String id;
    int taskMinimumMs = 200;
    int taskMaximumMs = 5000;
    
    public WebTask(String user, String TaskId) {
        this.user = user;
        this.finishTime = new Date().getTime() + getRandomTaskMs();
        this.processorUsage = new BigDecimal(0);
        this.id = TaskId;
    }

    public WebTask(String user, Long finishTime, BigDecimal processorUsage, String TaskId) {
        this.user = user;
        this.finishTime = finishTime;
        this.processorUsage = processorUsage;
        this.id = TaskId;
    }
    
    private int getRandomTaskMs() {
        Random rand = new Random();
        return taskMinimumMs + rand.nextInt((taskMaximumMs - taskMinimumMs) + 1);
    }

    void setProcessorUsage(BigDecimal processorUsage) {
        this.processorUsage = processorUsage;
    }
    
}
