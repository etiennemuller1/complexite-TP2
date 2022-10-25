import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sudoku {

    int[][] grille;
    public Sudoku(String sudokuFileName) throws FileNotFoundException {
        File f = new File(sudokuFileName);
        Scanner scanner = new Scanner(f);
        grille = new int[9][9];
        for(int i = 0; i<9;i++)
        {
            for(int j = 0; j<9;j++)
            {
                grille[j][i] = scanner.nextInt();
            }
        }
    }

    public void print()
    {
        for(int i = 0; i<9;i++)
        {
            for(int j = 0; j<9;j++)
            {
                System.out.print(grille[j][i] + " ");
            }
            System.out.println();
        }
    }
}
