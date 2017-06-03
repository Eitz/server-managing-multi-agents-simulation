/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Random;
import websim.agents.UserAgent;

/**
 *
 * @author eitz
 */
public class AccessWebsiteBehaviour extends TickerBehaviour {
    
    private final UserAgent agent;

    public AccessWebsiteBehaviour(Agent a, int repeatMs) {
        super(a, repeatMs);
        this.agent = (UserAgent) a;
    }

    boolean shouldAccessNow() {
        Random rand = new Random();
        // 1 in 5 times
        int oneIn = 5;
        return (rand.nextInt(oneIn) + 1) == oneIn;
    }

    @Override
    public void onTick() {
        if (getTickCount() > new Random().nextInt(5000)) {
            agent.doDelete();
            return;
        }
        if (shouldAccessNow()) {
            // System.out.println("[UserAgent("+ getLocalName() +")] I am accessing now!");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(agent.connectTo, AID.ISLOCALNAME));
            msg.setLanguage("ENGLISH");
            msg.setOntology("user-access");
            msg.setContent(myAgent.getAID().getLocalName());
            myAgent.send(msg);
        } else {
            // System.out.println("[UserAgent("+ getLocalName() +")] Not accessing!");
        }
    }
    
}
