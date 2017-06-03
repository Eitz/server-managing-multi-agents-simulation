package websim;

import jade.Boot;
import jade.core.Profile;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import websim.agents.DevOpsAgent;
import websim.agents.MaliciousUserAgent;
import websim.agents.SecurityAgent;
import websim.agents.SiteAgent;
import websim.agents.UserAgent;

public class AgentManager {
    
    Runtime jadeRuntime;
    AgentContainer myContainer;
    
    int userId = 1;
    List<UserAgent> users;
    List<SiteAgent> sites;
    List<DevOpsAgent> devOps;
    List<SecurityAgent> securityAgents;
    List<MaliciousUserAgent> maliciousUsers;
    
    void setup(boolean showGui) {
        prepareVariables();
        
        this.initJade(showGui);
        
        try {
            addSite("Google");
            addDevOps("Google");
            
            for (int i=0; i<100; i++)
                addUser("Google");
            
            addMaliciousUser("Google");
            addSecurityAgent("Google");
        } catch (StaleProxyException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private void prepareVariables() {
        users = new LinkedList<>();
        sites = new ArrayList<>();
        devOps = new ArrayList<>();
        securityAgents = new ArrayList<>();
        maliciousUsers = new LinkedList<>();
    }
    
    void initJade(boolean showGui) {
        
        String[] param = new String[ 1 ];
        if (showGui)
            param[ 0 ] = "-gui";
        Boot.main( param );
        jadeRuntime = Runtime.instance();
        createContainer();
    }
    
    private void createContainer() {
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "MyContainer");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        myContainer = jadeRuntime.createAgentContainer(profile);
    }
    
    
    
    void addSite(String site) throws StaleProxyException {
        AgentController agentController = myContainer.createNewAgent(site, "websim.agents.SiteAgent", null);
        agentController.start();
    }
    
    void addUser(String connectTo) throws StaleProxyException {
        String[] agentArgs = new String[] { connectTo };
        AgentController agentController = myContainer.createNewAgent("User-" + userId++, "websim.agents.UserAgent", agentArgs);
        agentController.start();
    }
    
    void addMaliciousUser(String connectTo) throws StaleProxyException {
        String[] agentArgs = new String[] { connectTo };
        AgentController agentController = myContainer.createNewAgent("MaliciousUser-" + userId++, "websim.agents.MaliciousUserAgent", agentArgs);
        agentController.start();
    }
    
    void addDevOps(String connectTo) throws StaleProxyException {
        String[] agentArgs = new String[] { connectTo, "20-80:5" };
        AgentController agentController = myContainer.createNewAgent("DevOps-" + connectTo, "websim.agents.DevOpsAgent", agentArgs);
        agentController.start();
    }
    
    void addSecurityAgent(String connectTo) throws StaleProxyException {
        String[] agentArgs = new String[] { connectTo, "20" };
        AgentController agentController = myContainer.createNewAgent("SecurityAgent-" + connectTo, "websim.agents.SecurityAgent", agentArgs);
        agentController.start();
    }

    
    
}

