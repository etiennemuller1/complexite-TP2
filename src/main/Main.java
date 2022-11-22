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
        performanceVerif();
    }

    public static void performanceVerif() {
        int nbOfMeasures = 200;
        int upTo = 5_000;

        PerfVerif.exportVerifTautologyPerf(upTo, nbOfMeasures);
        PerfVerif.exportVerifContradictionPerf(upTo, nbOfMeasures);
    }
}
