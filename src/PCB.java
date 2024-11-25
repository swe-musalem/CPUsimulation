public class PCB {
    private int id;
    private int burstTime;
    private int memoryRequired;
    private int waitingTime = 0;
    private int turnaroundTime = 0;
    private String state;

    public PCB(int id, int burstTime, int memoryRequired) {
        this.id = id;
        this.burstTime = burstTime;
        this.memoryRequired = memoryRequired;
        this.state = "New";
    }

    public int getId() { return id; }
    public int getBurstTime() { return burstTime; }
    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }
    public int getMemoryRequired() { return memoryRequired; }
    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    @Override
    public String toString() {
        return "Process ID: " + id +
                ", Burst Time: " + burstTime + "ms, Memory: " + memoryRequired + "MB, State: " + state;
    }
}
