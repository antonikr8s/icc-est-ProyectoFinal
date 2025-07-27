package solver.solverImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import models.Cell;
import models.CellState;
import models.SolveResults;
import solver.MazeSolver;

public class MazeSolverRecursivoCompleto implements MazeSolver {
    private Set<Cell> visited = new LinkedHashSet<>();
    private List<Cell> camino = new ArrayList<>();

    @Override
    public SolveResults getPath(Cell[][] maze, Cell start, Cell end) {
        visited.clear();
        camino.clear();
        findPath(maze, start.row, start.col, end);
        Collections.reverse(camino);
        return new SolveResults(new ArrayList<>(visited), new ArrayList<>(camino));
    }

    private boolean findPath(Cell[][] maze, int row, int col, Cell target) {
        if (!isValid(maze, row, col)) return false;

        Cell cell = maze[row][col];
        if (visited.contains(cell)) return false;

        visited.add(cell);
        camino.add(cell);

        if (cell.equals(target)) return true;

        if (findPath(maze, row + 1, col, target) ||
            findPath(maze, row, col + 1, target) ||
            findPath(maze, row - 1, col, target) ||
            findPath(maze, row, col - 1, target)) {
            return true;
        }

        camino.remove(camino.size() - 1); 
        return false;
    }

    private boolean isValid(Cell[][] maze, int row, int col) {
        return row >= 0 && row < maze.length &&
               col >= 0 && col < maze[0].length &&
               maze[row][col].state != CellState.WALL;
    }

    @Override
    public String getName() {
        return "Recursivo Completo";
    }
}
