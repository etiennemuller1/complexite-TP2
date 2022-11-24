package main;

import Performance.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        /*Verificateur verificateur = new Verificateur("/amuhome/m16014784/Bureau/Reduction/SudokuSat.txt",
                "/amuhome/m16014784/Bureau/Reduction/src/Affectation.txt");
        System.out.println(verificateur.verifier());*/
        //Sudoku sudoku = Sudoku.createSudokuFromFile("src/sudoku.txt");
        Sudoku randomSudoku = Sudoku.createRandomSudoku(4,25);
        randomSudoku.toSAT("RandomSudokuCNF.txt");
        //System.out.println(sudoku.isSolutionGood("src/SudokuSolved.txt"));
        //performanceVerif();
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
}
