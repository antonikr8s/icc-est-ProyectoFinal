package views;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ResultadosDialog extends JDialog {
    private final DefaultTableModel model;
    private final AlgorithmResultDAO resultDAO;
    private List<AlgorithmResult> results;

    // Orden personalizado de algoritmos
    private static final String[] ALGORITMO_ORDEN = {
        "Recursivo",
        "Recursivo Completo",
        "Recursivo Completo BT",
        "BFS",
        "DFS"
    };

    public ResultadosDialog(JFrame parent, AlgorithmResultDAO dao) {
        super(parent, "Resultados Guardados", true);
        this.resultDAO = dao;

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Algoritmo", "Celdas Camino", "Tiempo (ns)"}, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        JPanel buttonPanel = new JPanel();
        JButton clearButton = new JButton("Limpiar Resultados");
        JButton chartButton = new JButton("Graficar Resultados");

        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas borrar todos los resultados?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                resultDAO.clear();
                model.setRowCount(0);
            }
        });

        chartButton.addActionListener(e -> mostrarGrafica());

        buttonPanel.add(clearButton);
        buttonPanel.add(chartButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void loadData() {
        results = resultDAO.findAll();
        ordenarResultados();
        model.setRowCount(0);
        for (AlgorithmResult ar : results) {
            model.addRow(new Object[]{
                ar.getAlgorithmName(),
                ar.getPathLength(),
                ar.getTimeMs()
            });
        }
    }

    private void ordenarResultados() {
        results.sort(Comparator.comparingInt(ar -> {
            for (int i = 0; i < ALGORITMO_ORDEN.length; i++) {
                if (ALGORITMO_ORDEN[i].equalsIgnoreCase(ar.getAlgorithmName())) {
                    return i;
                }
            }
            return ALGORITMO_ORDEN.length; // Los algoritmos no listados van al final
        }));
    }

    private void mostrarGrafica() {
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos para graficar.");
            return;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (AlgorithmResult ar : results) {
            dataset.addValue(ar.getTimeMs(), "Tiempo (ns)", ar.getAlgorithmName());
        }

        JFreeChart chart = ChartFactory.createLineChart(
            "Tiempos de Ejecución por Algoritmo",
            "Algoritmo",
            "Tiempo (ns)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JDialog chartDialog = new JDialog(this, "Gráfica de Resultados", true);
        chartDialog.setContentPane(chartPanel);
        chartDialog.setSize(600, 400);
        chartDialog.setLocationRelativeTo(this);
        chartDialog.setVisible(true);
    }
}
