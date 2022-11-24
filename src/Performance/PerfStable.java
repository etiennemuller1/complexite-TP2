package Performance;

import main.Formule;
import main.Stable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntToLongFunction;

import static Performance.Performance.NANOSECONDS_TO_SECONDS;
import static Performance.Performance.getMean;

public class PerfStable {
    final static private double PERF_GRAPH_DENSITY = 0.7;

    /** Calcule les performances des algorithmes liés aux graphes
     *
     * @param upTo Jusqu'à quelle taille tester, inclus
     * @param nbOfMeasures Le nombre de mesures effectuées par taille
     * @return
     */
    public static Double[][] getGraphPerformance(int upTo, int nbOfMeasures) {
        upTo++; /* Afin d'inclure upTo dans les calculs */
        Double[][] performance = new Double[3][upTo];

        for (int size = 0; size < upTo; size++) {
            Double[][] measures = new Double[3][nbOfMeasures]; /* Les différentes mesures pour le même index */

            /* On procède à plusieurs mesures afin de lisser la courbe
             * et mieux capturer sa "tendance" */
            for (int measure = 0; measure < nbOfMeasures; measure++) {
                Stable.Graph graph = Stable.Graph.generateRandomGraph(size, PERF_GRAPH_DENSITY);
                Formule formule;
                Formule[] formuleContainer = new Formule[1];

                measures[0][measure] = (double) reductionPerf.apply(graph, formuleContainer) / NANOSECONDS_TO_SECONDS;
                formule = formuleContainer[0];

                measures[1][measure] = (double) bruteForcePerf.apply(formule) / NANOSECONDS_TO_SECONDS;
                measures[2][measure] = (double) solverPerf.apply(formule) / NANOSECONDS_TO_SECONDS;


            }
            performance[0][size] = getMean(measures[0]);
            performance[1][size] = getMean(measures[1]);
            performance[2][size] = getMean(measures[2]);

        }

        return performance;
    }

    /** Teste la performance de la réduction, et retourne également la formule générée
     * graph -> Le graphe à réduire
     * formulaContainer -> La formule à retourner. On utilise un tableau de taille 1
     *                     afin de passer la formule par référence, et pas par valeur comme Java
     *                     le fait habituellement
     * retourne -> Le temps mis pour la réduction, en nanosecondes
     */
    private static BiFunction<Stable.Graph, Formule[], Long> reductionPerf = (graph, formulaContainer) -> {
        Instant before, after;
        Stable stable = new Stable(graph);
        Formule formule;

        before = Instant.now();
        formule = stable.computeAndGetFormula();
        after = Instant.now();

        formulaContainer[0] = formule;

        return before.until(after, ChronoUnit.NANOS);
    };

    /** Teste la performance de l'algorithme en force brute sur la formule donnée
     * formule -> La formule à résoudre
     * retourne -> Le temps mis pour la résolution, en nanosecondes
     */
    private static Function<Formule, Long> bruteForcePerf = (formule) -> {
        Instant before, after;

        before = Instant.now();
        /* TODO */
        after = Instant.now();

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

    public static Double[] getStablePerf(int upTo, int nbOfMeasures) {
        IntToLongFunction func = (size) -> {
            Instant before, after;
            Stable.Graph graph = Stable.Graph.generateRandomGraph(size, PERF_GRAPH_DENSITY);
            Stable stable = new Stable(graph);

            before = Instant.now();
            stable.computeAndGetFormula();
            after = Instant.now();

            return before.until(after, ChronoUnit.NANOS);
        };

        return Performance.getPerformance(upTo, nbOfMeasures, func);
    }
}
