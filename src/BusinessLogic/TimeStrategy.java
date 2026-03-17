package BusinessLogic;

import Model.*;
import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server bestServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod() + task.getServiceTime() <
                    bestServer.getWaitingPeriod() + task.getServiceTime()) {
                bestServer = server;
            }
        }
        bestServer.addTask(task);
    }
}