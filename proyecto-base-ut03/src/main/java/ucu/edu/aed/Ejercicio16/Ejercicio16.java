package ucu.edu.aed.Ejercicio16;

import ucu.edu.aed.tda.generic_trie.TArbolGenerico;

public class Ejercicio16 {
    public static void main(String[] args) {
        Persona ana = new Persona(1, "Ana", 1940);
        Persona luis = new Persona(2, "Luis", 1964);
        Persona marta = new Persona(3, "Marta", 1967);
        Persona pedro = new Persona(4, "Pedro", 1970);
        Persona sofia = new Persona(5, "Sofia", 1990);
        Persona diego = new Persona(6, "Diego", 1993);
        Persona lucia = new Persona(7, "Lucia", 1995);
        Persona pablo = new Persona(8, "Pablo", 1998);
        Persona elena = new Persona(9, "Elena", 2000);
        Persona tomas = new Persona(10, "Tomas", 2003);

        ArbolGenealogico familia = new ArbolGenealogico(ana);
        TArbolGenerico<Persona> arbol = familia;
        arbol.agregarHijo(ana, luis);
        arbol.agregarHijo(ana, marta);
        arbol.agregarHijo(ana, pedro);
        arbol.agregarHijo(luis, sofia);
        arbol.agregarHijo(luis, diego);
        arbol.agregarHijo(marta, lucia);
        arbol.agregarHijo(marta, pablo);
        arbol.agregarHijo(pedro, elena);
        arbol.agregarHijo(pedro, tomas);

        System.out.println("Descendientes de Luis: " + familia.listarDescendientes(luis));
        System.out.println("Altura: " + familia.altura());
        System.out.println("Total de personas: " + familia.contarPersonas());
        for (int i = 0; i <= familia.altura(); i++) System.out.println("Generacion " + i + ": " + familia.obtenerGeneracion(i));
        System.out.println("Ancestro de Sofia y Diego: " + familia.ancestroComunMasCercano(sofia, diego));
        System.out.println("Sofia desciende de Luis: " + familia.esDescendiente(sofia, luis));
    }
}
