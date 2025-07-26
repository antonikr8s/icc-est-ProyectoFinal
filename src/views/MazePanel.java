package views;

import controllers.MazeController;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import models.Cell;
import models.CellState;

public class MazePanel extends JPanel {
    private final int rows;
    private final int cols;
    private final Cell[][] cells;
    private final JButton[][] buttons;
    private MazeController controller;

    public MazePanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        this.buttons = new JButton[rows][cols];
        setLayout(new GridLayout(rows, cols));
        initGrid();
    }

    public void setController(MazeController controller) {
        this.controller = controller;
    }

    private void initGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = new Cell(row, col);
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setOpaque(true);
                button.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                int r = row;
                int c = col;
                button.addActionListener(e -> {
                    if (controller != null) {
                        controller.onCellClicked(r, c);
                    }
                });

                add(button);
                cells[row][col] = cell;
                buttons[row][col] = button;
            }
        }
    }

    public void limpiarCeldasVisitadas() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = cells[row][col];
                if (cell.state != CellState.WALL
                        && cell.state != CellState.START
                        && cell.state != CellState.END) {
                    cell.state = CellState.EMPTY;
                    buttons[row][col].setBackground(Color.WHITE);
                }
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public JButton getButton(int row, int col) {
        return buttons[row][col];
    }
}
