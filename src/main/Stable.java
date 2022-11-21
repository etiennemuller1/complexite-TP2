package main;

import java.util.ArrayList;

/** Construit la réduction d'un problème zone-vide vers SAT */
public class Stable {
    private Formule clauses;

    /** Rajoute une contrainte de voisinage pour le sommet et son voisin donnés
     *  (si on prend le sommet, alors on ne prend pas son voisin)
     *  Voir rapport pour plus de détails
     *
     * @param variableSommet Le sommet
     * @param variableVoisin Son voisin
     */
    private void addNeighbourConstraint(int variableSommet, int variableVoisin) {
        ArrayList<Integer> constraint = new ArrayList<>(2);
        constraint.add(-variableSommet);
        constraint.add(-variableVoisin);

        clauses.addClause(constraint);
    }
    /*TODO: Trèèès probablement à changer, comme un seul sommet ne va pas correspondre à une seule variable */
}
