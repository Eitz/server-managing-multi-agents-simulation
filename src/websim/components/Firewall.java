
package websim.components;

import websim.components.WebTask;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eitz
 */
public class Firewall {
    List<String> blockUsers;
    
    public Firewall() {
        blockUsers = new ArrayList<>();
    }
    
    public void addRuleBlockUser(String user) {
        if (!blockUsers.contains(user))
            blockUsers.add(user);
    }
    
    public void removeRuleBlockUser(String user) {
        if (blockUsers.contains(user))
            blockUsers.remove(user);
    }
    
    public boolean checkIfOk(String user) {
        return ! blockUsers.contains(user);
    }
}
