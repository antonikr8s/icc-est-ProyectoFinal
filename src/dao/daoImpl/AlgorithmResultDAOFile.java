package dao.daoImpl;

import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmResultDAOFile implements AlgorithmResultDAO {
    private final File file;

    public AlgorithmResultDAOFile(String filePath) {
        this.file = new File(filePath);
    }

    @Override
    public void save(AlgorithmResult newResult) {
        List<AlgorithmResult> resultados = findAll();
        boolean actualizado = false;

        for (int i = 0; i < resultados.size(); i++) {
            if (resultados.get(i).getAlgorithmName().equalsIgnoreCase(newResult.getAlgorithmName())) {
                resultados.set(i, newResult); 
                actualizado = true;
                break;
            }
        }

        if (!actualizado) {
            resultados.add(newResult); 
        }

        try (FileWriter fw = new FileWriter(this.file, false)) {
            for (AlgorithmResult result : resultados) {
                fw.write(result.toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar resultados: " + e.getMessage());
        }
    }

    @Override
    public List<AlgorithmResult> findAll() {
        List<AlgorithmResult> resultados = new ArrayList<>();

        if (!this.file.exists()) return resultados;

        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String nombre = partes[0];
                    int pathLength = Integer.parseInt(partes[1]);
                    long time = Long.parseLong(partes[2]);
                    resultados.add(new AlgorithmResult(nombre, pathLength, time));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al leer resultados del archivo: " + e.getMessage());
        }

        return resultados;
    }

    @Override
    public void clear() {
        try (FileWriter fw = new FileWriter(this.file, false)) {
            // Limpiar contenido del archivo
        } catch (IOException e) {
            System.err.println("Error al limpiar el archivo: " + e.getMessage());
        }
    }
}
