package ucu.edu.aed.Ejercicio16;

import ucu.edu.aed.tda.generic_trie.ArbolGenerico;
import ucu.edu.aed.tda.generic_trie.TNodoGenerico;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ArbolGenealogico extends ArbolGenerico<Persona> {
    public ArbolGenealogico(Persona ancestro) { super(ancestro); }

    public List<Persona> listarDescendientes(Persona persona) {
        List<Persona> resultado = new LinkedList<>();
        TNodoGenerico<Persona> nodo = raiz == null ? null : raiz.buscar(persona);
        if (nodo != null) nodo.preOrden(actual -> {
            if (actual.getDato().compareTo(persona) != 0) resultado.add(actual.getDato());
        });
        return resultado;
    }

    public int altura() { return raiz == null ? -1 : raiz.altura(); }

    public int contarPersonas() {
        final int[] cantidad = {0};
        preOrden(persona -> cantidad[0]++);
        return cantidad[0];
    }

    public List<Persona> obtenerGeneracion(int generacion) {
        if (generacion < 0) throw new IllegalArgumentException("La generacion no puede ser negativa");
        List<Persona> resultado = new LinkedList<>();
        if (raiz != null) obtenerGeneracion(raiz, generacion, resultado);
        return resultado;
    }

    private void obtenerGeneracion(TNodoGenerico<Persona> nodo, int generacion, List<Persona> resultado) {
        if (generacion == 0) {
            resultado.add(nodo.getDato());
            return;
        }
        for (Persona hijo : nodo.obtenerHijos()) {
            obtenerGeneracion(nodo.buscar(hijo), generacion - 1, resultado);
        }
    }

    public Persona ancestroComunMasCercano(Persona primera, Persona segunda) {
        if (raiz == null) return null;
        LinkedList<Persona> caminoPrimera = new LinkedList<>();
        LinkedList<Persona> caminoSegunda = new LinkedList<>();
        if (!caminoHasta(primera, caminoPrimera) || !caminoHasta(segunda, caminoSegunda)) return null;
        Iterator<Persona> a = caminoPrimera.iterator();
        Iterator<Persona> b = caminoSegunda.iterator();
        Persona comun = null;
        while (a.hasNext() && b.hasNext()) {
            Persona p = a.next();
            Persona q = b.next();
            if (p.compareTo(q) != 0) break;
            comun = p;
        }
        return comun;
    }

    private boolean caminoHasta(Persona persona, LinkedList<Persona> camino) {
        Persona actual = buscar(persona);
        if (actual == null) return false;
        while (actual != null) {
            camino.addFirst(actual);
            actual = obtenerPadre(actual);
        }
        return true;
    }

    public boolean esDescendiente(Persona persona, Persona ancestro) {
        if (raiz == null || persona.compareTo(ancestro) == 0) return false;
        TNodoGenerico<Persona> nodo = raiz.buscar(ancestro);
        return nodo != null && nodo.buscar(persona) != null;
    }
}
