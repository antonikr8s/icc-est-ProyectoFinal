package models;

import java.util.List;

public class SolveResults {
    public List<models.Cell> path;
    public long time;
    public SolveResults(List<models.Cell> path, long time) {
        this.path = path;
        this.time = time;
    }
}