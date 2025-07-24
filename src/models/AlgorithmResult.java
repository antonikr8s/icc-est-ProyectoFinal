package models;

public class AlgorithmResult {
    public String name;
    public int pathLength;
    public long timeNano;
    public AlgorithmResult(String name, int pathLength, long timeNano) {
        this.name = name;
        this.pathLength = pathLength;
        this.timeNano = timeNano;
    }
}