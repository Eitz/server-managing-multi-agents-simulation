package websim.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.util.List;
import websim.DevOpsWatcher;
import websim.WebTask;
import websim.agents.SiteAgent;

public class PeriodicSiteTasksBehaviour extends TickerBehaviour {

        SiteAgent agent;
        
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
        
        void printConsoleInfo() {
            System.out.println("["+agent.consoleName()+"] Computer(s): " + agent.computer.toString());
            System.out.println("["+agent.consoleName()+"] Processor usage: " + agent.computer.getProcessorUsage());
            System.out.println("["+agent.consoleName()+"] Current users: " + agent.computer.getCurrentUsersCount());
            System.out.println("["+agent.consoleName()+"] Current tasks: " + agent.computer.getTasksCount());
            System.out.println("["+agent.consoleName()+"] Current devops watching: " + agent.devOpsWatchers.size());
            for(DevOpsWatcher dow : agent.devOpsWatchers)
                System.out.println("[ServerAgent("+ agent.getAID().getLocalName() +")]\t\t" + dow.toString());
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
            checkForCompletedTasks();            
        }
    }