/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author eitz
 */
public class RegisterWatchWebSiteBehaviour extends OneShotBehaviour {
    
    DevOpsAgent agent;

    RegisterWatchWebSiteBehaviour(Agent a) {
        super(a);
        agent = (DevOpsAgent) a;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(agent.connectTo, AID.ISLOCALNAME));
        msg.setLanguage("ENGLISH");
        msg.setOntology("devops-processor-listener");
        msg.setContent(agent.parameters);
        agent.send(msg);
    }
    
}
