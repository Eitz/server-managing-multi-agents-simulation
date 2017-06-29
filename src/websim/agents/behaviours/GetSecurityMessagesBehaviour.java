package websim.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import websim.agents.SecurityAgent;

public class GetSecurityMessagesBehaviour extends CyclicBehaviour {

    SecurityAgent agent;

    public GetSecurityMessagesBehaviour(Agent a) {
        super(a);
        agent = (SecurityAgent) myAgent;
    }

    @Override
    public void action() {
        ACLMessage msg = agent.receive();

        if (msg == null) {
            block();
            return;
        }

        switch (msg.getOntology()) {
            case "security-server-alert":
                handleServerAlert(msg);
                break;
            default:
                break;
        }
    }

    void handleServerAlert(ACLMessage msg) {
        String user = msg.getContent();
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.REQUEST);
        reply.setOntology("security-add-firewall-rule");
        reply.setContent("ban:" + user);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GetSecurityMessagesBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        agent.send(reply);
    }
}