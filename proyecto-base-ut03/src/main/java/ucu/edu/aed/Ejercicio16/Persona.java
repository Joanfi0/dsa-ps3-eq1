package ucu.edu.aed.Ejercicio16;

import java.util.Objects;

public class Persona implements Comparable<Persona> {
    private final int id;
    private final String nombre;
    private final int anioNacimiento;

    public Persona(int id, String nombre, int anioNacimiento) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre);
        this.anioNacimiento = anioNacimiento;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getAnioNacimiento() { return anioNacimiento; }
    @Override public int compareTo(Persona otra) { return Integer.compare(id, otra.id); }
    @Override public boolean equals(Object otra) { return otra instanceof Persona && id == ((Persona) otra).id; }
    @Override public int hashCode() { return Integer.hashCode(id); }
    @Override public String toString() { return nombre + " (" + anioNacimiento + ")"; }
}
