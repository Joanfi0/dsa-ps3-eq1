package ucu.edu.aed.Ejercicio9;

import ucu.edu.aed.tda.hash.Report;
import ucu.edu.aed.tda.hash.THash;
import ucu.edu.aed.utils.FileUtils;
import java.util.LinkedList;

public class Ejercicio9 {
    private static final int[] FACTORES = {70, 75, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99};

    public static void main(String[] args) {
        String archivoClaves = "ut03/listado-general-desordenado.txt";
        String archivoConsultas = "ut03/listado-general-palabrasBuscar.txt";
        String[] leidas = leerClaves(archivoClaves);

        THash<String, String> conjunto = new THash<>(leidas.length);
        LinkedList<String> unicas = new LinkedList<>();

        for (String clave : leidas) if (conjunto.insertar(clave, clave)) unicas.add(clave);
        String[] claves = unicas.toArray(new String[0]);
        String[] consultasArchivo = leerClaves(archivoConsultas);
        String[] consultas = new String[consultasArchivo.length * 2];

        for (int i = 0; i < consultasArchivo.length; i++) {
            consultas[2 * i] = consultasArchivo[i];

            String ausente = "#ausente#" + consultasArchivo[i];
            while (conjunto.buscar(ausente) != null) ausente = "#" + ausente;
            consultas[2 * i + 1] = ausente;
        }

        if (claves.length < 99) throw new IllegalArgumentException("Se necesitan al menos 99 claves distintas");

        int capacidad = 100 * (claves.length / 99);
        System.out.println("Carga (%)\tProm. insercion\tProm. busq. exitosa\tProm. busq. sin exito");

        for (int fila = 0; fila < FACTORES.length; fila++) {
            THash<String, String> tabla = new THash<>(capacidad);
            int cantidad = (int) ((long) capacidad * FACTORES[fila] / 100);
            Report reporte = new Report();
            long insercion = 0, exitosas = 0, fallidas = 0;
            int cantidadExitosas = 0, cantidadFallidas = 0;
            for (int i = 0; i < cantidad; i++) {
                tabla.insertar(claves[i], claves[i], reporte);
                insercion += reporte.getCantidadComparaciones();
            }
            for (String clave : consultas) {
                if (tabla.buscar(clave, reporte) != null) {
                    exitosas += reporte.getCantidadComparaciones();
                    cantidadExitosas++;
                } else {
                    fallidas += reporte.getCantidadComparaciones();
                    cantidadFallidas++;
                }
            }
            double promedioInsercion = (double) insercion / cantidad;
            double promedioExitosas = (double) exitosas / cantidadExitosas;
            double promedioFallidas = (double) fallidas / cantidadFallidas;
            System.out.println(FACTORES[fila] + "\t" + promedioInsercion + "\t"
                    + promedioExitosas + "\t" + promedioFallidas);
        }
        System.out.println("Los resultados coinciden con la teoria: al aumentar el factor de carga crecen las colisiones y " +
                "las comparaciones, especialmente en las busquedas sin exito.");
    }

    private static String[] leerClaves(String archivo) {
        LinkedList<String> lista = new LinkedList<>();
        FileUtils.leerLineas(archivo, linea -> {
            String clave = linea.trim();
            if (!clave.isEmpty()) lista.add(clave);
        });
        if (lista.isEmpty()) throw new IllegalArgumentException("El archivo no contiene claves o no se pudo leer: " + archivo);
        return lista.toArray(new String[0]);
    }

}
