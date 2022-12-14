package Performance;

import main.Formule;
import main.Stable;
import main.Sudoku;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import static Performance.Performance.NANOSECONDS_TO_SECONDS;
import static Performance.Performance.getMean;
import static main.Main.KISSAT_EXEC_PATH;

public class PerfSudoku {

    /** Calcule les performances des algorithmes liés aux sudokus
     *
     * @param upTo Jusqu'à quelle taille tester, inclus
     * @param nbOfMeasures Le nombre de mesures effectuées par taille
     * @return
     */
    public static Double[][] getSudokuPerformance(int upTo, int nbOfMeasures) {
        final int NB_OF_ALGO = 2;
        final int SUDOKU_DENSITY = 5;

        upTo++; /* Afin d'inclure upTo dans les calculs */
        Double[][] performance = new Double[NB_OF_ALGO][upTo];

        /*FIXME: Les algos sudoku ne fonctionnent pas sur des sudoku de taille
        *  inférieure à 3, ceci est donc un garde-fou temporaire pour ne pas crasher le programme */
        for (int algo = 0; algo < NB_OF_ALGO; algo++) {
            for (int i = 0; i < 3; i++) {
                performance[algo][i] = 0.0;
            }
        }

        for (int size = 3; size < upTo; size++) {
            Double[][] measures = new Double[NB_OF_ALGO][nbOfMeasures]; /* Les différentes mesures pour le même index */

            /* On procède à plusieurs mesures afin de lisser la courbe
             * et mieux capturer sa "tendance" */
            for (int measure = 0; measure < nbOfMeasures; measure++) {
                Sudoku sudoku = Sudoku.createRandomSudoku(size, SUDOKU_DENSITY);
                Formule formule;
                Formule[] formuleContainer = new Formule[1];

                measures[0][measure] = (double) reductionPerf.apply(sudoku, formuleContainer) / NANOSECONDS_TO_SECONDS;
                formule = formuleContainer[0];

                measures[1][measure] = (double) solverPerf.apply(formule) / NANOSECONDS_TO_SECONDS;
            }
            performance[0][size] = getMean(measures[0]);
            performance[1][size] = getMean(measures[1]);
        }

        return performance;
    }

    /** Teste la performance de la réduction, et retourne également la formule générée
     * sudoku -> Le sudoku à réduire
     * formulaContainer -> La formule à retourner. On utilise un tableau de taille 1
     *                     afin de passer la formule par référence, et pas par valeur comme Java
     *                     le fait habituellement
     * retourne -> Le temps mis pour la réduction, en nanosecondes
     */
    private final static BiFunction<Sudoku, Formule[], Long> reductionPerf = (sudoku, formulaContainer) -> {
        Instant before, after;
        Formule formule;

        before = Instant.now();
        formule = sudoku.toSAT();
        after = Instant.now();

        formulaContainer[0] = formule; /* On insère la formule dans le conteneur */

        return before.until(after, ChronoUnit.NANOS);
    };

    /** Teste la performance du solveur sur la formule donnée
     * formule -> La formule à résoudre
     * retourne -> Le temps mis pour la résolution, en nanosecondes
     */
    private final static Function<Formule, Long> solverPerf = (formule) -> {
        Instant before, after;
        ProcessBuilder kissatBuilder = new ProcessBuilder(KISSAT_EXEC_PATH, "tmp_formula.txt", "--relaxed");
        kissatBuilder.inheritIO();

        formule.createCNFFile("tmp_formula.txt");

        before = Instant.now();
        try {
            Process kissat = kissatBuilder.start();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'exéuction de kissat.");
        }
        after = Instant.now();

        return before.until(after, ChronoUnit.NANOS);
    };

    public static void exportSudokuPerformance(int upTo, int nbOfMeasures) {
        Double[][] performance = PerfSudoku.getSudokuPerformance(upTo, nbOfMeasures);
        Performance.exportFile(performance[0], "SudokuReduct" + upTo + "_" + nbOfMeasures + ".txt");
        Performance.exportFile(performance[1], "SudokuSolver" + upTo + "_" + nbOfMeasures + ".txt");
    }
}
