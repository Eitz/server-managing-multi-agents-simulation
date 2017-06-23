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
import java.awt.Color;
import java.util.Random;
import websim.agents.UserAgent;
import websim.ui.UIManager;

/**
 *
 * @author eitz
 */
public class AccessWebsiteBehaviour extends TickerBehaviour {
    
    private final UserAgent agent;
    int removalTick;

    public AccessWebsiteBehaviour(Agent a, int repeatMs) {
        super(a, repeatMs);
        this.agent = (UserAgent) a;
        removalTick = new Random().nextInt(3000);
    }

    boolean shouldAccessNow() {
        Random rand = new Random();
        // 1 in 5 times
        int oneIn = 5;
        return (rand.nextInt(oneIn) + 1) == oneIn;
    }

    void removeFromUI() {
        UIManager.getInstance()
                .getSitePanel(agent.connectTo).users
                .removeUser(agent.userPanel);
    }
    
    @Override
    public void onTick() {
        if (getTickCount() > removalTick) {
            removeFromUI();
            agent.doDelete();
            return;
        }
        if (getTickCount() == 1 || shouldAccessNow()) {
            // System.out.println("[UserAgent("+ getLocalName() +")] I am accessing now!");
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(agent.connectTo, AID.ISLOCALNAME));
            msg.setLanguage("ENGLISH");
            msg.setOntology("user-access");
            msg.setContent(myAgent.getAID().getLocalName());
            msg.setConversationId(getRandomConversationId());
            myAgent.send(msg);
            agent.userPanel.addTask(msg.getConversationId(), "Connecting...", Color.black);
        } else {
            // System.out.println("[UserAgent("+ getLocalName() +")] Not accessing!");
        }
    }

    private String getRandomConversationId() {
        return 
            agent.getLocalName() + "task-" +
            new Random().nextInt(100000);
    }
    
}
