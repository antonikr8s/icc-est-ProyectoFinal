package models;

public class AlgorithmResult {
    private final String algorithmName;
    private final int pathLength;
    private final long timeMs;

    public AlgorithmResult(String algorithmName, int pathLength, long timeMs) {
        this.algorithmName = algorithmName;
        this.pathLength = pathLength;
        this.timeMs = timeMs;
    }

    public String getAlgorithmName() {
        return this.algorithmName;
    }

    public int getPathLength() {
        return this.pathLength;
    }

    public long getTimeMs() {
        return this.timeMs;
    }

    @Override
    public String toString() {
        return algorithmName + "," + pathLength + "," + timeMs;
    }
}
