import java.io.*;
import java.util.*;

public class JobLoader implements Runnable {
    private final List<PCB> jobQueue;

    public JobLoader(List<PCB> jobQueue) {
        this.jobQueue = jobQueue;
    }

    @Override
    public void run() {
        System.out.println("Loading jobs from 'job.txt'...");
        try (BufferedReader reader = new BufferedReader(new FileReader("job.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split("[:;]");
                int id = Integer.parseInt(parts[0]);
                int burstTime = Integer.parseInt(parts[1]);
                int memoryRequired = Integer.parseInt(parts[2]);
                PCB job = new PCB(id, burstTime, memoryRequired);
                jobQueue.add(job);
                System.out.println("Added Job: " + job);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
