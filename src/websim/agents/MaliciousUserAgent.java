package websim.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.Color;
import java.util.Random;
import websim.graphics.LogPanel;
import websim.graphics.SitePanel;
import websim.graphics.UserPanel;
import websim.graphics.UsersPanel;
import websim.ui.UIManager;

public class MaliciousUserAgent extends Agent {
    
    UserPanel userPanel;
    LogPanel logPanel;
    
    String connectTo = "";

    @Override
    protected void setup() {        
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            connectTo = (String) args[0];
        } else {
            throw new Error("UserAgent must receive at least 1 website to connectTo!");
        }        
        addToUsersPanel();
        logPanel.append(getLocalName(), "attacking site!", Color.RED, Color.RED);
        addBehaviour(new AccessWebsiteBehaviour(this, 200));
    }
    
    void addToUsersPanel() {
        SitePanel s = UIManager.getInstance().getSitePanel(connectTo);
        logPanel = s.log;
        userPanel = s.users.addMaliciousUser(connectTo, getLocalName());
    }

    private class AccessWebsiteBehaviour extends TickerBehaviour {
        
        boolean shouldAccessNow() {
            return true;
        }
        
        public AccessWebsiteBehaviour(Agent a, int repeatMs) {
            super(a, repeatMs);
        }
        
        void removeFromUI() {
            UIManager.getInstance()
                .getSitePanel(connectTo).users
                .removeUser(userPanel);
        }

        @Override
        public void onTick() {
            
            if (checkIfBanned()) {
                removeFromUI();
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
                msg.setConversationId(getRandomConversationId());
                myAgent.send(msg);
                userPanel.addTask(msg.getConversationId(), "Connecting...", Color.red);
            } else {
                // System.out.println("[UserAgent("+ getLocalName() +")] Not accessing!");
            }
        }
        
        boolean checkIfBanned() {
            ACLMessage msg = myAgent.receive();
            if (msg == null) {
               return false; 
            } else {
                System.out.println("MESSAGE -> " + msg.getContent());
               return msg.getContent().contains("ban");
            }
        }
        
        private String getRandomConversationId() {
        return 
            getLocalName() + "task-" +
            new Random().nextInt(100000);
        }
    }
}
