package websim;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Computer implements Server {

    public enum ComputerSpecs {
        LOW,
        MID,
        HIGH
    }
    
    BigDecimal processorUsage = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);
    float processorIncrementsMin;
    float processorIncrementsMax;
    Map<String, List<WebTask>> currentUsers = new HashMap<>();
    ComputerSpecs specs;
    
    public Computer(ComputerSpecs specs) {
        this.specs = specs;
        defineProcessorSpeed();
    }
    
    private void defineProcessorSpeed() {
        switch (specs) {
            case HIGH:
                processorIncrementsMin = 0.1f;
                processorIncrementsMax = 2f;
            case MID:
                processorIncrementsMin = 0.3f;
                processorIncrementsMax = 3f;
            case LOW:
                processorIncrementsMin = 0.5f;
                processorIncrementsMax = 5f;  
        }
    }

    private BigDecimal getRandomProcessorUsage() {
        Random rand = new Random();
        BigDecimal decimal = new BigDecimal((processorIncrementsMax - processorIncrementsMin) * rand.nextFloat() + processorIncrementsMin);
        decimal = decimal.setScale(2, RoundingMode.CEILING);
        return decimal;
    }
    
    @Override
    public void removeTask(WebTask task) {
        List userTasks = currentUsers.get(task.user);
        userTasks.remove(task);
        if (userTasks.isEmpty())
            currentUsers.remove(task.user);
        processorUsage = processorUsage.subtract(task.processorUsage);
    }
    
    @Override
    public boolean addTask(WebTask task) {
        task.setProcessorUsage(getRandomProcessorUsage());
                
        // Server overload
        if (processorUsage.intValue() == 100) {
            return false;
        } else {
            processorUsage = processorUsage.add(task.processorUsage);
            if (processorUsage.floatValue() >= 100) {
                task.processorUsage = task.processorUsage.subtract(processorUsage.subtract(new BigDecimal(100)));
                processorUsage = new BigDecimal(100);
            }
            if (!currentUsers.containsKey(task.user))
                currentUsers.put(task.user, new ArrayList<>());
            currentUsers.get(task.user).add(task);
            return true;
        }
    }
    
    @Override
    public List<WebTask> getCompletedTasks() {
        long currentTime = new Date().getTime();
        List<WebTask> done = new ArrayList<>();
        int taskQtd = 0;
        for (Map.Entry<String, List<WebTask>> user : currentUsers.entrySet()) {
            List<WebTask> userTasks = user.getValue();
            for (WebTask task : userTasks){
                taskQtd++;
                if (currentTime >= task.finishTime) {
                    done.add(task);
                }
            }
        }
        return done;
    }

    @Override
    public BigDecimal getProcessorUsage() {
        return processorUsage;
    }

    @Override
    public int getCurrentUsersCount() {
        return currentUsers.size();
    }

    @Override
    public int getTasksCount() {
        int taskQtd = 0;
        for (Map.Entry<String, List<WebTask>> user : currentUsers.entrySet()) {
            List<WebTask> userTasks = user.getValue();
            for (WebTask task : userTasks)
                taskQtd++;
        }
        return taskQtd;
    }

    @Override
    public boolean hasTask(WebTask task) {
        if (currentUsers.containsKey(task.user)){
            for (Map.Entry<String, List<WebTask>> user : currentUsers.entrySet()) {
                List<WebTask> userTasks = user.getValue();
                for (WebTask t : userTasks){
                    if (task.equals(t))
                        return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean upgrade() {
        switch (specs) {
            case LOW:
                specs = ComputerSpecs.MID;
                break;            
            case MID:
                specs = ComputerSpecs.HIGH;
                break;
            case HIGH:
                return false;
        }
        defineProcessorSpeed();
        return true;
    }
    
    @Override
    public boolean degrade() {
        switch (specs) {
            case HIGH:
                specs = ComputerSpecs.MID;
                break;
            case MID:
                specs = ComputerSpecs.LOW;
                break;
            case LOW:
                return false;
         
        }
        defineProcessorSpeed();
        return true;
    }
        
    @Override
    public boolean isCluster() {
        return false;
    }

    @Override
    public String toString() {
        return "[" + specs.name() + "]";
    }    
    
    @Override
    public boolean isDegradable() {
        return specs != ComputerSpecs.LOW;
    }
    
}

