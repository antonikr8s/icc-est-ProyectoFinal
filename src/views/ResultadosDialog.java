package views;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
}