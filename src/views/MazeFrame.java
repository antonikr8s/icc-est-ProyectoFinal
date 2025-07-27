package views;

import controllers.MazeController;
import dao.AlgorithmResultDAO;
import dao.daoImpl.AlgorithmResultDAOFile;
import models.AlgorithmResult;
import models.Cell;
import models.CellState;
import models.SolveResults;
import solver.MazeSolver;
import solver.solverImpl.MazeSolverBFS;
import solver.solverImpl.MazeSolverDFS;
import solver.solverImpl.MazeSolverRecursivo;
import solver.solverImpl.MazeSolverRecursivoCompleto;
import solver.solverImpl.MazeSolverRecursivoCompletoBT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        super("Maze Creator");
        this.resultDAO = new AlgorithmResultDAOFile("results.csv");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Laberinto
        this.mazePanel = new MazePanel(rows, cols);
        this.controller = new MazeController(mazePanel);
        mazePanel.setController(controller);
        add(mazePanel, BorderLayout.CENTER);

        // Controles superiores
        JPanel topPanel = new JPanel();
        JButton btnStart = new JButton("Set Start");
        JButton btnEnd = new JButton("Set End");
        JButton btnWall = new JButton("Toggle Wall");
        btnStart.addActionListener(e -> controller.setMode(MazeController.Mode.START));
        btnEnd.addActionListener(e -> controller.setMode(MazeController.Mode.END));
        btnWall.addActionListener(e -> controller.setMode(MazeController.Mode.WALL));
        topPanel.add(btnStart);
        topPanel.add(btnEnd);
        topPanel.add(btnWall);
        add(topPanel, BorderLayout.NORTH);

        // Controles inferiores
        String[] algoritmos = {
                "Recursivo",
                "Recursivo Completo",
                "Recursivo Completo BT",
                "BFS",
                "DFS"
        };
        algorithmSelector = new JComboBox<>(algoritmos);
        solveButton = new JButton("Resolver");
        JButton btnClear = new JButton("Limpiar");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Algoritmo:"));
        bottomPanel.add(algorithmSelector);
        bottomPanel.add(solveButton);
        bottomPanel.add(pasoAPasoButton);
        bottomPanel.add(btnClear);
        add(bottomPanel, BorderLayout.SOUTH);

        solveButton.addActionListener(e -> {
            SolveResults res = resolverYObtenerResultados();
            if (res != null)
                animarVisitadas(res.visitadas, res.camino);
        });

        pasoAPasoButton.addActionListener(e -> {
            if (!resolvioPasoAPaso) {
                SolveResults res = resolverYObtenerResultados();
                if (res != null) {
                    pasoCeldasVisitadas = res.visitadas;
                    pasoCamino = res.camino;
                    pasoIndex = 0;
                    resolvioPasoAPaso = true;
                }
            } else if (pasoIndex < pasoCeldasVisitadas.size()) {
                Cell c = pasoCeldasVisitadas.get(pasoIndex++);
                if (c.state == CellState.EMPTY)
                    paintCell(c, CellState.EMPTY);
            } else if (pasoIndex - pasoCeldasVisitadas.size() < pasoCamino.size()) {
                int idx = pasoIndex - pasoCeldasVisitadas.size();
                Cell c = pasoCamino.get(idx);
                pasoIndex++;
                if (c.state != CellState.START && c.state != CellState.END)
                    paintCell(c, CellState.PATH);
            } else {
                JOptionPane.showMessageDialog(this, "Ya se ha mostrado todo el recorrido.");
                resolvioPasoAPaso = false;
            }
        });

        btnClear.addActionListener(e -> {
            mazePanel.limpiarCeldasVisitadas();
            limpiarPasoAPaso();
        });

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem nuevoItem = new JMenuItem("Nuevo laberinto");
        nuevoItem.addActionListener(e -> reiniciarLaberinto());
        JMenuItem verResItem = new JMenuItem("Ver resultados");
        verResItem.addActionListener(e -> {
            ResultadosDialog dlg = new ResultadosDialog(this, resultDAO);
            dlg.setVisible(true);
        });
        menuArchivo.add(nuevoItem);
        menuArchivo.add(verResItem);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem acercaItem = new JMenuItem("Acerca de");
        acercaItem.addActionListener(e -> mostrarAcercaDe());
        menuAyuda.add(acercaItem);

        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private SolveResults resolverYObtenerResultados() {
    Cell inicio = controller.getStartCell();
    Cell fin = controller.getEndCell();
    if (inicio == null || fin == null) {
        JOptionPane.showMessageDialog(this, "Debe establecer origen y destino.");
        return null;
    }

    mazePanel.limpiarCeldasVisitadas();
    limpiarPasoAPaso();

    String sel = (String) algorithmSelector.getSelectedItem();
    MazeSolver solver;
    switch (sel) {
        case "Recursivo" -> solver = new MazeSolverRecursivo();
        case "Recursivo Completo" -> solver = new MazeSolverRecursivoCompleto();
        case "Recursivo Completo BT" -> solver = new MazeSolverRecursivoCompletoBT();
        case "DFS" -> solver = new MazeSolverDFS();
        case "BFS" -> solver = new MazeSolverBFS();
        default -> solver = new MazeSolverRecursivo();
    }

    long t0 = System.nanoTime();
    SolveResults resultados = solver.getPath(mazePanel.getCells(), inicio, fin);
    long t1 = System.nanoTime();

    if (resultados != null && !resultados.camino.isEmpty()) {
        AlgorithmResult ar = new AlgorithmResult(
                solver.getName(),
                resultados.camino.size(),
                t1 - t0);
        resultDAO.save(ar);
        return resultados;
    } else {
        // Mostrar el mensaje solo después de animar las celdas visitadas
        new Thread(() -> {
            try {
                // Pintar visitadas aunque no se haya encontrado el camino
                for (Cell c : resultados.visitadas) {
                    if (c.state == CellState.EMPTY)
                        SwingUtilities.invokeLater(() -> paintCell(c, CellState.EMPTY));
                    Thread.sleep(30);
                }

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "No se encontró un camino al destino.");
                });
            } catch (InterruptedException ignored) {}
        }).start();

        return resultados;
    }
}


    private void paintCell(Cell c, CellState state) {
        JButton b = mazePanel.getButton(c.row, c.col);
        b.setBackground(COLOR_MAP.get(state));
    }

    private void animarVisitadas(List<Cell> visitadas, List<Cell> camino) {
        new Thread(() -> {
            try {
                for (Cell c : visitadas) {
                    if (c.state == CellState.EMPTY)
                        paintCell(c, CellState.EMPTY);
                    Thread.sleep(30);
                }
                for (Cell c : camino) {
                    if (c.state != CellState.START && c.state != CellState.END)
                        paintCell(c, CellState.PATH);
                    Thread.sleep(80);
                }
            } catch (InterruptedException ignored) {
            }
        }).start();
    }

    private void limpiarPasoAPaso() {
        pasoCeldasVisitadas = null;
        pasoCamino = null;
        pasoIndex = 0;
        resolvioPasoAPaso = false;
    }

    private int[] solicitarDimensiones() {
        try {
            String fs = JOptionPane.showInputDialog("Ingrese número de filas:");
            if (fs == null)
                return null;
            String cs = JOptionPane.showInputDialog("Ingrese número de columnas:");
            if (cs == null)
                return null;
            int f = Integer.parseInt(fs.trim());
            int c = Integer.parseInt(cs.trim());
            if (f <= 4 || c <= 4) {
                JOptionPane.showMessageDialog(this, "Debe ingresar valores mayores a 4.");
                return null;
            }
            return new int[] { f, c };
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Debe ingresar números válidos.");
            return null;
        }
    }

    private void reiniciarLaberinto() {
        int[] dims = solicitarDimensiones();
        if (dims != null) {
            dispose();
            SwingUtilities.invokeLater(() -> new MazeFrame(dims[0], dims[1]));
        }
    }

    private void mostrarAcercaDe() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel("Desarrollado por: Carlos Antonio Gordillo Tenemaza"));
        p.add(Box.createVerticalStrut(10));

        JPanel links = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel l1 = new JLabel("<html><a href=''>github.com/antonikr8s</a></html>");
        l1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/antonikr8s"));
                } catch (Exception ignored) {
                }
            }
        });

        links.add(l1);
        p.add(links);

        JOptionPane.showMessageDialog(this, p, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }

    public MazePanel getMazePanel() {
        return mazePanel;
    }
}
