package Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private final BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
    private final AtomicInteger waitingPeriod = new AtomicInteger(0);

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public Task getCurrentTask() {
        return tasks.peek();
    }

    public Task updateTasks(int currentTime) {
        Task current = tasks.peek();
        if (current != null && current.getArrivalTime() <= currentTime) {
            current.decrementServiceTime();

            if (current.getServiceTime() == 0) {
                return tasks.poll(); // Returnăm task-ul completat
            }
        }
        return null;
    }

    public String getQueueStatus() {
        if (tasks.isEmpty()) {
            return "closed";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Task t : tasks) {
                sb.append(t.toString()).append("; ");
            }
            return sb.toString();
        }
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public int getQueueSize() {
        return tasks.size();
    }

}