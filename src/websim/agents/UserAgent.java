package websim.agents;

import websim.agents.behaviours.AccessWebsiteBehaviour;
import jade.core.Agent;

public class UserAgent extends Agent {
    
    public String connectTo = "";

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

}
