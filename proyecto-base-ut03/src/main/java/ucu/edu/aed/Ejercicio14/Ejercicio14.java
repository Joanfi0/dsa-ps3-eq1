package ucu.edu.aed.Ejercicio14;

import ucu.edu.aed.tda.hash.Estrategia;
import ucu.edu.aed.tda.hash.Report;
import ucu.edu.aed.tda.hash.THash;

public class Ejercicio14 {
    private static final int CAPACIDAD = 29;

    public static void main(String[] args) {
        int[] claves = {45, 12, 37, 82, 29, 54, 31, 76, 18, 93, 11, 68};
        int[] ausentes = {74, 41, 66, 111, 58, 83, 60, 105, 47, 122, 40, 97};

        System.out.println("Tamaño 29: es primo y permite mantener la carga por debajo del 50% para el sondeo cuadratico.");
        System.out.println("Funcion hash: h(k) = k % 29.");
        System.out.println("Claves ausentes: 74, 41, 66, 111, 58, 83, 60, 105, 47, 122, 40, 97.");

        for (Estrategia estrategia : Estrategia.values()) {

            int tipo;
            switch (estrategia) {
                case LINEAL:
                    tipo = 1;
                    break;
                case CUADRATICA:
                    tipo = 2;
                    break;
                case ENCADENAMIENTO:
                    tipo = 3;
                    break;
                default:
                    throw new IllegalArgumentException("Estrategia invalida");
            }
            THash<Integer, Integer> tabla = new THash<>(CAPACIDAD, tipo);
            Report reporte = new Report();
            long colisiones = 0;

            String[] estado = new String[CAPACIDAD];

            for (int clave : claves) {
                if (!tabla.insertar(clave, clave, reporte)) {
                    throw new IllegalStateException("El conjunto de prueba contiene una clave repetida");
                }
                int comparaciones = reporte.getCantidadComparaciones();
                if (estrategia == Estrategia.ENCADENAMIENTO) {
                    if (comparaciones > 0) colisiones++;
                } else {
                    colisiones += comparaciones;
                }
                registrarEstado(estado, clave, comparaciones, estrategia);
            }

            System.out.println("\n" + estrategia);
            System.out.println("Estado final:");
            for (int i = 0; i < estado.length; i++) {
                System.out.println(i + ": " + (estado[i] == null ? "vacio" : estado[i]));
            }
            System.out.println("Colisiones: " + colisiones);
            System.out.println("Promedio exitosas: " + promedio(tabla, claves));
            System.out.println("Promedio sin exito: " + promedio(tabla, ausentes));
        }
        System.out.println("En estas pruebas, encadenamiento separado fue mejor: empato en las busquedas exitosas y realizo menos comparaciones en las fallidas.");
    }

    private static void registrarEstado(String[] estado, int clave, int comparaciones, Estrategia estrategia) {
        int inicio = clave % CAPACIDAD;
        if (estrategia == Estrategia.ENCADENAMIENTO) {
            estado[inicio] = estado[inicio] == null ? String.valueOf(clave) : estado[inicio] + " -> " + clave;
        } else {
            long paso = estrategia == Estrategia.CUADRATICA ? (long) comparaciones * comparaciones : comparaciones;
            int posicion = (int) ((inicio + paso) % CAPACIDAD);
            estado[posicion] = String.valueOf(clave);
        }
    }

    private static double promedio(THash<Integer, Integer> tabla, int[] claves) {
        long total = 0;
        Report reporte = new Report();
        for (int clave : claves) {
            tabla.buscar(clave, reporte);
            total += reporte.getCantidadComparaciones();
        }
        return (double) total / claves.length;
    }
}
