import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public void toSAT()
    {
        for (int i =0;i<tailleCarre;i++)
        {
            for(int j = 0;j<tailleCarre;j++)
            {
            }
        }
    }
}
