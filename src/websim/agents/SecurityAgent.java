package websim.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import websim.agents.behaviours.GetDevOpsMessagesBehaviour;

public class SecurityAgent extends Agent {
    String connectTo = "";

    void getArgs() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            connectTo = (String) args[0];
        } else {
            throw new Error("UserAgent must receive at least 1 website to connectTo!");
        }
    }
    
    @Override
    protected void setup() {        
        getArgs();
        addBehaviour(new RegisterWatchWebSiteBehaviour(this));
        addBehaviour(new GetDevOpsMessagesBehaviour(this));
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
            msg.setOntology("devops-processor-listener");
            msg.setContent("30-50:3");
            agent.send(msg);
        }
    }
}
