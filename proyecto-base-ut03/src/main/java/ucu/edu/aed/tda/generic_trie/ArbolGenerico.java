package ucu.edu.aed.tda.generic_trie;

import java.util.function.Consumer;

public class ArbolGenerico<T extends Comparable<T>> implements TArbolGenerico<T> {
    protected TNodoGenerico<T> raiz;

    public ArbolGenerico(T datoRaiz) { raiz = new NodoGenerico<>(datoRaiz); }

    @Override
    public boolean agregarHijo(Comparable<T> padre, T hijo) {
        if (raiz == null || raiz.buscar(hijo) != null) return false;
        TNodoGenerico<T> destino = raiz.buscar(padre);
        return destino != null && destino.agregarHijo(destino.getDato(), hijo);
    }

    @Override public void eliminar(Comparable<T> criterio) { if (raiz != null) raiz = raiz.eliminar(criterio); }

    @Override
    public T obtenerPadre(Comparable<T> criterio) {
        TNodoGenerico<T> nodo = raiz == null ? null : raiz.obtenerPadre(criterio);
        return nodo == null ? null : nodo.getDato();
    }

    @Override
    public T buscar(Comparable<T> criterio) {
        TNodoGenerico<T> nodo = raiz == null ? null : raiz.buscar(criterio);
        return nodo == null ? null : nodo.getDato();
    }

    @Override public void preOrden(Consumer<T> consumidor) { if (raiz != null) raiz.preOrden(n -> consumidor.accept(n.getDato())); }
    @Override public void inOrden(Consumer<T> consumidor) { if (raiz != null) raiz.inOrden(n -> consumidor.accept(n.getDato())); }
    @Override public void postOrden(Consumer<T> consumidor) { if (raiz != null) raiz.postOrden(n -> consumidor.accept(n.getDato())); }
    @Override public void vaciar() { raiz = null; }

    @Override
    public int grado(Comparable<T> criterio) {
        TNodoGenerico<T> nodo = raiz == null ? null : raiz.buscar(criterio);
        return nodo == null ? -1 : nodo.grado();
    }

    @Override
    public int altura(Comparable<T> criterio) {
        TNodoGenerico<T> nodo = raiz == null ? null : raiz.buscar(criterio);
        return nodo == null ? -1 : nodo.altura();
    }
}
