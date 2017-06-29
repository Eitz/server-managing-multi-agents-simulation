package websim.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import websim.components.DevOpsWatcher;
import websim.components.SecurityWatcher;
import websim.agents.SiteAgent;
import websim.graphics.UserPanel;
import websim.UIManager;

public class GetSiteMessagesBehaviour extends CyclicBehaviour {

    SiteAgent agent;

    public GetSiteMessagesBehaviour(Agent a) {
        super(a);
        agent = (SiteAgent) myAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = agent.receive();

        if (msg == null) {
            block();
            return;
        }

        switch (msg.getOntology()) {
            case "user-access":
                handleUserAccessMessage(msg);        
                break;
            case "devops-processor-listener":
                handleDevOpsListener(msg);        
                break;
            case "devops-alter-computer":
                handleDevOpsAlterComputer(msg);
                break;
            case "security-access-listener":
                handleSecurityListener(msg);        
                break;
            case "security-add-firewall-rule":
                handleSecurityRule(msg);
                break;
            default:
                break;
        }
    }

    void handleUserAccessMessage(ACLMessage msg) {
        String user = msg.getContent();
        ACLMessage reply = msg.createReply();
        
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(new Random().nextInt(2000));
            } catch (InterruptedException ex) {
                Logger.getLogger(GetSiteMessagesBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
            boolean taskAccepted = agent.receiveTask(user, msg.getConversationId());
            if (taskAccepted) {
                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                reply.setOntology("task-status");
                reply.setContent(msg.getConversationId());
                UserPanel up = UIManager
                        .getInstance()
                        .getSitePanel(agent.getLocalName())
                        .users.getUserPanel(user);
                if (up != null)
                    up.updateTask(msg.getConversationId(), "Waiting...", Color.blue);
            } else {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setOntology("task-status");
                if (!agent.firewall.checkIfOk(user))
                    reply.setContent("ban");
                else
                    reply.setContent(msg.getConversationId());
                UserPanel up = UIManager
                        .getInstance()
                        .getSitePanel(agent.getLocalName())
                        .users.getUserPanel(user);
                if (up != null)
                    up.updateTask(msg.getConversationId(), "500 ERR", Color.red);
                agent.sitePanel.log.append(
                    user,
                    "Error (" + (agent.firewall.checkIfOk(user) ? "TOO MANY TASKS" : "BANNED") + ")",
                    agent.firewall.checkIfOk(user) ? Color.GRAY : Color.RED,
                    agent.firewall.checkIfOk(user) ? Color.BLACK : Color.RED
                );
            }
            agent.send(reply);
        });
        thread.start();
    }

    void handleDevOpsAlterComputer(ACLMessage msg) {
        String action = msg.getContent();
        agent.performDevOpsAction(action);
        ACLMessage reply = msg.createReply();
        
        agent.sitePanel.log.append("DevOps", "Changing computer: (" + action.toUpperCase() +")", Color.BLUE, Color.black);
        agent.sitePanel.agents.getDevOpsPanel().setActive();
        
        reply.setPerformative(ACLMessage.AGREE);
        reply.setOntology("devops-alter-computer");
        reply.setContent("1");
        agent.send(reply);
    }

    // the message: min-max:ticks
    // Min is the minimum processor usage to inform
    // Max is the maximum processor usage to inform
    // ticks is the time of repetitions when to inform
    void handleDevOpsListener(ACLMessage msg) {

        String[] messageSplited = msg.getContent().split(":");
        int ticks = Integer.parseInt(messageSplited[1]);
        String minMax[] = messageSplited[0].split("-");
        BigDecimal min = new BigDecimal(minMax[0]);
        BigDecimal max = new BigDecimal(minMax[1]);

        DevOpsWatcher watcher = new DevOpsWatcher(msg.getSender().getLocalName(), min, max, ticks);
        
        agent.addDevOpsWatcher(watcher);
        agent.sitePanel.agents.getDevOpsPanel().setStatus("Connecting...");
        agent.sitePanel.agents.getDevOpsPanel().setActive();
        agent.sitePanel.log.append("DevOps", "connected!", Color.BLUE, Color.black);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        reply.setOntology("devops-processor-listener");
        reply.setContent("1");
        agent.send(reply);
    }
    
    // the message: min-max:ticks
    // Min is the minimum processor usage to inform
    // Max is the maximum processor usage to inform
    // ticks is the time of repetitions when to inform
    void handleSecurityListener(ACLMessage msg) {

        int maxSimultaneousTask = Integer.parseInt(msg.getContent());

        SecurityWatcher watcher = new SecurityWatcher(msg.getSender().getLocalName(), maxSimultaneousTask);
        agent.addSecurityWatcher(watcher);

        agent.sitePanel.agents.getSecPanel().setStatus("Connecting...");
        agent.sitePanel.agents.getSecPanel().setActive();
        agent.sitePanel.log.append("SecurityAgent", "connected!", Color.BLUE, Color.black);
        
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        reply.setOntology(msg.getOntology());
        reply.setContent("1");
        agent.send(reply);
    }
    
    
    void handleSecurityRule(ACLMessage msg) {

        String[] splittedMessage = msg.getContent().split(":");
        String action = splittedMessage[0];
        String user = splittedMessage[1];
        agent.performSecurityAction(action, user);

        agent.sitePanel
            .agents.getSecPanel()
            .setStatus("New BAN!", Color.RED);
        agent.sitePanel.agents.getSecPanel().setActive();
        agent.sitePanel.log.append("SecurityAgent", "Time to BAN: (" + user + ")", Color.BLUE, Color.BLACK);
        
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        reply.setOntology(msg.getOntology());
        reply.setContent("1");
        agent.send(reply);
    }
    
}