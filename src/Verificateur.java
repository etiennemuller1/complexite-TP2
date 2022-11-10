import java.io.File;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class Verificateur {

    public static class Affectations {
        TreeSet<Integer> litteraux = new TreeSet<>();

        /** Assigne la valeur vraie au littéral donné
         * @param litteral Le littéral
         */
        public void addLitteral(int litteral) {
            if (litteral == 0)
                throw new IllegalArgumentException("Le littéral 0 n'a pas de sens.");

            if (litteraux.contains(-litteral))
                throw new IllegalArgumentException("Le littéral opposé est déjà assigné à vrai.");

            litteraux.add(litteral);
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
    }

    /** Représente une formule de type SAT, composée de clauses */
    public static class Formule {
        ArrayList<ArrayList<Integer>> clauses;

        public Formule() {
            this.clauses = new ArrayList<>();
        }

        public Formule(int nbClauses) {
            this.clauses = new ArrayList<>(nbClauses);
        }

        /** Rajoute une clause dans la formule
         *
         * @param clause La clause, sous forme de liste de littéraux la composant
         *               On suppose qu'elle est bien formulé (e.g. il n'y a pas de littéral 0)
         */
        public void addClause(ArrayList<Integer> clause) {
            clauses.add(clause);
        }
    }

    int nbVariables;
    int nbClauses;
    ArrayList<ArrayList<Integer>> clauses;
    Affectations affectations;
    public Verificateur(String fileSourceCNF, String fileSourceVerif) throws FileNotFoundException
    {
        lectureCNF(fileSourceCNF);
        lectureVerif(fileSourceVerif);
    }
    private void lectureCNF(String fileSourceCNF) throws FileNotFoundException {
        File docCNF = new File(fileSourceCNF);
        Scanner scannerCNF = new Scanner(docCNF);
        scannerCNF.next();
        scannerCNF.next();
        nbVariables = scannerCNF.nextInt();
        nbClauses = scannerCNF.nextInt();
        clauses = new ArrayList<>();
        for(int i = 0; i<nbClauses;i++)
        {
            clauses.add(new ArrayList<>());
        }
        for(int i = 0;i<nbClauses;i++)
        {
            int literalTemp = scannerCNF.nextInt();
            while(literalTemp!=0)
            {
                clauses.get(i).add(literalTemp);
                literalTemp=scannerCNF.nextInt();
            }
        }
    }
    private void lectureVerif(String fileSourceVerif) throws FileNotFoundException {
        File docVerif = new File(fileSourceVerif);
        Scanner scannerVerif = new Scanner(docVerif);
        affectations= new Affectations();
        while(scannerVerif.hasNext())
        {
            affectations.addLitteral(scannerVerif.nextInt());
        }
    }
    public boolean verifier()
    {
        clauseIter:
        for (ArrayList<Integer> clause: clauses)
        {
            for (int litteral:clause)
            {
                if (affectations.contains(litteral))
                {
                    continue clauseIter;
                }
            }
            return false;
        }
        return true;
    }
}