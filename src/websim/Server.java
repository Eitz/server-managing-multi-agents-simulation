package websim;

import java.math.BigDecimal;
import java.util.List;

public interface Server {
    public boolean addTask(WebTask task);
    
    public boolean hasTask(WebTask task);
    
    public void removeTask(WebTask task);
    
    public List<WebTask> getCompletedTasks();

    public BigDecimal getProcessorUsage();

    public int getCurrentUsersCount();

    public int getTasksCount();
    
    public boolean isCluster();
    public boolean isDegradable();
    
    public boolean upgrade();
    public boolean degrade();
}
