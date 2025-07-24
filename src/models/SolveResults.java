package models;

import java.util.List;

public class SolveResults {
    public final List<Cell> visitadas;
    public final List<Cell> camino;

    public SolveResults(List<Cell> visitadas, List<Cell> camino) {
        this.visitadas = visitadas;
        this.camino = camino;
    }
}
