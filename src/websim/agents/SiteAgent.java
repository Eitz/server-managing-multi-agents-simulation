package websim.agents;

import jade.core.AID;
import websim.WebTask;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;
import websim.Computer;
import websim.DevOpsWatcher;
import websim.LoadBalancer;
import websim.Server;
import websim.agents.behaviours.GetSiteMessagesBehaviour;
import websim.agents.behaviours.PeriodicSiteTasksBehaviour;

public class SiteAgent extends Agent {
    
    public Server computer = new Computer(Computer.ComputerSpecs.LOW);
    public int refreshTime = 2000;
    public List<DevOpsWatcher> devOpsWatchers = new ArrayList<>();
    
    public String consoleName() {
        return "ServerAgent("+ this.getAID().getLocalName() +")";
    }
    
    public boolean receiveTask(String user, String taskId) {              
        WebTask newTask = new WebTask(user, taskId);
        return computer.addTask(newTask);
    }
    
    public void answerTask(WebTask task, boolean allGood) {
        ACLMessage msg = new ACLMessage(allGood ? ACLMessage.CONFIRM : ACLMessage.FAILURE);
        msg.addReceiver(new AID(task.user, AID.ISLOCALNAME));
        msg.setLanguage("ENGLISH");
        msg.setOntology("task-reply");
        msg.setContent(task.id);
        this.send(msg);
    }
    
    public void setDevOpsWatcher(DevOpsWatcher devOpsWatcher) {
        devOpsWatchers.add(devOpsWatcher);
    }
    
    public void informDevOps(DevOpsWatcher devOpsWatcher, String limit) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(devOpsWatcher.devOpsName, AID.ISLOCALNAME));
        msg.setLanguage("ENGLISH");
        // String ontology = computer.isCluster() ? "devops-alert-cluster" : "devops-alert-server";
        String ontology = "devops-server-alert";
        msg.setOntology(ontology);
        msg.setContent(limit);
        this.send(msg);
        System.out.println("DevOpsInformed: ("+ limit +")" + devOpsWatcher);
    }
    
    public void performDevOpsAction(String action) {
        System.out.println("Action --> " + action);
        if (action.equals("upgrade")) {
            boolean successUpgrade = computer.upgrade();
            
            if (!computer.isCluster()) {
                // FIX HERE, send task-failed to all users
                if (!successUpgrade)
                    computer = new LoadBalancer(2);
            }
        }
        if (action.equals("degrade")) {
            // FIX HERE, send task-failed to all users in the degraded computer
            boolean successDegrade = computer.degrade();
            if (!successDegrade && computer.isCluster()){
                computer = new Computer(Computer.ComputerSpecs.HIGH);
            }
        }
    }
    
    @Override
    protected void setup () {
        addBehaviour(new GetSiteMessagesBehaviour(this));
        addBehaviour(new PeriodicSiteTasksBehaviour(this, refreshTime));
    }
}
