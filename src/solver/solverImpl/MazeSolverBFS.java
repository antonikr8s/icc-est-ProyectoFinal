package solver.solverImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import models.Cell;
import models.CellState;
import models.SolveResults;
import solver.MazeSolver;

public class MazeSolverBFS implements MazeSolver {

    @Override
    public SolveResults getPath(Cell[][] maze, Cell start, Cell end) {
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] visitado = new boolean[rows][cols];
        Map<Cell, Cell> predecesor = new HashMap<>();
        LinkedList<Cell> cola = new LinkedList<>();
        List<Cell> visitadas = new ArrayList<>();

        Cell inicio = maze[start.row][start.col];
        Cell destino = maze[end.row][end.col];

        cola.add(inicio);
        visitado[inicio.row][inicio.col] = true;

        while (!cola.isEmpty()) {
            Cell actual = cola.poll();
            visitadas.add(actual);

            if (actual.equals(destino)) break;

            int[][] direcciones = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
            for (int[] d : direcciones) {
                int nuevaFila = actual.row + d[0];
                int nuevaCol = actual.col + d[1];

                if (nuevaFila >= 0 && nuevaFila < rows && nuevaCol >= 0 && nuevaCol < cols) {
                    Cell vecino = maze[nuevaFila][nuevaCol];
                    if (!visitado[nuevaFila][nuevaCol] && vecino.state != CellState.WALL) {
                        visitado[nuevaFila][nuevaCol] = true;
                        predecesor.put(vecino, actual);
                        cola.add(vecino);
                    }
                }
            }
        }

        // Reconstruir el camino
        List<Cell> camino = new ArrayList<>();
        Cell paso = destino;
        while (predecesor.containsKey(paso)) {
            camino.add(paso);
            paso = predecesor.get(paso);
        }

        if (paso.equals(inicio)) {
            camino.add(inicio);
            Collections.reverse(camino);
        } else {
            camino.clear(); // No se encontró camino
        }

        return new SolveResults(visitadas, camino);
    }

    @Override
    public String getName() {
        return "BFS";
    }
}
