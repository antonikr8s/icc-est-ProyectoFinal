package solver;

import models.Cell;
import models.CellState;

public interface MazeSolver {
    String getName();
    models.SolveResults solve(CellState[][] maze, Cell start, Cell end);
}