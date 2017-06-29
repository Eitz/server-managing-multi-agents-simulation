package websim.agents;

import websim.agents.behaviours.AccessWebsiteBehaviour;
import jade.core.Agent;
import java.awt.Color;
import websim.graphics.LogPanel;
import websim.graphics.SitePanel;
import websim.graphics.UserPanel;
import websim.UIManager;

public class UserAgent extends Agent {
    
    public String connectTo = "";
    public UserPanel userPanel;
    public LogPanel logPanel;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            connectTo = (String) args[0];
        } else {
            throw new Error("UserAgent must receive at least 1 website to connectTo!");
        }
        addToUsersPanel();
        logPanel.append(getLocalName(), "connected!", Color.GRAY, Color.BLACK);
        addBehaviour(new AccessWebsiteBehaviour(this, 2000));
    }
    
    void addToUsersPanel() {
        SitePanel s = UIManager.getInstance().getSitePanel(connectTo);
        logPanel = s.log;
        userPanel = s.users.addUser(connectTo, getLocalName());
    }

}
