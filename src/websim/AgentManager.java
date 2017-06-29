package websim;

import jade.core.Profile;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentManager {
    
    private static AgentManager instance;
    synchronized public static AgentManager getInstance() {
        if (instance == null) instance = new AgentManager();
        return instance;
    }
    
    Runtime jadeRuntime;
    AgentContainer myContainer;
    
    int userId = 1;
    List<String> sites;
    
    void setup(boolean showGui) {
        
        sites = new ArrayList<>();
        
        this.initJade(showGui);
        
        
        addSite("Google.com");
        addDevOps("Google.com");
        addSecurityAgent("Google.com");
        
        addSite("Yahoo.com");
        addDevOps("Yahoo.com");
        addSecurityAgent("Yahoo.com");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        addMaliciousUser();
        
        int maliciousRespawn = 30;
        int maliciousCount = maliciousRespawn;
        
        while(true) {
            try {
                Thread.sleep(500 + new Random().nextInt(500));
            } catch (InterruptedException ex) {
                Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            maliciousCount--;
            addUser();
            if (maliciousCount==0) {
                addMaliciousUser();
                maliciousCount=maliciousRespawn;
            }
            if (new Random().nextInt(100) < 15) {
                addBurstOfUsers();
            }
            
        }
    }
    
    void initJade(boolean showGui) {
        jadeRuntime = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "WebSim");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, showGui ? "true" : "false");
        myContainer = jadeRuntime.createMainContainer(profile);
    }
   
    public void addAgent(String name, String agent) {
        addAgent(name, agent, null);
    }
    
    public void addAgent(String name, String agent, String[] args) {
        try {
            AgentController agentController = myContainer.createNewAgent(name, agent, args);
            agentController.start();
        } catch (StaleProxyException ex) {
            Logger.getLogger(AgentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addSite(String site) {
        sites.add(site);
        addAgent(site, "websim.agents.SiteAgent");
    }
    
    public void addUser(String connectTo) {
        String[] agentArgs = new String[] { connectTo };
        addAgent("User-" + userId++, "websim.agents.UserAgent", agentArgs);
    }
    
    public void addMaliciousUser(String connectTo) {
        String[] agentArgs = new String[] { connectTo };
        addAgent("MaliciousUser-" + userId++, "websim.agents.MaliciousUserAgent", agentArgs);        
    }
    
    void addDevOps(String connectTo) {
        String[] agentArgs = new String[] { connectTo, "20-80:5" };
        addAgent("DevOps-" + connectTo, "websim.agents.DevOpsAgent", agentArgs);
    }
    
    void addSecurityAgent(String connectTo) {
        String[] agentArgs = new String[] { connectTo, "10" };
        addAgent("SecurityAgent-" + connectTo, "websim.agents.SecurityAgent", agentArgs);
    }

    public void addMaliciousUser() {
        Random rand = new Random();
        String site = sites.get(rand.nextInt(sites.size()));
        addMaliciousUser(site);
    }
    
    public void addUser() {
        Random rand = new Random();
        String site = sites.get(rand.nextInt(sites.size()));
        addUser(site);
    }
    
    void addBurstOfUsers() {
        Random rand = new Random();
        String site = sites.get(rand.nextInt(sites.size()));
        for (int i=0; i<new Random().nextInt(30); i++)
            addUser(site);
    }

    
    
}

