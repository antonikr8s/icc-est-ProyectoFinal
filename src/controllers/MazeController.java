package controllers;

import java.awt.Color;
import javax.swing.JButton;
import models.Cell;
import models.CellState;
import views.MazePanel;

public class MazeController {
    private final MazePanel panel;
    private Cell startCell;
    private Cell endCell;
    private Mode currentMode = Mode.WALL;

    public enum Mode {
        START, END, WALL;
    }

    public MazeController(MazePanel panel) {
        this.panel = panel;
        panel.setController(this);
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    public void onCellClicked(int row, int col) {
        switch (this.currentMode) {
            case START -> setStartCell(row, col);
            case END -> setEndCell(row, col);
            case WALL -> toggleWall(row, col);
        }
    }

    public void limpiar() {
        this.panel.limpiarCeldasVisitadas();
    }

    public Cell getStartCell() {
        return this.startCell;
    }

    public Cell getEndCell() {
        return this.endCell;
    }

    public void setStartCell(int row, int col) {
        Cell cell = panel.getCells()[row][col];
        JButton button = panel.getButton(row, col);

        if (startCell != null) {
            panel.getButton(startCell.row, startCell.col).setBackground(Color.WHITE);
            startCell.state = CellState.EMPTY;
        }

        startCell = cell;
        cell.state = CellState.START;
        button.setBackground(Color.GREEN);
    }

    public void setEndCell(int row, int col) {
        Cell cell = panel.getCells()[row][col];
        JButton button = panel.getButton(row, col);

        if (endCell != null) {
            panel.getButton(endCell.row, endCell.col).setBackground(Color.WHITE);
            endCell.state = CellState.EMPTY;
        }

        endCell = cell;
        cell.state = CellState.END;
        button.setBackground(Color.RED);
    }

    public void toggleWall(int row, int col) {
        Cell cell = panel.getCells()[row][col];
        JButton button = panel.getButton(row, col);

        if (cell.state == CellState.EMPTY) {
            cell.state = CellState.WALL;
            button.setBackground(Color.BLACK);
        } else if (cell.state == CellState.WALL) {
            cell.state = CellState.EMPTY;
            button.setBackground(Color.WHITE);
        }
    }
}
