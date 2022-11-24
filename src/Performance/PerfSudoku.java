package Performance;

import main.Formule;
import main.Stable;
import main.Sudoku;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import static Performance.Performance.NANOSECONDS_TO_SECONDS;
import static Performance.Performance.getMean;

public class PerfSudoku {

    /** Calcule les performances des algorithmes liés aux sudokus
     *
     * @param upTo Jusqu'à quelle taille tester, inclus
     * @param nbOfMeasures Le nombre de mesures effectuées par taille
     * @return
     */
    public static Double[][] getSudokuPerformance(int upTo, int nbOfMeasures) {
        final int NB_OF_ALGO = 2;
        final int SUDOKU_DENSITY = 30;

        upTo++; /* Afin d'inclure upTo dans les calculs */
        Double[][] performance = new Double[NB_OF_ALGO][upTo];

        for (int size = 0; size < upTo; size++) {
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
    private static BiFunction<Sudoku, Formule[], Long> reductionPerf = (sudoku, formulaContainer) -> {
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
    private static Function<Formule, Long> solverPerf = (formule) -> {
        Instant before, after;

        before = Instant.now();
        /* TODO */
        after = Instant.now();

        return before.until(after, ChronoUnit.NANOS);
    };

    public static void exportStablePerformance(int upTo, int nbOfMeasures) {
        Double[][] performance = PerfStable.getStablePerformance(upTo, nbOfMeasures);
        Performance.exportFile(performance[0], "SudokuReduct" + upTo + "_" + nbOfMeasures + ".txt");
        Performance.exportFile(performance[1], "SudokuSolver" + upTo + "_" + nbOfMeasures + ".txt");
    }
}
