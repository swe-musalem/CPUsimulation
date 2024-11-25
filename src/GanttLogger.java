import java.util.*;

public class GanttLogger {
    private List<String> logs = new ArrayList<>();

    public void logGanttData(int jobId, int startTime, int finishTime, String algorithm) {
        String logEntry = String.format("Job ID: %d, Start: %d ms, Finish: %d ms, Algorithm: %s", jobId, startTime, finishTime, algorithm);
        logs.add(logEntry);
    }

    public void displayLogs() {
        for (String log : logs) {
            System.out.println(log);
        }
    }
}
