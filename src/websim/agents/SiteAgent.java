package websim.agents;

import jade.core.AID;
import websim.components.WebTask;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;
import websim.components.Computer;
import websim.components.DevOpsWatcher;
import websim.components.Firewall;
import websim.components.LoadBalancer;
import websim.components.SecurityWatcher;
import websim.components.Server;
import websim.agents.behaviours.GetSiteMessagesBehaviour;
import websim.agents.behaviours.PeriodicSiteTasksBehaviour;
import websim.graphics.SitePanelGraphic;
import websim.ui.UIManager;

public class SiteAgent extends Agent {

    public SitePanelGraphic sitePanel;
    
    public Server computer = new Computer(Computer.ComputerSpecs.LOW);
    public Firewall firewall = null;
    public int refreshTime = 2000;
    public List<DevOpsWatcher> devOpsWatchers = new ArrayList<>();
    public List<SecurityWatcher> securityWatchers = new ArrayList<>();
    
    public String consoleName() {
        return "ServerAgent("+ this.getAID().getLocalName() +")";
    }
    
    public void addFirewall() {
        firewall = new Firewall();
    }
    
    public boolean receiveTask(String user, String taskId) {              
        WebTask newTask = new WebTask(user, taskId);
        if (firewall.checkIfOk(user))
            return computer.addTask(newTask);
        else
            return false;
    }
    
    public void answerTask(WebTask task, boolean allGood) {
        ACLMessage msg = new ACLMessage(allGood ? ACLMessage.CONFIRM : ACLMessage.FAILURE);
        msg.addReceiver(new AID(task.user, AID.ISLOCALNAME));
        msg.setLanguage("ENGLISH");
        msg.setOntology("task-reply");
        msg.setContent(task.id);
        this.send(msg);
    }
    
    public void addDevOpsWatcher(DevOpsWatcher devOpsWatcher) {
        devOpsWatchers.add(devOpsWatcher);
    }
    
    public void addSecurityWatcher(SecurityWatcher secWatcher) {
        addFirewall();
        securityWatchers.add(secWatcher);
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
    
    public void informSecurityAgent(SecurityWatcher secWatcher, String maliciousUser) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(secWatcher.getName(), AID.ISLOCALNAME));
        msg.setLanguage("ENGLISH");
        // String ontology = computer.isCluster() ? "devops-alert-cluster" : "devops-alert-server";
        String ontology = "security-server-alert";
        msg.setOntology(ontology);
        msg.setContent(maliciousUser);
        this.send(msg);
        System.out.println("SecurityAgent informed: ("+ maliciousUser +")" + secWatcher.getName());
    }
    
    public void performSecurityAction(String action, String user) {
        System.out.println("SECURITY --> " + action);
        System.out.println("Action --> " + action);
        if (action.equals("ban")) {
            firewall.addRuleBlockUser(user);
            // FIX HERE: add task failed to all tasks of this user
        }
        if (action.equals("unban")) {
            firewall.removeRuleBlockUser(user);
        }
    }
    
    @Override
    protected void setup () {
        addBehaviour(new GetSiteMessagesBehaviour(this));
        addBehaviour(new PeriodicSiteTasksBehaviour(this, refreshTime));
        sitePanel = new SitePanelGraphic(this);
        UIManager.getInstance().addSite(sitePanel);
    }
}
