package Performance;

import main.Stable;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.IntToLongFunction;

public class Performance {

    private final static int NANOSECONDS_TO_SECONDS = 1_000_000_000;

    /** Calcule les performances d'un algorithme
     *
     * @param upTo Jusqu'à quelle taille tester, inclus
     * @param nbOfMeasures Le nombre de mesures effectuées par taille
     * @param func Le testeur d'algorithme à utiliser, prenant la taille de entrée à générer pour les tests et
     *             retournant le temps nécessaire pour exécuter l'algorithme sur cette entrée générée
     * @return
     */
    public static Double[] getPerformance(int upTo, int nbOfMeasures, IntToLongFunction func) {
        upTo++; /* Afin d'inclure upTo dans les calculs */
        Double[] performance = new Double[upTo];

        for (int size = 0; size < upTo; size++) {
            Double[] measures = new Double[nbOfMeasures]; /* Les différentes mesures pour le même index */

            /* On procède à plusieurs mesures afin de lisser la courbe
             * et mieux capturer sa "tendance" */
            for (int measure = 0; measure < nbOfMeasures; measure++) {
                measures[measure] = (double) func.applyAsLong(size) / NANOSECONDS_TO_SECONDS;
            }
            performance[size] = getMean(measures);
        }

        return performance;
    }

    final static private double PERF_GRAPH_DENSITY = 0.7;
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

        return getPerformance(upTo, nbOfMeasures, func);
    }

    /** Calcule la moyenne de toutes les valeurs du tableau array
     *
     * @param array Le tableau
     * @return La moyenne de toutes les valeurs contenues dans array
     */
    private static Double getMean(Double[] array) {
        Double total = 0.0;
        for (Double d: array) {
            total += d;
        }
        return total / array.length;
    }

    /** Exporte le tableau de benchmark sous forme écrite,
     * dans un fichier
     * @param performance Le tableau comportant les résultats des tests
     * @param filename Le nom du fichier où sera exporté les résultats
     */
    public static void exportFile(Double[] performance, String filename) {
        FileWriter writer;
        try {
            writer = new FileWriter(filename, false);
        } catch (IOException e) {
            System.out.println("Error when exporting the benchmark file.");
            return;
        }

        try {
            for (Double d : performance) {
                writer.write(d.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error when writing to the benchmark file.");
            return;
        }

        try { writer.close(); } catch (IOException e) {
            System.out.println("Error when closing the writer.");
        }
    }

}
