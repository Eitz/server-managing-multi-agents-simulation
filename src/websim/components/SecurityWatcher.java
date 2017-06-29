/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eitz
 */
public class SecurityWatcher {
    
    String securityAgentName;
    int maxSimultaneousTask;
        
    public SecurityWatcher(String securityAgentName, int maxSimultaneousTask) {
        this.securityAgentName = securityAgentName;
        this.maxSimultaneousTask = maxSimultaneousTask;
    }
    
    public List<String> getAlerts(Server computer) {
        
        List<String> alerts = new ArrayList<>();
                
        Map<String, Integer> result = computer.getUserTasksCount();
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            int userTasks = entry.getValue();
            String userName = entry.getKey();
            if (isAbuser(userName, userTasks))
                alerts.add(userName);
        }
        
        return alerts;
    }
    
    public boolean isAbuser(String user, int tasksQuantity) {
        return tasksQuantity > maxSimultaneousTask;
    }
    
    @Override
    public String toString() {
        return
            "{" +
                securityAgentName +
                ", maxSimultaneousTask:" + maxSimultaneousTask +
            "}";
    }    

    public String getName() {
        return securityAgentName;
    }

    public String getStringRepresentation(boolean b) {
        return "Max simultan. tasks: " + maxSimultaneousTask;
    }
    
}
