package main;


import java.io.*;
import java.util.*;

public class Sudoku {

    int[][][] numeroVariables;
    HashMap<Integer, int[]> variableNumero = new HashMap<>();
    int[][] grille;
    int taille;
    int tailleCarre;

    /**Créer un sudoku à partir d'un fichier
     * @param sudokuFileName chemin du fichier txt à partir duquel il faut créer le sudoku
     * @return le sudoku souhaité
     * **/
    public static Sudoku createSudokuFromFile(String sudokuFileName) throws FileNotFoundException {

        File f = new File(sudokuFileName);
        Scanner scanner = new Scanner(f);
        int taille = scanner.nextInt();
        Sudoku sudoku = new Sudoku(taille);

        for (int i = 0; i < sudoku.tailleCarre; i++) {
            for (int j = 0; j < sudoku.tailleCarre; j++) {
                int value = scanner.nextInt();
                sudoku.setGrilleValue(i,j,value);
            }
        }
        sudoku.print(sudoku.grille);
        return sudoku;
    }

    /** Créer un Sudoku aléatoire pas forcément solvable
     * @param taille La taille du sudoku souhaité
     * @param densité la densité de chiffre à placer dans le sudoku en pourcentage
     * @return le sudoku souhaité
     * **/
    public static Sudoku createRandomSudoku(int taille, int densité)
    {
        Sudoku sudoku = new Sudoku(taille);
        Random random = new Random();

        for (int i = 0; i < sudoku.tailleCarre; i++) {
            for (int j = 0; j < sudoku.tailleCarre; j++) {
                int r = random.nextInt(100);
                if(r<densité)
                {
                    int value = random.nextInt(9);
                    sudoku.setGrilleValue(i,j,value);
                }
            }
        }
        sudoku.print(sudoku.grille);
        return sudoku;
    }

    /** Créer un sudoku et l'affiche
     * @param taille La taille du sudoku souhaité
     * **/
    public Sudoku(int taille)
    {
        this.taille = taille;
        tailleCarre = taille * taille;
        grille = new int[tailleCarre][tailleCarre];
        numeroVariables = new int[tailleCarre][tailleCarre][tailleCarre];

        int numeroVariable = 1;
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                for (int k = 0; k < tailleCarre; k++) {
                    variableNumero.put(numeroVariable, new int[]{i, j, k});
                    numeroVariables[i][j][k] = numeroVariable++;
                }
            }
        }
    }

    /**Affiche le sudoku
     * @param grille grille du sudoku
     * **/
    public void print(int[][] grille) {
        System.out.println(taille);
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                System.out.print(grille[j][i] + " ");
            }
            System.out.println();
        }
    }

    /**
     * @param i position i de la valeur
     * @param j position j de la valeur
     * @param value valeur à affecter
     * **/
    public void setGrilleValue(int i,int j, int value)
    {
        grille[j][i]=value;
    }

    /**créer un fichier SudokuCNF.txt dans le dossier src
     * @param fileName nom du fichier SAT à créer
     * **/
    public void toSAT(String fileName) throws IOException {

        Formule cnf = new Formule();



        /**(1)chaque case doit avoir au moins une valeur
         * Theta(n^6)
         * **/
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                Clause clause = new Clause();
                for (int k = 0; k < tailleCarre; k++) {
                    clause.add(numeroVariables[i][j][k]);
                }
                cnf.addClause(clause);
            }
        }

        /**(2)chaque case ne peut pas avoir plus d'une valeur
         * Theta(n^8)
         * **/
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


        /**(3)chaque colonne ne peut pas avoir une même valeur sur plus d'une case
         * Theta(n^8)
         * **/
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
        /**(4)chaque ligne ne peut pas avoir une même valeur sur plus d'une case
         * Theta(n^8)
         * **/
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

        /**(5)chaque zone ne peut pas avoir une même valeur sur plus d'une case
         * Theta(n^8)
         * **/
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

        /**(6)on affecte les valeurs deja connue
         * Theta(n^4)
         * **/
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                if (grille[i][j] != 0) {
                    Clause clause = new Clause();
                    clause.add(numeroVariables[i][j][grille[i][j] - 1]);
                    cnf.addClause(clause);
                }
            }
        }

        cnf.createCNFFile(fileName, numeroVariables[tailleCarre - 1][tailleCarre - 1][tailleCarre - 1]);
    }

    /**
     * @param solvedSudokuFileName chemin du fichier a vérifié
     * @return true si le sudoku est bien solution du sudoku initial et qu'il respecte bel et bien les règles du sudoku**/
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

        print(solvedGrid);

        /**On vérifie si la solution est bien celle du sudoku demandé**/
        for (int i = 0; i < tailleCarre; i++) {
            for (int j = 0; j < tailleCarre; j++) {
                if (grille[i][j] != 0) {
                    if (solvedGrid[i][j] != grille[i][j]) {
                        return false;
                    }
                }
            }
        }

        /**On vérifie s'il n'y a pas 2 fois la meme valeur dans chaque ligne**/
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

        /**On vérifie s'il n'y a pas 2 fois la meme valeur dans chaque colonne**/
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

        /**On vérifie s'il n'y a pas 2 fois la meme valeur dans chaque zone**/
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


