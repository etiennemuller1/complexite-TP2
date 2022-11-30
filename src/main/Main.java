package main;

import Performance.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    final static public String KISSAT_EXEC_PATH = "/amuhome/p19020624/Téléchargements/kissat-1.0.3-79d8d8f2/build/kissat";
    public static void main(String[] args) throws FileNotFoundException {
        /*Verificateur verificateur = new Verificateur("/amuhome/m16014784/Bureau/Reduction/src/DIMACSCNF.txt",
                "/amuhome/m16014784/Bureau/Reduction/src/Affectation.txt");
        System.out.println(verificateur.clauses);
        System.out.println(verificateur.affectations);
        System.out.println(verificateur.verifier());*/
        //Sudoku sudoku = new Sudoku("/amuhome/m16014784/Bureau/Reduction/src/sudoku.txt");
        //sudoku.toSAT();
        //performanceVerif();
        performanceSudoku();
    }

    public static void performanceVerif() {
        int nbOfMeasures = 200;
        int upTo = 5_000;

        PerfVerif.exportVerifTautologyPerf(upTo, nbOfMeasures);
        PerfVerif.exportVerifContradictionPerf(upTo, nbOfMeasures);
    }

    public static void performanceStable() {
        int nbOfMeasures = 20;
        int upTo = 200;

        PerfStable.exportStablePerformance(upTo, nbOfMeasures);
    }

    public static void performanceStable3D() {
        int nbOfMeasures = 5;
        int upTo = 60;

        PerfStable.export3DStablePerformance(upTo, nbOfMeasures);

    }

    public static void performanceSudoku() {
        int nbOfMeasures = 30;
        int upTo = 7;

        PerfSudoku.exportSudokuPerformance(upTo, nbOfMeasures);
    }
}
