package websim;

public class Main {
    static boolean showJadeGui = true;
    public static void main(String [ ] args) {
        AgentManager agentManager = new AgentManager();
        agentManager.setup(showJadeGui);
    }
}
