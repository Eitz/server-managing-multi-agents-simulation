package websim.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import websim.agents.behaviours.GetSecurityMessagesBehaviour;

public class SecurityAgent extends Agent {
    
    String connectTo;
    String parameters;

    void getArgs() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            connectTo = (String) args[0];
            parameters = (String) args[1];
        } else {
            throw new Error("UserAgent must receive at least 1 website to connectTo!");
        }
    }
    
    @Override
    protected void setup() {        
        getArgs();
        addBehaviour(new RegisterWatchWebSiteBehaviour(this));
        addBehaviour(new GetSecurityMessagesBehaviour(this));
    }

    public class RegisterWatchWebSiteBehaviour extends OneShotBehaviour {
        
        SecurityAgent agent;

        private RegisterWatchWebSiteBehaviour(Agent a) {
            super(a);
            agent = (SecurityAgent) a;
        }

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(agent.connectTo, AID.ISLOCALNAME));
            msg.setLanguage("ENGLISH");
            msg.setOntology("security-access-listener");
            msg.setContent(agent.parameters);
            agent.send(msg);
        }
    }
}
