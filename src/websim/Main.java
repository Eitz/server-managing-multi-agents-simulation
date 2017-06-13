package websim;

import websim.ui.UIManager;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static boolean showJadeGui = true;
    
    public static void main(String [ ] args) {
        
        setupLog();
        UIManager uiManager = UIManager.getInstance();
        AgentManager agentManager = new AgentManager();
        agentManager.setup(showJadeGui);
        uiManager.setAgentManager(agentManager);
    }
    
    public static void setupLog () {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT %4$s %5$s%6$s%n\n");
        Logger l = Logger.getLogger("websim");
        l.setLevel(Level.ALL);
        l.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        l.addHandler(handler);
    }
}
