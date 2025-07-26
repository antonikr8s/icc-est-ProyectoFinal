import javax.swing.SwingUtilities;

import views.MazeFrame;

public class MazeApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int[] dimensiones = solicitarDimensiones();
            if (dimensiones != null) {
                new MazeFrame(dimensiones[0], dimensiones[1]);
            }
        });
    }

    private static int[] solicitarDimensiones() {
        return null;
    }
}