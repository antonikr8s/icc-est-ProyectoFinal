package views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import controllers.MazeController;
import dao.AlgorithmResultDAO;
import models.Cell;
import models.CellState;

import java.awt.*;

public class MazeFrame extends JFrame {
    private final MazePanel mazePanel;
    private final MazeController controller;
    private final JComboBox<String> algorithmSelector;
    private final JButton solveButton;
    private final JButton pasoAPasoButton = new JButton("Paso a paso");

    private List<Cell> pasoCeldasVisitadas;
    private List<Cell> pasoCamino;
    private int pasoIndex = 0;
    private boolean resolvioPasoAPaso = false;

    private final AlgorithmResultDAO resultDAO;

    private static final Map<CellState, Color> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put(CellState.EMPTY, Color.LIGHT_GRAY);
        COLOR_MAP.put(CellState.WALL, Color.BLACK);
        COLOR_MAP.put(CellState.START, Color.GREEN);
        COLOR_MAP.put(CellState.END, Color.RED);
        COLOR_MAP.put(CellState.PATH, Color.BLUE);
    }

    public MazeFrame(int rows, int cols) {
        return null;
    }
}