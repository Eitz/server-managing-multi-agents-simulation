package websim;

import jade.Boot;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentManager {
    
    String host = null; // use default values
    int port = -1; // use default values
    String platformId = null; // use default values
    boolean isMain = true;

    Runtime jadeRuntime;
    AgentContainer serverContainer;
    AgentContainer userContainer;
    
    
    void initJade(boolean showGui) {
        
        String[] param = new String[ 1 ];
        if (showGui)
            param[ 0 ] = "-gui";
        Boot.main( param );
        
    }
    
    void setup(boolean showGui) {
        this.initJade(showGui);
        jadeRuntime = Runtime.instance();
        
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "SiteContainer");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        serverContainer = jadeRuntime.createAgentContainer(profile);
        
        profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "UserContainer");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        userContainer = jadeRuntime.createAgentContainer(profile);
        try {
            String[] agentArgs = new String[ 1 ];
            AgentController agentController = serverContainer.createNewAgent("Google", "websim.agents.SiteAgent", agentArgs);
            agentController.start();
            
            agentArgs = new String[] {"Google"};
            agentController = serverContainer.createNewAgent("GoogleDevOps", "websim.agents.DevOpsAgent", agentArgs);
            agentController.start();            
            
            for (int i=1; i<=30; i++) {
                agentController = userContainer.createNewAgent("User-"+i, "websim.agents.UserAgent", agentArgs);
                agentController.start();
            }
            
        } catch (StaleProxyException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

