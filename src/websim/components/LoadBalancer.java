/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.components;

import websim.components.Server;
import websim.components.WebTask;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eitz
 */
public class LoadBalancer implements Server {
    
    List<Computer> computers = new ArrayList<>();
    
    public LoadBalancer(Computer[] computers) {
        for (Computer c : computers)
            this.computers.add(c);
    }
    
    public LoadBalancer(Computer computer) {
        this(new Computer[] { computer });
    }

    public LoadBalancer(int i) {
        for (int x=0; x<i; x++)
            this.computers.add(new Computer(Computer.ComputerSpecs.i7));
    }

    @Override
    public boolean isCluster() {
        return true;
    }
    
    @Override
    public void removeTask(WebTask task) {
        for (Computer c : computers) {
            if (c.hasTask(task)) {
                c.removeTask(task);
                return;
            }
        }
    }
    
    @Override
    public boolean hasTask(WebTask task) {
        for (Computer c : computers) {
            if (c.hasTask(task)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addTask(WebTask task) {
        Computer minimumUsedComputer = computers.get(0);
        for (Computer c : computers) {
            if (c.getProcessorUsage().compareTo(minimumUsedComputer.getProcessorUsage()) < 0) {
                minimumUsedComputer = c;
            }
        }
        return minimumUsedComputer.addTask(task);
    }

    @Override
    public List<WebTask> getCompletedTasks() {
        List<WebTask> completedTasks = new ArrayList<>();
        for (Computer c : computers)
            completedTasks.addAll(c.getCompletedTasks());
        return completedTasks;
    }

    @Override
    public BigDecimal getProcessorUsage() {
        BigDecimal processorUsage = new BigDecimal(0);
        for (Computer c : computers) 
            processorUsage = processorUsage.add(c.getProcessorUsage());
        return processorUsage.divide(new BigDecimal(computers.size()), RoundingMode.HALF_EVEN);
    }

    @Override
    public int getCurrentUsersCount() {
        List<String> currentUsersNames = new ArrayList<>();
        for (Computer c : computers) {
            for (Map.Entry<String, List<WebTask>> entry : c.currentUsers.entrySet()) {
                if (!currentUsersNames.contains(entry.getKey())){
                    currentUsersNames.add(entry.getKey());
                }
            }
        }
        return currentUsersNames.size();
    }

    @Override
    public int getTasksCount() {
        int currentTasksCount = 0;
        for (Computer c : computers)
            currentTasksCount += c.getTasksCount();
        return currentTasksCount;
    }

    @Override
    public boolean upgrade() {
        Computer upgradeComputer = new Computer(Computer.ComputerSpecs.i7);
        computers.add(upgradeComputer);
        return true;
    }
    
    @Override
    public boolean degrade() {
        computers.remove(computers.size()-1);
        return computers.size() > 0;
    }

    @Override
    public String toString() {
        String output = "{ ";
        for (Computer c : computers) {
            output += c.toString() + ", ";
        }
        output += " }";
        output = output.replace(", }", " }");
        return output;
    }

    @Override
    public boolean isDegradable() {
        return true;
    }

    @Override
    public Map<String, Integer> getUserTasksCount() {
        Map<String, Integer> userTasksCount = new HashMap<>();
        for (Computer computer : computers) {
            Map<String, Integer> innerTasksCount = computer.getUserTasksCount();
            for (Map.Entry<String, Integer> entry : innerTasksCount.entrySet()) {
                int userTasks = entry.getValue();
                String userName = entry.getKey();
                if (userTasksCount.get(userName) == null)
                    userTasksCount.put(userName, userTasks);
                else
                    userTasksCount.put(userName, userTasks + userTasksCount.get(userName));
            }
        }
        return userTasksCount;
    }
    
    @Override
    public ServerInformation[] getServerInformation() {
        List<ServerInformation> infos = new ArrayList<>();
        for (Computer c : computers) 
            infos.add(new ServerInformation(c.specs.name(), c.processorUsage));
        ServerInformation[] serverArr = new ServerInformation[infos.size()];
        serverArr = infos.toArray(serverArr);
        return serverArr;
    }
}
