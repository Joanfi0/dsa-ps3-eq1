package ucu.edu.aed.tda.hash;

import java.util.LinkedList;
import java.util.Objects;

public class THash<K, V> {
    protected TNodoHash<K, V>[] hashTable;
    private final int estrategia;
    private LinkedList<TNodoHash<K, V>>[] cadenas;
    private int cantidad;

    public THash(int elementosEsperados) {
        this(elementosEsperados, 1);
    }

    @SuppressWarnings("unchecked")
    public THash(int elementosEsperados, int estrategia) {
        if (estrategia < 1 || estrategia > 3) throw new IllegalArgumentException("Estrategia invalida");
        this.estrategia = estrategia;
        hashTable = new TNodoHash[calcularCapacidadOptima(elementosEsperados)];
        cadenas = new LinkedList[hashTable.length];
    }

    protected int calcularCapacidadOptima(int elementosEsperados) {
        if (elementosEsperados < 1) throw new IllegalArgumentException("Tamano positivo requerido");
        return elementosEsperados;
    }

    public V buscar(K clave) {
        return buscar(clave, new Report());
    }

    public boolean insertar(K clave, V valor) {
        return insertar(clave, valor, new Report());
    }

    public boolean delete(K clave) {
        return delete(clave, new Report());
    }

    protected int functionHashing(K clave) {
        return Math.floorMod(Objects.requireNonNull(clave).hashCode(), hashTable.length);
    }

    public V buscar(K clave, Report report) {
        report.setCantidadComparaciones(0);
        int inicio = functionHashing(clave);
        if (estrategia == 3) {
            if (cadenas[inicio] != null) {
                for (TNodoHash<K, V> nodo : cadenas[inicio]) {
                    report.setCantidadComparaciones(report.getCantidadComparaciones() + 1);
                    if (clave.equals(nodo.getClave())) return nodo.getValor();
                }
            }
        } else {
            for (int i = 0; i < hashTable.length; i++) {
                long paso = estrategia == 2 ? (long) i * i : i;
                int posicion = (int) ((inicio + paso) % hashTable.length);
                TNodoHash<K, V> nodo = hashTable[posicion];
                if (nodo == null) return null;
                if (!nodo.isLoteLibre()) {
                    report.setCantidadComparaciones(report.getCantidadComparaciones() + 1);
                    if (clave.equals(nodo.getClave())) return nodo.getValor();
                }
            }
        }
        return null;
    }

    public boolean insertar(K clave, V valor, Report report) {
        Objects.requireNonNull(valor, "null se reserva para indicar clave inexistente");
        report.setCantidadComparaciones(0);
        int inicio = functionHashing(clave);
        if (estrategia == 3) {
            if (cadenas[inicio] == null) cadenas[inicio] = new LinkedList<>();
            for (TNodoHash<K, V> nodo : cadenas[inicio]) {
                report.setCantidadComparaciones(report.getCantidadComparaciones() + 1);
                if (clave.equals(nodo.getClave())) return false;
            }
            cadenas[inicio].add(new TNodoHash<>(clave, valor));
            cantidad++;
        } else {
            int libre = -1;
            for (int i = 0; i < hashTable.length; i++) {
                long paso = estrategia == 2 ? (long) i * i : i;
                int p = (int) ((inicio + paso) % hashTable.length);
                TNodoHash<K, V> nodo = hashTable[p];
                if (nodo == null) {
                    if (libre == -1) libre = p;
                    break;
                }
                if (nodo.isLoteLibre()) {
                    if (libre == -1) libre = p;
                } else {
                    report.setCantidadComparaciones(report.getCantidadComparaciones() + 1);
                    if (clave.equals(nodo.getClave())) return false;
                }
            }
            if (libre == -1) {
                redimensionar();
                return insertar(clave, valor, report);
            }
            hashTable[libre] = new TNodoHash<>(clave, valor);
            cantidad++;
        }
        return true;
    }

    public boolean delete(K clave, Report report) {
        report.setCantidadComparaciones(0);
        int inicio = functionHashing(clave);
        if (estrategia == 3) {
            if (cadenas[inicio] == null) return false;
            java.util.Iterator<TNodoHash<K, V>> it = cadenas[inicio].iterator();
            while (it.hasNext()) {
                TNodoHash<K, V> nodo = it.next();
                report.setCantidadComparaciones(report.getCantidadComparaciones() + 1);
                if (clave.equals(nodo.getClave())) { it.remove(); cantidad--; return true; }
            }
        } else {
            for (int i = 0; i < hashTable.length; i++) {
                long paso = estrategia == 2 ? (long) i * i : i;
                int posicion = (int) ((inicio + paso) % hashTable.length);
                TNodoHash<K, V> nodo = hashTable[posicion];
                if (nodo == null) return false;
                if (!nodo.isLoteLibre()) {
                    report.setCantidadComparaciones(report.getCantidadComparaciones() + 1);
                    if (clave.equals(nodo.getClave())) {
                        nodo.setLoteLibre(true); cantidad--; return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean esVacio() { return cantidad == 0; }

    public void vaciar() {
        for (int i = 0; i < hashTable.length; i++) { hashTable[i] = null; cadenas[i] = null; }
        cantidad = 0;
    }

    @SuppressWarnings("unchecked")
    protected boolean redimensionar() {
        Iterable<Entry<K, V>> anteriores = entries();
        int nuevaCapacidad = Math.addExact(Math.multiplyExact(2, hashTable.length), 1);

        boolean primo = false;
        while (!primo) {
            primo = true;
            for (int d = 2; d <= nuevaCapacidad / d; d++) {
                if (nuevaCapacidad % d == 0) { primo = false; break; }
            }
            if (!primo) nuevaCapacidad = Math.incrementExact(nuevaCapacidad);
        }
        hashTable = new TNodoHash[nuevaCapacidad];
        cadenas = new LinkedList[hashTable.length];
        cantidad = 0;
        for (Entry<K, V> entrada : anteriores) insertar(entrada.getClave(), entrada.getValor());
        return true;
    }

    public Iterable<Entry<K, V>> entries() {
        LinkedList<Entry<K, V>> resultado = new LinkedList<>();
        for (int i = 0; i < hashTable.length; i++) {
            if (estrategia == 3) {
                if (cadenas[i] != null) for (TNodoHash<K, V> nodo : cadenas[i]) resultado.add(nodo.getEntry());
            } else if (hashTable[i] != null && !hashTable[i].isLoteLibre()) {
                resultado.add(hashTable[i].getEntry());
            }
        }
        return resultado;
    }

    public Iterable<K> keys() {
        LinkedList<K> resultado = new LinkedList<>();
        for (Entry<K, V> entrada : entries()) resultado.add(entrada.getClave());
        return resultado;
    }

    public Iterable<V> values() {
        LinkedList<V> resultado = new LinkedList<>();
        for (Entry<K, V> entrada : entries()) resultado.add(entrada.getValor());
        return resultado;
    }

}
