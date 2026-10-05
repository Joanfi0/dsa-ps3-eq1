package ucu.edu.aed.tda.trie.impl;

import ucu.edu.aed.tda.trie.Entry;
import ucu.edu.aed.tda.trie.TNodoTrie;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class NodoTrie<T> implements TNodoTrie<T> {
    private final List<NodoTrie<T>> hijos = new LinkedList<>();
    private char caracter;
    private String palabra = "";
    private boolean esPalabra;
    private T dato;

    @Override
    public void recorrer(Consumer<Entry<T>> consumer) {
        Objects.requireNonNull(consumer);
        if (esPalabra) consumer.accept(new Entry<>(dato, true, palabra));
        for (NodoTrie<T> hijo : hijos) hijo.recorrer(consumer);
    }

    @Override
    public Entry<T> buscar(String palabra) {
        Objects.requireNonNull(palabra);
        NodoTrie<T> actual = this;
        for (int i = 0; i < palabra.length(); i++) {
            NodoTrie<T> siguiente = null;
            for (NodoTrie<T> hijo : actual.hijos) {
                if (hijo.caracter == palabra.charAt(i)) {
                    siguiente = hijo;
                    break;
                }
            }
            if (siguiente == null) return null;
            actual = siguiente;
        }
        return new Entry<>(actual.getDato(), actual.esPalabra(), actual.palabra);
    }

    @Override
    public boolean insertar(String palabra, T dato) {
        Objects.requireNonNull(palabra);
        NodoTrie<T> actual = this;
        for (int i = 0; i < palabra.length(); i++) {
            NodoTrie<T> siguiente = null;
            for (NodoTrie<T> hijo : actual.hijos) {
                if (hijo.caracter == palabra.charAt(i)) {
                    siguiente = hijo;
                    break;
                }
            }
            if (siguiente == null) {
                siguiente = new NodoTrie<>();
                siguiente.caracter = palabra.charAt(i);
                siguiente.palabra = actual.palabra + siguiente.caracter;
                actual.hijos.add(siguiente);
            }
            actual = siguiente;
        }
        if (actual.esPalabra) return false;
        actual.esPalabra = true;
        actual.dato = dato;
        return true;
    }

    @Override
    public List<Entry<T>> predecir(String prefijo) {
        Objects.requireNonNull(prefijo);
        List<Entry<T>> resultado = new LinkedList<>();
        NodoTrie<T> actual = this;
        for (int i = 0; i < prefijo.length(); i++) {
            NodoTrie<T> siguiente = null;
            for (NodoTrie<T> hijo : actual.hijos) {
                if (hijo.caracter == prefijo.charAt(i)) {
                    siguiente = hijo;
                    break;
                }
            }
            if (siguiente == null) return resultado;
            actual = siguiente;
        }
        actual.recorrer(resultado::add);
        return resultado;
    }

    @Override
    public T getDato() {
        return esPalabra ? dato : null;
    }

    @Override
    public boolean esPalabra() {
        return esPalabra;
    }
}
