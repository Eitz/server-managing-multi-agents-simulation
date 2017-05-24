package websim.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class UserAgent extends Agent {
    String connectTo = "";

    @Override
    protected void setup() {        
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            connectTo = (String) args[0];
        } else {
            throw new Error("UserAgent must receive at least 1 website to connectTo!");
        }        
        addBehaviour(new AccessWebsiteBehaviour(this, 1000));
    }

    private class AccessWebsiteBehaviour extends TickerBehaviour {
        
        boolean shouldAccessNow() {
            Random rand = new Random();
            // 1 in 5 times
            int oneIn = 5;
            return (rand.nextInt(oneIn)+1) == oneIn;
        }
        
        public AccessWebsiteBehaviour(Agent a, int repeatMs) {
            super(a, repeatMs);
        }

        @Override
        public void onTick() {
            if (getTickCount() > new Random().nextInt(5000)){
               doDelete();
               return;
            }
                
            if (shouldAccessNow()) {
                // System.out.println("[UserAgent("+ getLocalName() +")] I am accessing now!");
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID(connectTo, AID.ISLOCALNAME));
                msg.setLanguage("ENGLISH");
                msg.setOntology("user-access");
                msg.setContent(myAgent.getAID().getLocalName());
                myAgent.send(msg);
            } else {
                // System.out.println("[UserAgent("+ getLocalName() +")] Not accessing!");
            }
        }
    }
}
