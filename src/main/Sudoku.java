package main;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Sudoku {

    int[][][] numeroVariables;
    HashMap<Integer, int[]> variableNumero = new HashMap<>();
    boolean[][][] variables;
    int[][] grille;
    int taille;
    int tailleCarre;

    public Sudoku(String sudokuFileName) throws FileNotFoundException {

        File f = new File(sudokuFileName);
        Scanner scanner = new Scanner(f);
        taille = scanner.nextInt();
        tailleCarre = taille * taille;

        grille = new int[tailleCarre][tailleCarre];
        variables = new boolean[tailleCarre][tailleCarre][tailleCarre];
        numeroVariables = new int[tailleCarre][tailleCarre][tailleCarre];


        int numeroVariable = 1;
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                for (int k = 0; k < tailleCarre; k++) {
                    variables[i][j][k] = false;
                    variableNumero.put(numeroVariable, new int[]{i, j, k});
                    numeroVariables[i][j][k] = numeroVariable++;

                }
            }
        }


        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                int value = scanner.nextInt();
                grille[i][j] = value;
                if (value != -0) {
                    variables[i][j][value - 1] = true;
                }
            }
        }
    }

    public void print() {
        System.out.println(taille);
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                System.out.print(grille[j][i] + " ");
            }
            System.out.println();
        }
    }


    public void toSAT() throws IOException {

        Formule cnf = new Formule();

        //on crée une clause pour chaque valeur deja connue
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                if (grille[i][j] != 0) {
                    Clause clause = new Clause();
                    clause.add(numeroVariables[i][j][grille[i][j] - 1]);
                    cnf.addClause(clause);
                }
            }
        }

        //chaque case doit avoir au moins une valeur
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                Clause clause = new Clause();
                for (int k = 0; k < tailleCarre; k++) {
                    clause.add(numeroVariables[i][j][k]);
                }
                cnf.addClause(clause);
            }
        }

        //chaque case ne peut pas avoir plus d'une valeur
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                for (int k = 0; k < tailleCarre; k++) {
                    for (int k2 = k + 1; k2 < tailleCarre; k2++) {
                        Clause clause = new Clause();
                        clause.add(-numeroVariables[i][j][k]);
                        clause.add(-numeroVariables[i][j][k2]);
                        cnf.addClause(clause);
                    }
                }
            }
        }


        //chaque colonne ne peut pas avoir une même valeur sur plus d'une case
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                for (int k = 0; k < tailleCarre; k++) {
                    for (int i2 = i + 1; i2 < tailleCarre; i2++) {
                        Clause clause = new Clause();
                        clause.add(-numeroVariables[i][j][k]);
                        clause.add(-numeroVariables[i2][j][k]);
                        cnf.addClause(clause);
                    }
                }
            }
        }
        //chaque ligne ne peut pas avoir une même valeur sur plus d'une case
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                for (int k = 0; k < tailleCarre; k++) {
                    for (int j2 = j + 1; j2 < tailleCarre; j2++) {
                        Clause clause = new Clause();
                        clause.add(-numeroVariables[i][j][k]);
                        clause.add(-numeroVariables[i][j2][k]);
                        cnf.addClause(clause);
                    }
                }
            }
        }

        //chaque zone ne peut pas avoir une même valeur sur plus d'une case
        for (int z = 0; z < taille; z++) {
            for (int w = 0; w < taille; w++) {
                for (int k = 0; k < tailleCarre; k++) {
                    for (int i = z * taille; i < (z + 1) * taille; i++) {
                        for (int j = w * taille; j < (w + 1) * taille; j++) {
                            for (int i2 = z * taille; i2 < (z + 1) * taille; i2++) {
                                for (int j2 = j; j2 < (w + 1) * taille; j2++) {
                                    if (i != i2 || j != j2) {
                                        Clause clause = new Clause();
                                        clause.add(-numeroVariables[i][j][k]);
                                        clause.add(-numeroVariables[i2][j2][k]);
                                        cnf.addClause(clause);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        cnf.createCNFFile("SudokuCNF.txt", numeroVariables[tailleCarre - 1][tailleCarre - 1][tailleCarre - 1]);
    }

    public boolean isSolutionGood(String solvedSudokuFileName) throws FileNotFoundException {

        File f = new File(solvedSudokuFileName);
        Scanner scanner = new Scanner(f);

        int[][] solvedGrid = new int[tailleCarre][tailleCarre];
        int i0 = 0;
        int j0 = 0;

        while (j0 != tailleCarre) {
            int value = scanner.nextInt();
            if (value > 0) {
                solvedGrid[j0][i0++] = variableNumero.get(value)[2] + 1;
                if (i0 == tailleCarre) {
                    i0 = 0;
                    j0++;
                }
            }
        }

        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                System.out.print(solvedGrid[i][j] + " ");
            }
            System.out.println();
        }

        //On vérifie si la solution est bien celle du sudoku demandé
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                if (grille[i][j] != 0) {
                    if (solvedGrid[i][j] != grille[i][j]) {
                        System.out.println(i + " " + j + " " + solvedGrid[i][j] + " " + grille[i][j]);
                        return false;
                    }
                }
            }
        }

        //On vérifie s'il n'y a pas 2 fois la meme valeur sur chaque ligne
        for (int i = 0; i < tailleCarre; i++) {
            ArrayList<Integer> values = new ArrayList<>();
            for (int j = 0; j < tailleCarre; j++) {
                int value = solvedGrid[i][j];
                if (values.contains(value)) {
                    return false;
                }
                values.add(value);
            }
        }

        //On vérifie s'il n'y a pas 2 fois la meme valeur sur chaque colonne
        for (int j = 0; j < tailleCarre; j++) {
            ArrayList<Integer> values = new ArrayList<>();
            for (int i = 0; i < tailleCarre; i++) {
                int value = solvedGrid[i][j];
                if (values.contains(value)) {
                    return false;
                }
                values.add(value);
            }
        }

        //On vérifie s'il n'y a pas 2 fois la meme valeur dans chaque zone
        for (int z = 0; z < taille; z++) {
            for (int w = 0; w < taille; w++) {
                ArrayList<Integer> values = new ArrayList<>();
                for (int i = z*taille; i < (z+1)*taille; i++) {
                    for (int j = w*taille; j < (w+1)*taille; j++) {
                        int value = solvedGrid[i][j];
                        if (values.contains(value)) {
                            return false;
                        }
                        values.add(value);
                    }
                }
            }
        }
        return true;
    }
}


