package main;

import java.util.ArrayList;
import java.util.Iterator;

public class Clause implements Iterable<Integer> {
    ArrayList<Integer> clauseParts;

    /** Créée une nouvelle clause */
    public Clause() {
        this.clauseParts = new ArrayList<>();
    }

    /** Créée une nouvelle clause optimisée pour le nombre d'éléments spécifiés
     *
     * @param size La taille d'éléments que devra contenir cette clause
     *             Elle sera redimensionnée automatiquement si besoin est
     */
    public Clause(int size) {
        this.clauseParts = new ArrayList<>(size);
    }

    public void add(int clausePart) {
        clauseParts.add(clausePart);
    }

    public String createCNFLine() {
        String line = "";
        for (Integer part : clauseParts
             ) {
            line += part.toString() + " ";
        }
        line += "0";
        return line;
    }

    @Override
    public Iterator<Integer> iterator() {
        return clauseParts.iterator();
    }
}
