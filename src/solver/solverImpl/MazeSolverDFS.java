package solver.solverImpl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import models.Cell;
import models.CellState;
import models.SolveResults;
import solver.MazeSolver;

public class MazeSolverDFS implements MazeSolver {
    private Set<Cell> visitadas = new LinkedHashSet<>();
    private List<Cell> camino = new ArrayList<>();

    @Override
    public SolveResults getPath(Cell[][] maze, Cell start, Cell end) {
        visitadas.clear();
        camino.clear();
        dfs(maze, start.row, start.col, end);
        return new SolveResults(new ArrayList<>(visitadas), new ArrayList<>(camino));
    }

    private boolean dfs(Cell[][] maze, int row, int col, Cell target) {
        if (!isValid(maze, row, col)) return false;

        Cell cell = maze[row][col];
        if (visitadas.contains(cell)) return false;

        visitadas.add(cell);

        if (cell.equals(target)) {
            camino.add(cell);
            return true;
        }

        if (dfs(maze, row + 1, col, target) ||
            dfs(maze, row - 1, col, target) ||
            dfs(maze, row, col + 1, target) ||
            dfs(maze, row, col - 1, target)) {
            camino.add(cell);
            return true;
        }

        return false;
    }

    private boolean isValid(Cell[][] maze, int row, int col) {
        return row >= 0 && row < maze.length &&
               col >= 0 && col < maze[0].length &&
               maze[row][col].state != CellState.WALL;
    }

    @Override
    public String getName() {
        return "DFS";
    }
}
