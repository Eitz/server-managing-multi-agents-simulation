package websim.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.math.BigDecimal;
import websim.DevOpsWatcher;
import websim.agents.SiteAgent;

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
            default:
                break;
        }
    }

    void handleUserAccessMessage(ACLMessage msg) {
        String user = msg.getContent();
        ACLMessage reply = msg.createReply();
        boolean taskAccepted = agent.receiveTask(user, msg.getConversationId());
        if (taskAccepted) {
            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            reply.setOntology("task-status");
            reply.setContent(msg.getConversationId());
        } else {
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setOntology("task-status");
            reply.setContent(msg.getConversationId());
        }
        agent.send(reply);
    }

    void handleDevOpsAlterComputer(ACLMessage msg) {
        System.out.println("Received message from devops: " + msg.getOntology());
        String action = msg.getContent();
        agent.performDevOpsAction(action);
        ACLMessage reply = msg.createReply();
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
        agent.setDevOpsWatcher(watcher);

        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        reply.setOntology("devops-processor-listener");
        reply.setContent("1");
        agent.send(reply);
    }
}