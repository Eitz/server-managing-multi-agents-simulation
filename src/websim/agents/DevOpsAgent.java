package websim.agents;

import websim.agents.behaviours.RegisterWatchWebSiteBehaviour;
import jade.core.Agent;
import websim.agents.behaviours.GetDevOpsMessagesBehaviour;

public class DevOpsAgent extends Agent {
    
    public String connectTo = "";
    public String parameters = "20-80:5";

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

}
