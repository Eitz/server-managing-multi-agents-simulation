package websim.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import websim.components.DevOpsWatcher;
import websim.components.SecurityWatcher;
import websim.components.WebTask;
import websim.agents.SiteAgent;

public class PeriodicSiteTasksBehaviour extends TickerBehaviour {

        SiteAgent agent;
        Logger log = Logger.getLogger("websim");
        
        public PeriodicSiteTasksBehaviour(Agent a, int repeatMs) {
            super(a, repeatMs);
            agent = (SiteAgent) myAgent;
        }
        
        void checkForCompletedTasks() {
            List<WebTask> completedTasks = agent.computer.getCompletedTasks();
            for (WebTask task : completedTasks) {
                agent.answerTask(task, true);
                agent.computer.removeTask(task);
            }
        }
        
        void checkForSecurityAlerts() {
            for (SecurityWatcher watcher : agent.securityWatchers){
                List<String> maliciousUsers = watcher.getAlerts(agent.computer);
                for (String maliciousUser : maliciousUsers) {
                    agent.informSecurityAgent(watcher, maliciousUser);
                }
            }
        }
        
        void printConsoleInfo() {
            String agentName = agent.consoleName();
            String devOps = "";
            devOps = agent.devOpsWatchers.stream().map((dow) -> dow.toString() + ",").reduce(devOps, String::concat);
            log.log(
                Level.FINE,
                "[{0}]\n" +
                "- Computer(s): {1}\n" +
                "- Processor usage: {2}\n" + 
                "- Current users: {3}\n" + 
                "- Current tasks: {4}\n" + 
                "- Current devops watching: {5}",
                new Object[]{
                    agentName,
                    agent.computer.toString(),
                    agent.computer.getProcessorUsage(),
                    agent.computer.getCurrentUsersCount(),
                    agent.computer.getTasksCount(),
                    devOps
                }
            );
            
            
        }
        
        void checkForDevOpsAlerts() {
            for (DevOpsWatcher watcher : agent.devOpsWatchers){
                String alertMinMax = watcher.getAlert(agent.computer);
                if (alertMinMax != null) {
                    agent.informDevOps(watcher, alertMinMax);
                }
            }
        }

        @Override
        public void onTick() {
            printConsoleInfo();
            checkForDevOpsAlerts();
            checkForSecurityAlerts();
            checkForCompletedTasks();            
        }
    }