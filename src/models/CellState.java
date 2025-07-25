package models;

public enum CellState {
    EMPTY,  // Celda libre
    WALL,   // Muro
    START,  // Punto de inicio
    END,    // Punto de destino
    PATH    // Parte del camino encontrado
}