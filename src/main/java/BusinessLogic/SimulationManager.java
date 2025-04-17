package BusinessLogic;

import Model.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable {
    private final int numClients;
    private final int numQueues;
    private final int timeMax;
    private final int minArrival;
    private final int maxArrival;
    private final int minService;
    private final int maxService;
    private final JTextArea logArea;
    private final Scheduler scheduler;
    private final List<Task> generatedTasks = new ArrayList<>();
    private final AtomicInteger currentTime = new AtomicInteger(0);
    private final List<Integer> waitingTimes = new ArrayList<>();
    private final List<Integer> serviceTimes = new ArrayList<>();
    private final Map<Task, Integer> taskEntryTimes = new HashMap<>();
    private int peakHour = 0;
    private int maxClientsInQueues = 0;
    private static final String LOG_FILE = "simulation_log.txt";
    private StringBuilder fullLog = new StringBuilder();


    public SimulationManager(int numClients, int numQueues, int timeMax,
                             int minArrival, int maxArrival, int minService, int maxService,
                             JTextArea logArea) {
        this.numClients = numClients;
        this.numQueues = numQueues;
        this.timeMax = timeMax;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.logArea = logArea;
        this.scheduler = new Scheduler(numQueues, 100);
        this.scheduler.changeStrategy(SelectionPolicy.SHORTEST_QUEUE);
    }

    @Override
    public void run() {
        generateRandomTasks();

        while (currentTime.get() <= timeMax && !allTasksProcessed()) {

            processArrivals(currentTime.get());


            updateQueues(currentTime.get());


            updateLog(currentTime.get());


            currentTime.incrementAndGet();

            try {
                Thread.sleep(1000); // 1 second per time unit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        displayFinalStatistics();
    }

    private void generateRandomTasks() {
        Random rand = new Random();
        for (int i = 0; i < numClients; i++) {
            int arrival = rand.nextInt(maxArrival - minArrival + 1) + minArrival;
            int service = rand.nextInt(maxService - minService + 1) + minService;
            generatedTasks.add(new Task(i + 1, arrival, service));
        }
        Collections.sort(generatedTasks, Comparator.comparingInt(Task::getArrivalTime));
    }

    private void processArrivals(int currentTime) {
        Iterator<Task> iterator = generatedTasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getArrivalTime() == currentTime) {
                scheduler.dispatchTask(task);
                taskEntryTimes.put(task, currentTime); // Record entry time
                serviceTimes.add(task.getServiceTime());
                iterator.remove();
            }
        }
    }

    private void recordWaitingTime(Task task, int exitTime) {
        double waitingTime = exitTime - task.getArrivalTime();
        waitingTimes.add((int) waitingTime);
    }


    private void updateQueues(int currentTime) {
        for (Server server : scheduler.getServers()) {
            synchronized (server) {
                Task completedTask = server.updateTasks(currentTime);
                if (completedTask != null) {
                    recordWaitingTime(completedTask, currentTime);
                }
            }
        }
    }

    private void updateLog(int currentTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time ").append(currentTime).append(":\n");

        sb.append("Waiting clients: ");
        if (generatedTasks.isEmpty()) {
            sb.append("none\n");
        } else {
            generatedTasks.forEach(t -> sb.append(t.toString()).append(" "));
            sb.append("\n");
        }

        List<Server> servers = scheduler.getServers();
        for (int i = 0; i < servers.size(); i++) {
            sb.append("Queue ").append(i + 1).append(": ")
                    .append(servers.get(i).getQueueStatus()).append("\n");
        }

        int currentClientsInQueues = scheduler.getServers().stream().mapToInt(Server::getQueueSize).sum();

        if (currentClientsInQueues > maxClientsInQueues) {
            maxClientsInQueues = currentClientsInQueues;
            peakHour = currentTime;
        }

        logArea.append(sb.toString());

    }

    private boolean allTasksProcessed() {
        if (!generatedTasks.isEmpty()) return false;
        return scheduler.getServers().stream().allMatch(s -> s.getCurrentTask() == null);
    }

    private void displayFinalStatistics() {
        double avgWaitingTime = waitingTimes.stream().mapToInt(Integer::intValue).average().orElse(0);
        double avgServiceTime = serviceTimes.stream().mapToInt(Integer::intValue).average().orElse(0);

        String stats = String.format("\n=== FINAL STATISTICS ===\n" +
                        "Average Waiting Time: %.2f\n" +
                        "Average Service Time: %.2f\n" +
                        "Peak Hour: %d (with %d clients in queues)\n",
                avgWaitingTime, avgServiceTime, peakHour, maxClientsInQueues);

        SwingUtilities.invokeLater(() -> logArea.append(stats));
    }

}