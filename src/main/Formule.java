package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/** Représente une formule de type SAT, composée de clauses */
public class Formule implements Iterable<ArrayList<Integer>> {
    ArrayList<ArrayList<Integer>> clauses;

    /** Construit une nouvelle Formule
     *
     * @param size Le nombre de clauses que l'on compte mettre dans cette instance
     *             Cette valeur peut être inexacte
     */
    public Formule(int size) {
        this.clauses = new ArrayList<>(size);
    }

    @Override
    public Iterator iterator() {
        return clauses.iterator();
    }

    @Override
    public String toString() {
        return clauses.toString();
    }

    /** Rajoute une clause dans la formule
     *
     * @param clause La clause, sous forme de liste de littéraux la composant
     *               On suppose qu'elle est bien formulé (e.g. il n'y a pas de littéral 0)
     */
    public void addClause(ArrayList<Integer> clause) {
        clauses.add(clause);
    }

    public int getNbOfClauses() {
        return clauses.size();
    }

    static public Formule fromFile(String path) throws FileNotFoundException {
        Formule formule;
        Scanner scan;
        int nbVariables, nbClauses;

        /* On tente d'ouvrir le fichier */
        scan = new Scanner(new File(path));
        scan.next(); /* On saute "p" */
        scan.next(); /* On saute "cnf" */
        nbVariables = scan.nextInt();
        nbClauses = scan.nextInt();
        formule = new Formule(nbClauses);

        /* On récupère maintenant les clauses */
        for (int i = 0; i < nbClauses; i++) {
            ArrayList<Integer> clause = new ArrayList<>();
            formule.clauses.add(clause);

            int literal = scan.nextInt();
            while (literal != 0) { /* Tant qu'on est pas à la fin de la clause */
                clause.add(literal);
                literal = scan.nextInt();
            }
        }
        return formule;
    }

    /** Génère une formule tautologique
     * La "taille" de celle-ci est équivalente à 2*nbOfVariables,
     * étant donné que ceux-ci n'apparaissent que 2 fois dans la formule
     * (1 littéral positif et 1 littéral négatif)
     *
     * @param nbOfVariables Le nombre de variables composant la formule
     * @return La formule tautologique générée
     */
    static public Formule generateTautology(int nbOfVariables) {
        if (nbOfVariables < 0)
            throw new IllegalArgumentException();

        int nbOfClauses = nbOfVariables;

        Formule formule = new Formule(nbOfClauses);
        for (int clauseNb = 1; clauseNb <= nbOfClauses; clauseNb++) {
            int literal = clauseNb;
            ArrayList<Integer> clause = new ArrayList<>(2);
            clause.add(literal);
            clause.add(-literal);

            formule.addClause(clause);
        }

        return formule;
    }

    /** Génère une formule impossible à satisfaire
     * (je ne suis pas certain que l'on appelle ce type de formule une
     * contradiction, à vérifier)
     * La taille est équivalente au nombre de littéraux, donc ici 2*nbOfVariables
     * (cf. javadoc de generateTautology)
     *
     * @param nbOfVariables Le nombre de variables composant la formule*
     * @return La formule générée
     */
    static public Formule generateContradiction(int nbOfVariables) {
        if (nbOfVariables < 0)
            throw new IllegalArgumentException();

        int nbOfLiterals = nbOfVariables * 2;
        int nbOfClauses = nbOfLiterals;

        Formule formule = new Formule(nbOfClauses);
        for (int variable = 1; variable <= nbOfVariables; variable++) {
            int literal = variable;

            /* Clause contenant le littéral positif */
            ArrayList<Integer> clausePositive = new ArrayList<>(1);
            clausePositive.add(literal);
            formule.addClause(clausePositive);

            /* Clause contenant le littéral négatif */
            ArrayList<Integer> clauseNegative = new ArrayList<>(1);
            clauseNegative.add(-literal);
            formule.addClause(clauseNegative);
        }

        return formule;
    }

    /** Vérifie si cette formule est satisfaite par l'affectation donnée en paramètre
     *  Correspond à la première partie du TP
     *
     * @param affectations Les affectations à tester
     * @return true si affectations satisfait la formule, false sinon
     */
    public boolean verify(Verificateur.Affectations affectations) {
        clauseIter:
        for (ArrayList<Integer> clause : this) {
            for (int literal : clause) {
                if (affectations.contains(literal))
                    continue clauseIter;
            }
            return false;
        }
        return true;
    }
}
