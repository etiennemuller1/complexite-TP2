package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/** Construit la réduction d'un problème zone-vide vers SAT */
public class Stable {

    /** Représente un graphe */
    public static class Graph {

        int matrix[][]; /* Matrice d'adjacence */
        int size;

        public Graph(int size) {
            this.size = size;
            matrix = new int[size][size];
        }

        /** Construit un graphe à partir d'un fichier
         * Dans le fichier, le premier entier est le nombre de sommets,
         * et les entiers suivant sont les couples de sommets liés par un arc
         *
         * @param filename Le chemin menant au fichier
         */
        public Graph(String filename) {
            try {
                File graph = new File(filename);
                Scanner reader = new Scanner(graph);
                size = reader.nextInt();
                matrix = new int[size][size];
                while (reader.hasNextInt()) {
                    int i = reader.nextInt();
                    int j = reader.nextInt();
                    addEdge(i, j);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /** Rajoute un arc dans le graphe
         *
         * @param i Le premier sommet
         * @param j Le second sommet
         */
        public void addEdge(int i, int j) {
            matrix[i][j] = 1;
            matrix[j][i] = 1;
        }

        /** Renvoie un nouveau graphe de taille size, et dont les arcs
         * sont calculés aléatoirement afin de correspondre approximativement
         * à la densité de graphe demandée.
         *
         * @param size La taille du graphe généré. Doit être positif et non-nul
         * @param density La densité approximative du graphe généré, contenu entre 0.0 et 1.0
         * @return Le graphe généré
         */
        static public Graph generateRandomGraph(int size, double density) {
            Graph graph = new Graph(size);
            Random rand = new Random();

            /* Si les paramètres ont des valeurs illégales */
            if (size <= 0 || (density > 1.0 || density < 0))
                throw new IllegalArgumentException();

            /* On parcourt toutes les paires non ordonnées d'arêtes */
            for (int i = 0; i < graph.size; i++) {
                for (int j = i; j < graph.size; j++) {
                    if (rand.nextDouble() <= density)
                        graph.addEdge(i, j);
                }
            }

            return graph;
        }
    }

    private Formule clauses;
    private Graph graph;

    public Formule computeAndGetFormula() {
        clauses = new Formule();
        
        addAllNeighboursConstraints();
        /*TODO: Rajouter les nouvelles contraintes */

        return clauses;
    }

    /** Rajoute toutes les contraintes de voisinage du graphe */
    private void addAllNeighboursConstraints() {
        for (int vertex = 0; vertex < graph.size; vertex++) {
            for (int neighbour = vertex; neighbour < graph.size; neighbour++) {
                if (graph.matrix[vertex][neighbour] != 0)
                    addNeighbourConstraint(vertex, neighbour);
            }
        }
    }
    /*TODO: Probablement à modifier avec les nouvelles variables */
    
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
