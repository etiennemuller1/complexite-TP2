package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Verificateur {

    public static class Affectations {
        BitSet litteraux;   /* Stocke les assignations des variables */

        public Affectations() {
            litteraux = new BitSet();
        }

        public Affectations(int nbOfLiterals) {
            litteraux = new BitSet(nbOfLiterals);
        }

        /** Assigne la valeur vraie au littéral donné
         * @param literal Le littéral, sous la forme :
         *                  * positif :  numVariable
         *                  * négatif : -numVariable
         */
        public void addLiteral(int literal) {
            if (literal == 0)
                throw new IllegalArgumentException("Le littéral 0 n'a pas de sens.");

            boolean value = literal > 0;  /* Assigne-t-on à vrai ou à faux ? */

            if (!value)
                literal = -literal;

            litteraux.set(literal, value);
        }

        /** Retourne la valeur du littéral donné
         *
         * @param literal Le littéral dont on souhaite connaitre la valeur,
         *                à donner sous la forme :
                            * positif :  numVariable
         *                  * négatif : -numVariable
         * @return La valeur du littéral
         */
        public boolean contains(int literal) {
            if (literal == 0)
                throw new IllegalArgumentException("Le littéral 0 n'a pas de sens.");

            boolean value = literal > 0;  /* S'intéresse-t-on au littéral vrai ou faux ? */
            if (value) /* On veut le littéral positif */
                return litteraux.get(literal);
            else       /* On veut le littéral négatif */
                literal = -literal;
                return !litteraux.get(literal);
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
            Affectations affectations = new Affectations(size+1);
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
            Affectations affectations = new Affectations(size+1);
            for (int variable = 1; variable <= size; variable++)
                affectations.addLiteral(-variable);

            return affectations;
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

    @Override
    public String toString() {
        return formule.toString() + affectations.toString();
    }
}