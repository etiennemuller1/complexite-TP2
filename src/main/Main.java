package main;

import Performance.*;

import java.io.FileNotFoundException;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        /*Verificateur verificateur = new Verificateur("/amuhome/m16014784/Bureau/Reduction/src/DIMACSCNF.txt",
                "/amuhome/m16014784/Bureau/Reduction/src/Affectation.txt");
        System.out.println(verificateur.clauses);
        System.out.println(verificateur.affectations);
        System.out.println(verificateur.verifier());*/
        //Sudoku sudoku = new Sudoku("/amuhome/m16014784/Bureau/Reduction/src/sudoku.txt");
        //sudoku.toSAT();
        performance();
    }

    public static void performance() {
        int nbOfMeasures = 200;
        int upTo = 5_000;

        Performance.exportFile(PerfVerif.getVerifTautologyPerf(upTo, nbOfMeasures),
                               "VerifTaut" + upTo + "_" + nbOfMeasures + ".txt");
        Performance.exportFile(PerfVerif.getVerifContradictionPerf(upTo, nbOfMeasures),
                "VerifContra" + upTo + "_" + nbOfMeasures + ".txt");
    }
}
