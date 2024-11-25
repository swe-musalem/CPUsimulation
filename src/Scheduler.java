import java.util.*;

public class Scheduler implements Runnable {
    private final List<PCB> readyQueue;
    private final List<PCB> jobQueue;
    private final int memoryLimit = 1024;
    private int currentMemoryUsage = 0;
    private String algorithm;
    private int quantum;
    private boolean stop = false;
    private double totalWaitingTime = 0;
    private double totalTurnaroundTime = 0;
    private int jobCount = 0;
    private final List<String> ganttChart = new ArrayList<>();

    public Scheduler(List<PCB> jobQueue, List<PCB> readyQueue) {
        this.jobQueue = jobQueue;
        this.readyQueue = readyQueue;
    }

    public void setAlgorithm(String algorithm, int quantum) {
        this.algorithm = algorithm;
        this.quantum = quantum;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public double getAvgWaitingTime() {
        return jobCount > 0 ? totalWaitingTime / jobCount : 0;
    }

    public double getAvgTurnaroundTime() {
        return jobCount > 0 ? totalTurnaroundTime / jobCount : 0;
    }

    public void loadToReadyQueue() {
        while (!jobQueue.isEmpty()) {
            PCB job = jobQueue.get(0);
            if (currentMemoryUsage + job.getMemoryRequired() <= memoryLimit) {
                job.setState("Ready");
                readyQueue.add(job);
                currentMemoryUsage += job.getMemoryRequired();
                jobQueue.remove(0);
                System.out.println("Loaded process " + job.getId() + " into ready queue. Memory used: " + currentMemoryUsage + "MB");
            } else {
                System.out.println("Not enough memory to load process " + job.getId() + ". Waiting for memory to be freed...");
                break;
            }
        }
    }

    private boolean allJobsProcessed() {
        return jobQueue.isEmpty() && readyQueue.isEmpty();
    }

    @Override
    public void run() {
        while (!stop) {
            while (!allJobsProcessed()) {
                loadToReadyQueue();

                if (readyQueue.isEmpty()) break;

                switch (algorithm) {
                    case "FCFS":
                        scheduleFCFS();
                        break;
                    case "RoundRobin":
                        scheduleRoundRobin(quantum);
                        break;
                    case "SJF":
                        scheduleSJF();
                        break;
                }
            }
            stopScheduler();
        }

        displayGanttChart();
    }

    private void updateStats(PCB job, int currentTime) {
        job.setTurnaroundTime(currentTime);
        job.setWaitingTime(currentTime - job.getBurstTime());
        totalWaitingTime += job.getWaitingTime();
        totalTurnaroundTime += job.getTurnaroundTime();
        jobCount++;
    }

    public void scheduleFCFS() {
        int currentTime = 0;

        while (!readyQueue.isEmpty()) {
            PCB job = readyQueue.remove(0);
            int startTime = currentTime;
            System.out.println("Starting Job " + job.getId() + " at time " + startTime + "ms");
            currentTime += job.getBurstTime();
            ganttChart.add("P" + job.getId() + " (" + startTime + "-" + currentTime + "ms)");
            updateStats(job, currentTime);
            System.out.println("Finished Job " + job.getId() + " at time " + currentTime + "ms");
            currentMemoryUsage -= job.getMemoryRequired();
        }
    }

    public void scheduleRoundRobin(int quantum) {
        int currentTime = 0;
        Queue<PCB> queue = new LinkedList<>(readyQueue);

        while (!queue.isEmpty()) {
            PCB job = queue.poll();
            int startTime = currentTime;

            if (job.getBurstTime() > quantum) {
                System.out.println("Job " + job.getId() + " paused, remaining burst time: " + (job.getBurstTime() - quantum) + "ms");
                currentTime += quantum;
                ganttChart.add("P" + job.getId() + " (" + startTime + "-" + currentTime + "ms)");
                job.setBurstTime(job.getBurstTime() - quantum);
                queue.add(job);
            } else {
                currentTime += job.getBurstTime();
                ganttChart.add("P" + job.getId() + " (" + startTime + "-" + currentTime + "ms)");
                updateStats(job, currentTime);
                System.out.println("Finished Job " + job.getId() + " at time " + currentTime + "ms");
                currentMemoryUsage -= job.getMemoryRequired();
            }
        }
        readyQueue.clear();
    }

    public void scheduleSJF() {
        readyQueue.sort(Comparator.comparingInt(PCB::getBurstTime));
        int currentTime = 0;

        while (!readyQueue.isEmpty()) {
            PCB job = readyQueue.remove(0);
            int startTime = currentTime;
            ganttChart.add("P" + job.getId() + " (" + startTime + "-" + (startTime + job.getBurstTime()) + "ms)"); // Add time info
            currentTime += job.getBurstTime();
            updateStats(job, currentTime);
            System.out.println("Finished Job " + job.getId() + " at time " + currentTime + "ms");
            currentMemoryUsage -= job.getMemoryRequired();
        }
    }

    public void stopScheduler() {
        stop = true;
    }

    public void resetScheduler() {
        stop = false;
        totalWaitingTime = 0;
        totalTurnaroundTime = 0;
        jobCount = 0;
        ganttChart.clear();
    }

    private void displayGanttChart() {
        for (String entry : ganttChart) {
            System.out.print("| " + entry + " ");
        }
        System.out.println("|");
    }
}
