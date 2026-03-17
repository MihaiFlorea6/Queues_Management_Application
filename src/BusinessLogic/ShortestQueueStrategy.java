package BusinessLogic;

import Model.*;
import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server bestServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod() < bestServer.getWaitingPeriod()) {
                bestServer = server;
            }
        }
        bestServer.addTask(task);
    }
}