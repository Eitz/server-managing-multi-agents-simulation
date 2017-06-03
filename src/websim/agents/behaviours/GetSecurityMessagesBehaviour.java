package websim.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
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
        
        System.out.println("SecurityAgent received message: " + msg.getOntology());

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
        agent.send(reply);
    }
}