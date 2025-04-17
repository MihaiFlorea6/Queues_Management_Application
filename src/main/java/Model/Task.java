package Model;

public class Task {
    private final int ID;
    private final int arrivalTime;
    private int serviceTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime() {
        serviceTime--;
    }

    @Override
    public String toString() {
        return "(ID=" + ID + ", Arrival=" + arrivalTime + ", Service=" + serviceTime + ")";
    }
}