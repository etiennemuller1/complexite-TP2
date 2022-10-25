import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Verificateur {
    int nbVariables;
    int nbClauses;
    ArrayList<ArrayList<Integer>> clauses;
    ArrayList<Integer> affectations;
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
        affectations= new ArrayList<>();
        while(scannerVerif.hasNext())
        {
            affectations.add(scannerVerif.nextInt());
        }
    }
    public boolean verifier()
    {
        clauseIter:
        for (ArrayList<Integer> clause: clauses)
        {
            for (int litteral:clause)
            {
                if(affectations.contains(litteral))
                {
                    continue clauseIter;
                }
            }
            return false;
        }
        return true;
    }
}