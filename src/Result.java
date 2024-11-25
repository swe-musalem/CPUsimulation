public class Result {
    private final String algorithm;
    private final double avgWaitingTime;
    private final double avgTurnaroundTime;

    public Result(String algorithm, double avgWaitingTime, double avgTurnaroundTime) {
        this.algorithm = algorithm;
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
    }

    public String getAlgorithm() { return algorithm; }
    public double getAvgWaitingTime() { return avgWaitingTime; }
    public double getAvgTurnaroundTime() { return avgTurnaroundTime; }
}
