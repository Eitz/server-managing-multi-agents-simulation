package websim.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import websim.agents.behaviours.GetDevOpsMessagesBehaviour;

public class DevOpsAgent extends Agent {
    String connectTo = "";
    String parameters = "20-80:5";

    void getArgs() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            connectTo = (String) args[0];
            if (args[1] != null)
                parameters = (String) args[1];
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
        
        DevOpsAgent agent;

        private RegisterWatchWebSiteBehaviour(Agent a) {
            super(a);
            agent = (DevOpsAgent) a;
        }

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(new AID(agent.connectTo, AID.ISLOCALNAME));
            msg.setLanguage("ENGLISH");
            msg.setOntology("devops-processor-listener");
            msg.setContent(parameters);
            agent.send(msg);
        }
    }
}
