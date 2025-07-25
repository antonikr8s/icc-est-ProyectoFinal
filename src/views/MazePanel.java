package views;

import controllers.MazeController;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import models.Cell;

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
    }
}