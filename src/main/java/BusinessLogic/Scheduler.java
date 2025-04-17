package BusinessLogic;

import Model.*;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxServers, int maxTasksPerServer) {
        servers = new ArrayList<>();
        for (int i = 0; i < maxServers; i++) {
            servers.add(new Server());
        }
        this.strategy = new ShortestQueueStrategy();
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        } else if (policy == SelectionPolicy.TIME_STRATEGY) {
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }

}