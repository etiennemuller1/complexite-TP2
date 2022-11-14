package main;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
/*        main.Verificateur verificateur = new main.Verificateur("path");
        System.out.println(verificateur.formule);
        System.out.println(verificateur.affectations);
        System.out.println(verificateur.verifier());*/
        //System.out.println(Verificateur.Formule.generateTautology(10));
        //System.out.println(Verificateur.Formule.generateContradiction(10));
        main.Sudoku sudoku = new main.Sudoku("/amuhome/m16014784/Bureau/Reduction/src/sudoku.txt");
        sudoku.print();
    }
}
