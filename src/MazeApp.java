import javax.swing.JOptionPane;
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
        try {
            String filas = JOptionPane.showInputDialog("Ingrese número de filas:");
            if (filas == null) return null;

            String columnas = JOptionPane.showInputDialog("Ingrese número de columnas:");
            if (columnas == null) return null;

            int f = Integer.parseInt(filas.trim());
            int c = Integer.parseInt(columnas.trim());

            if (f <= 4 || c <= 4) {
                JOptionPane.showMessageDialog(null, "Debe ingresar valores mayores a 4.");
                return null;
            }

            return new int[]{ f, c };
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar números válidos.");
            return null;
        }
    }
}
