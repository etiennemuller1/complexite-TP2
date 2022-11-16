package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class Verificateur {

    public static class Affectations {
        TreeSet<Integer> litteraux = new TreeSet<>();

        /** Assigne la valeur vraie au littéral donné
         * @param literal Le littéral
         */
        public void addLiteral(int literal) {
            if (literal == 0)
                throw new IllegalArgumentException("Le littéral 0 n'a pas de sens.");

            if (litteraux.contains(-literal))
                throw new IllegalArgumentException("Le littéral opposé est déjà assigné à vrai.");

            litteraux.add(literal);
        }

        /** Retourne la valeur du littéral donné
         *
         * Attention, si litteral ou son opposé n'ont jamais été rentrés dans
         * cette instance d'Affectation, cette méthode retournera faux dans les deux cas !
         * On compte sur la validité des fichiers d'affectation donnés pour ne pas avoir à gérer
         * ce cas-là pour le moment.
         * @param litteral Le littéral dont on souhaite connaitre la valeur.
         * @return
         */
        public boolean contains(int litteral) {
            return litteraux.contains(litteral);
        }

        public String toString() {
            return litteraux.toString();
        }

        static public Affectations fromFile(String path) throws FileNotFoundException {
            Affectations affectations = new Affectations();
            Scanner scan;

            /* On tente d'ouvrir le fichier */
            scan = new Scanner(new File(path));
            while (scan.hasNext()) {
                affectations.addLiteral(scan.nextInt());
            }
            return affectations;
        }

        /** Génère une affectation où toutes les variables sont vraies
         *
         * @param size Le nombre de variables dans l'affectation
         * @return L'affectation
         */
        static public Affectations generateEverythingTrue(int size) {
            Affectations affectations = new Affectations();
            for (int variable = 1; variable <= size; variable++)
                affectations.addLiteral(variable);

            return affectations;
        }

        /** Génère une affectation où toutes les variables sont fausses
         *
         * @param size Le nombre de variables dans l'affectation
         * @return L'affectation
         */
        static public Affectations generateEverythingFalse(int size) {
            Affectations affectations = new Affectations();
            for (int variable = 1; variable <= size; variable++)
                affectations.addLiteral(-variable);

            return affectations;
        }
    }

    /** Représente une formule de type SAT, composée de clauses */
    public static class Formule implements Iterable<ArrayList<Integer>> {
        private int nbVariables;
        private int nbClauses;
        ArrayList<ArrayList<Integer>> clauses;

        public Formule(int nbClauses, int nbVariables) {
            this.nbClauses = nbClauses;
            this.nbVariables = nbVariables;
            this.clauses = new ArrayList<>(nbClauses);
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
            formule = new Formule(nbClauses, nbVariables);

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

            Formule formule = new Formule(nbOfClauses, nbOfVariables);
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

            Formule formule = new Formule(nbOfClauses, nbOfLiterals);
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

        public boolean verify(Affectations affectations) {
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

    private Formule formule;
    private Affectations affectations;

    public Verificateur(String fileSourceCNF, String fileSourceVerif) throws FileNotFoundException
    {
        formule = Formule.fromFile(fileSourceCNF);
        affectations = Affectations.fromFile(fileSourceVerif);
    }

    public Verificateur(Formule formule, Affectations affectations) {
        this.formule = formule;
        this.affectations = affectations;
    }

    public boolean verifier()
    {
        return formule.verify(affectations);
    }
}