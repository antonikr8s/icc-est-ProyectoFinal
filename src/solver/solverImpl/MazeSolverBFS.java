package solver.solverImpl;

import models.*;
import solver.MazeSolver;

public class MazeSolverBFS implements MazeSolver {
    public String getName() { return "BFS"; }
    public SolveResults solve(CellState[][] maze, Cell start, Cell end) {
        long startTime = System.nanoTime();
        return new SolveResults(null, System.nanoTime() - startTime);
    }
}