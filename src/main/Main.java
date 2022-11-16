package main;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        /*Verificateur verificateur = new Verificateur("/amuhome/m16014784/Bureau/Reduction/SudokuSat.txt",
                "/amuhome/m16014784/Bureau/Reduction/src/Affectation.txt");
        System.out.println(verificateur.verifier());*/
        Sudoku sudoku = new Sudoku("/amuhome/m16014784/Bureau/Reduction/src/sudoku.txt");
        sudoku.toSAT();
    }
}
