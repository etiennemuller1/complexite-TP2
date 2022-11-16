package main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Sudoku {

    boolean[][][] variables;
    int[][] grille;
    Values[] lignes;
    Values[] colonnes;
    Values[] regions;
    int taille;
    int tailleCarre;
    public Sudoku(String sudokuFileName) throws FileNotFoundException {

        File f = new File(sudokuFileName);
        Scanner scanner = new Scanner(f);
        taille = scanner.nextInt();
        tailleCarre = taille * taille;
        lignes = new Values[tailleCarre];
        colonnes = new Values[tailleCarre];
        regions = new Values[tailleCarre];
        grille = new int[tailleCarre][tailleCarre];
        variables = new boolean[tailleCarre][tailleCarre][tailleCarre];
        for (int i = 0;i<tailleCarre;i++)
        {
            for (int j = 0;j<tailleCarre;j++)
            {
                for (int k = 0;k<tailleCarre;k++)
                {
                    variables[i][j][k] = false;
                }
            }
        }

        for(int i = 0; i<tailleCarre;i++)
        {
            lignes[i]=new Values();
            colonnes[i] = new Values();
            regions[i] = new Values();
        }

        for(int i = 0; i<tailleCarre;i++)
        {
            for(int j = 0; j<tailleCarre;j++)
            {
                int value = scanner.nextInt();
                grille[j][i] = value;
                if(value!=-0)
                {
                    lignes[i].add(value);
                    colonnes[j].add(value);
                    regions[j/taille+3*i/taille-1].add(value);
                    variables[i][j][value-1]=true;
                }
            }
        }
    }

    public void print()
    {
        System.out.println(taille);
        for(int i = 0; i<tailleCarre;i++)
        {
            for(int j = 0; j<tailleCarre;j++)
            {
                System.out.print(grille[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void toSAT() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("sudokuSat");
        //chaque case ne peut pas avoir plus d'une valeur
        for (int i =0;i<tailleCarre;i++)
        {
            for(int j = 0;j<tailleCarre;j++)
            {
                if(grille[i][j]==0)
                {
                    for (int k = 0; k<tailleCarre;k++)
                    {
                        for (int k2 = k+1; k2 < tailleCarre; k2++)
                        {
                                System.out.println("case" + (i+1) + "," + (j+1) + " : " + "-"+i + "" + j +""+ (k + 1) + " " + "-" + i + "" + j +""+ (k2 + 1) );
                            }

                    }
                }
                else
                {
                    System.out.println(i +""+j+""+grille[i][j]);
                }
            }
        }
        //chaque colonne ne peut pas avoir une même valeur sur plus d'une case
        for (int j =0;j<tailleCarre;j++)
        {
            for(int k = 0;k<tailleCarre;k++)
            {
                    for (int i = 0; i<tailleCarre;i++)
                    {
                        for (int i2 = i+1; i2 < tailleCarre; i2++)
                        {
                            System.out.println("colonne" + (j+1) + " : " + "-"+i + "" + j +""+ (k + 1) + " " + "-" + i2 + "" + j +""+ (k + 1) );
                        }

                    }
            }
        }
        //chaque colonne ne peut pas avoir une même valeur sur plus d'une case
        for (int i =0;i<tailleCarre;i++)
        {
            for(int k = 0;k<tailleCarre;k++)
            {
                for (int j = 0; j<tailleCarre;j++)
                {
                    for (int j2 = j+1; j2 < tailleCarre; j2++)
                    {
                        System.out.println("ligne" + (i+1) + " : " + "-"+i + "" + j +""+ (k + 1) + " " + "-" + i + "" + j2 +""+ (k + 1) );
                    }

                }
            }
        }
        for(int z = 0; z<taille;z++)
        {
            for(int w = 0; w<taille;w++)
            {
                for(int k = 0;k<tailleCarre;k++)
                {
                    for (int i =z*taille;i<(z+1)*taille;i++) {
                        for (int i2 =i;i2<(z+1)*taille;i2++) {
                          for (int j = w * taille; j < (w + 1) * taille; j++) {
                              for (int j2 = j; j2 < (w + 1) * taille; j2++) {
                                if (i != i2 || j != j2){
                                System.out.println("zone" + (z + 1) + "," + (w + 1) + " : " + "-" + i + "" + j + "" + (k + 1) + " " + "-" + i2 + "" + j2 + "" + (k + 1));
                                }
                              }
                          }
                        }
                    }
                }
            }
        }
    }
}
