import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<PCB> originalJobQueue = new ArrayList<>();
        List<PCB> jobQueue = new ArrayList<>();
        List<PCB> readyQueue = new ArrayList<>();

        Thread loaderThread = new Thread(new JobLoader(originalJobQueue));
        loaderThread.start();
        try {
            loaderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        List<Result> results = new ArrayList<>();

        while (true) {
            System.out.println("\nChoose a scheduling algorithm:");
            System.out.println("1. FCFS");
            System.out.println("2. Round-Robin");
            System.out.println("3. SJF");
            System.out.println("4. Compare Results");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            if (choice == 5) break;

            if (choice == 4) {
                displayResults(results);
                continue;
            }

            jobQueue.clear();
            for (PCB job : originalJobQueue) {
                jobQueue.add(new PCB(job.getId(), job.getBurstTime(), job.getMemoryRequired()));
            }

            Scheduler scheduler = new Scheduler(jobQueue, readyQueue);

            switch (choice) {
                case 1:
                    scheduler.setAlgorithm("FCFS", 0);
                    break;
                case 2:
                    scheduler.setAlgorithm("RoundRobin", 10);
                    break;
                case 3:
                    scheduler.setAlgorithm("SJF", 0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }

            Thread schedulerThread = new Thread(scheduler);
            scheduler.resetScheduler();
            schedulerThread.start();
            try {
                schedulerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = new Result(scheduler.getAlgorithm(), scheduler.getAvgWaitingTime(), scheduler.getAvgTurnaroundTime());
            results.add(result);

            scheduler.stopScheduler();
        }

        System.out.println("Exiting program.");
        scanner.close();
    }

    private static void displayResults(List<Result> results) {
        System.out.println("\nComparison of Scheduling Algorithms:");
        System.out.println("Algorithm\tAvg Waiting Time\tAvg Turnaround Time");
        for (Result result : results) {
            System.out.printf("%s\t\t%.2f ms\t\t%.2f ms\n", result.getAlgorithm(), result.getAvgWaitingTime(), result.getAvgTurnaroundTime());
        }
    }
}
