package ucu.edu.aed.tda.generic_trie;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class NodoGenerico<T extends Comparable<T>> implements TNodoGenerico<T> {
    private final T dato;
    private NodoGenerico<T> primerHijo;
    private NodoGenerico<T> siguienteHermano;

    public NodoGenerico(T dato) { this.dato = Objects.requireNonNull(dato); }
    @Override public T getDato() { return dato; }

    @Override
    public boolean agregarHijo(T padre, T hijo) {
        Objects.requireNonNull(hijo);
        NodoGenerico<T> destino = (NodoGenerico<T>) buscar(padre);
        if (destino == null || buscar(hijo) != null) return false;
        NodoGenerico<T> nuevo = new NodoGenerico<>(hijo);
        if (destino.primerHijo == null) destino.primerHijo = nuevo;
        else {
            NodoGenerico<T> ultimo = destino.primerHijo;
            while (ultimo.siguienteHermano != null) ultimo = ultimo.siguienteHermano;
            ultimo.siguienteHermano = nuevo;
        }
        return true;
    }

    @Override
    public TNodoGenerico<T> buscar(Comparable<T> criterio) {
        if (criterio.compareTo(dato) == 0) return this;
        for (NodoGenerico<T> hijo = primerHijo; hijo != null; hijo = hijo.siguienteHermano) {
            TNodoGenerico<T> encontrado = hijo.buscar(criterio);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    @Override
    public TNodoGenerico<T> obtenerPadre(Comparable<T> criterio) {
        for (NodoGenerico<T> hijo = primerHijo; hijo != null; hijo = hijo.siguienteHermano) {
            if (criterio.compareTo(hijo.dato) == 0) return this;
            TNodoGenerico<T> encontrado = hijo.obtenerPadre(criterio);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    @Override
    public TNodoGenerico<T> eliminar(Comparable<T> criterio) {
        if (criterio.compareTo(dato) == 0) return null;
        NodoGenerico<T> anterior = null;
        NodoGenerico<T> actual = primerHijo;
        while (actual != null) {
            NodoGenerico<T> siguiente = actual.siguienteHermano;
            if (actual.eliminar(criterio) == null) {
                if (anterior == null) primerHijo = siguiente;
                else anterior.siguienteHermano = siguiente;
                actual.siguienteHermano = null;
            } else anterior = actual;
            actual = siguiente;
        }
        return this;
    }

    @Override
    public void preOrden(Consumer<TNodoGenerico<T>> consumidor) {
        consumidor.accept(this);
        for (NodoGenerico<T> h = primerHijo; h != null; h = h.siguienteHermano) h.preOrden(consumidor);
    }

    @Override
    public void inOrden(Consumer<TNodoGenerico<T>> consumidor) {
        if (primerHijo != null) primerHijo.inOrden(consumidor);
        consumidor.accept(this);
        if (primerHijo != null) {
            for (NodoGenerico<T> h = primerHijo.siguienteHermano; h != null; h = h.siguienteHermano) h.inOrden(consumidor);
        }
    }

    @Override
    public void postOrden(Consumer<TNodoGenerico<T>> consumidor) {
        for (NodoGenerico<T> h = primerHijo; h != null; h = h.siguienteHermano) h.postOrden(consumidor);
        consumidor.accept(this);
    }

    @Override
    public int altura() {
        int altura = 0;
        for (NodoGenerico<T> h = primerHijo; h != null; h = h.siguienteHermano) altura = Math.max(altura, 1 + h.altura());
        return altura;
    }

    @Override
    public int grado() {
        int cantidad = 0;
        for (NodoGenerico<T> h = primerHijo; h != null; h = h.siguienteHermano) cantidad++;
        return cantidad;
    }

    @Override public void vaciar() { primerHijo = null; }

    @Override
    public List<T> obtenerHijos() {
        List<T> resultado = new LinkedList<>();
        for (NodoGenerico<T> h = primerHijo; h != null; h = h.siguienteHermano) resultado.add(h.dato);
        return resultado;
    }

}
