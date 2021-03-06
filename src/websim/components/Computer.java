package websim.components;

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
        i3,
        i5,
        i7
    }
    
    BigDecimal processorUsage = new BigDecimal(0).setScale(2, RoundingMode.HALF_EVEN);
    float processorIncrementsMin;
    float processorIncrementsMax;
    volatile Map<String, List<WebTask>> currentUsers = new HashMap<>();
    ComputerSpecs specs;
    
    public Computer(ComputerSpecs specs) {
        this.specs = specs;
        defineProcessorSpeed();
    }
    
    @Override
    public Map<String, Integer> getUserTasksCount() {
        
        Map<String, Integer> usersTasksCount = new HashMap<>();
        
        for (Map.Entry<String, List<WebTask>> user : currentUsers.entrySet()) {
            List<WebTask> userTasks = user.getValue();
            usersTasksCount.put(user.getKey(), userTasks.size());
        }
        
        return usersTasksCount;
    }
    
    private void defineProcessorSpeed() {
        switch (specs) {
            case i7:
                processorIncrementsMin = 0.5f;
                processorIncrementsMax = 3f;
            case i5:
                processorIncrementsMin = 1f;
                processorIncrementsMax = 6f;
            case i3:
                processorIncrementsMin = 1.5f;
                processorIncrementsMax = 10f;  
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
    public synchronized boolean addTask(WebTask task) {
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
            case i3:
                specs = ComputerSpecs.i5;
                break;            
            case i5:
                specs = ComputerSpecs.i7;
                break;
            case i7:
                return false;
        }
        defineProcessorSpeed();
        return true;
    }
    
    @Override
    public boolean degrade() {
        switch (specs) {
            case i7:
                specs = ComputerSpecs.i5;
                break;
            case i5:
                specs = ComputerSpecs.i3;
                break;
            case i3:
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
        return specs != ComputerSpecs.i3;
    }
    
    @Override
    public ServerInformation[] getServerInformation() {
        return new ServerInformation[] { new ServerInformation(this.specs.name(), this.processorUsage) };
    }
    
}

