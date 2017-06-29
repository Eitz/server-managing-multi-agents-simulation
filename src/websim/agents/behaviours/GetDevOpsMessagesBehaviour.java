package websim.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import websim.agents.DevOpsAgent;

public class GetDevOpsMessagesBehaviour extends CyclicBehaviour {

    DevOpsAgent agent;

    public GetDevOpsMessagesBehaviour(Agent a) {
        super(a);
        agent = (DevOpsAgent) myAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = agent.receive();

        if (msg == null) {
            block();
            return;
        }
        
        // System.out.println("DevOpsReceived Message: " + msg.getOntology());

        switch (msg.getOntology()) {
            case "devops-server-alert":
                handleServerAlert(msg);
                break;
            default:
                break;
        }
    }

    void handleServerAlert(ACLMessage msg) {
        String content = msg.getContent();
        String action = null;
        if (content.equals("min"))
            action = "degrade";
        if (content.equals("max"))
            action = "upgrade";
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.REQUEST);
        reply.setOntology("devops-alter-computer");
        reply.setContent(action);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GetDevOpsMessagesBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        agent.send(reply);
    }
}