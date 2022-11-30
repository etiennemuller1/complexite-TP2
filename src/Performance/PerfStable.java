package Performance;

import main.Formule;
import main.Stable;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.BiFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.Function;

import static Performance.Performance.NANOSECONDS_TO_SECONDS;
import static Performance.Performance.getMean;
import static main.Main.KISSAT_EXEC_PATH;

public class PerfStable {
    final static private double PERF_GRAPH_DENSITY = 0.7;
    final static private int STABLE_SIZE = 2;

    /* Variable utilisée par get3DStablePerformance pour modifier le comportement de reductionPerf
    * par effet de bord… C'est vraiment pas joli-joli, mais ça devrait marcher */
    static private int STABLE_SIZE_GLOBAL = 5;

    /** Calcule les performances des algorithmes liés aux graphes
     *
     * @param upTo Jusqu'à quelle taille tester, inclus
     * @param nbOfMeasures Le nombre de mesures effectuées par taille
     * @return
     */
    public static Double[][] getStablePerformance(int upTo, int nbOfMeasures) {
        upTo++; /* Afin d'inclure upTo dans les calculs */
        Double[][] performance = new Double[3][upTo];

        for (int graphSize = 0; graphSize < upTo; graphSize++) {
            Double[][] measures = new Double[3][nbOfMeasures]; /* Les différentes mesures pour le même index */

            /* On procède à plusieurs mesures afin de lisser la courbe
             * et mieux capturer sa "tendance" */
            for (int measure = 0; measure < nbOfMeasures; measure++) {
                Stable.Graph graph = Stable.Graph.generateRandomGraph(graphSize, PERF_GRAPH_DENSITY);
                Formule formule;
                Formule[] formuleContainer = new Formule[1];

                measures[0][measure] = (double) reductionPerf.apply(graph, formuleContainer) / NANOSECONDS_TO_SECONDS;
                formule = formuleContainer[0];

                measures[1][measure] = (double) bruteForcePerf.apply(formule) / NANOSECONDS_TO_SECONDS;
                measures[2][measure] = (double) solverPerf.apply(formule) / NANOSECONDS_TO_SECONDS;
            }
            performance[0][graphSize] = getMean(measures[0]);
            performance[1][graphSize] = getMean(measures[1]);
            performance[2][graphSize] = getMean(measures[2]);
        }

        return performance;
    }

    /** Calcule les performances des algorithmes liés aux graphes, en fonction de la taille du graphe et de la zone vide
     *
     * @param upTo Jusqu'à quelle taille tester, inclus
     * @param nbOfMeasures Le nombre de mesures effectuées par taille
     * @return
     */
    public static Double[][][] get3DStablePerformance(int upTo, int nbOfMeasures) {
        upTo++; /* Afin d'inclure upTo dans les calculs */
        Double[][][] performance = new Double[3][upTo][];

        for (int graphSize = 0; graphSize < upTo; graphSize++) {
            Double[][] measures = new Double[3][nbOfMeasures]; /* Les différentes mesures pour le même index */

            performance[0][graphSize] = new Double[graphSize+1];
            performance[1][graphSize] = new Double[graphSize+1];
            performance[2][graphSize] = new Double[graphSize+1];

            System.out.print("Taille graphe : " + graphSize + "/" +  upTo + " ");
            for (int stableSize = 0; stableSize <= graphSize; stableSize++) {
                STABLE_SIZE_GLOBAL = stableSize;

                System.out.print("#");

                /* On procède à plusieurs mesures afin de lisser la courbe
                 * et mieux capturer sa "tendance" */
                for (int measure = 0; measure < nbOfMeasures; measure++) {
                    Stable.Graph graph = Stable.Graph.generateRandomGraph(graphSize, PERF_GRAPH_DENSITY);
                    Formule formule;
                    Formule[] formuleContainer = new Formule[1];

                    measures[0][measure] = (double) reductionPerf.apply(graph, formuleContainer) / NANOSECONDS_TO_SECONDS;
                    formule = formuleContainer[0];

                    measures[1][measure] = (double) bruteForcePerf.apply(formule) / NANOSECONDS_TO_SECONDS;
                    measures[2][measure] = (double) solverPerf.apply(formule) / NANOSECONDS_TO_SECONDS;
                }
                performance[0][graphSize][stableSize] = getMean(measures[0]);
                performance[1][graphSize][stableSize] = getMean(measures[1]);
                performance[2][graphSize][stableSize] = getMean(measures[2]);
            }
            System.out.println();
        }

        return performance;
    }

    /** Exporte un fichier de performance avec 2 variables
     *
     * @param performance Un tableau dont les dimensions sont les suivantes :
     *                      * 1er indice: Taille max du graphe
     *                      * 2ème indice: Taille max des stables
     * @param filename
     */
    public static void export3DFile(Double[][] performance, String filename) {
        FileWriter writer;
        try {
            writer = new FileWriter(filename, false);
        } catch (IOException e) {
            System.out.println("Error when exporting the benchmark file.");
            return;
        }

        try {
            for (int graphSize=0; graphSize<performance.length; graphSize++) {
                Double[] graphSizePerf = performance[graphSize];

                for (int stableSize = 0; stableSize <= graphSize; stableSize++) {
                    writer.write(graphSize + " " + stableSize + " " +
                                 graphSizePerf[stableSize] + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error when writing to the benchmark file.");
            return;
        }

        try { writer.close(); } catch (IOException e) {
            System.out.println("Error when closing the writer.");
        }
    }

    public static void exportStablePerformance(int upTo, int nbOfMeasures) {
        Double[][] performance = PerfStable.getStablePerformance(upTo, nbOfMeasures);
        Performance.exportFile(performance[0], "StableReduct" + upTo + "_" + nbOfMeasures + ".txt");
        Performance.exportFile(performance[1], "StableBruteForce" + upTo + "_" + nbOfMeasures + ".txt");
        Performance.exportFile(performance[2], "StableSolver" + upTo + "_" + nbOfMeasures + ".txt");
    }

    public static void export3DStablePerformance(int upTo, int nbOfMeasures) {
        Double[][][] performance = PerfStable.get3DStablePerformance(upTo, nbOfMeasures);
        export3DFile(performance[0], "Stable3DReduct" + upTo + "_" + nbOfMeasures + ".txt");
        export3DFile(performance[1], "Stable3DBruteForce" + upTo + "_" + nbOfMeasures + ".txt");
        export3DFile(performance[2], "Stable3DSolver" + upTo + "_" + nbOfMeasures + ".txt");
    }

    /** Teste la performance de la réduction, et retourne également la formule générée
     * graph -> Le graphe à réduire
     * formulaContainer -> La formule à retourner. On utilise un tableau de taille 1
     *                     afin de passer la formule par référence, et pas par valeur comme Java
     *                     le fait habituellement
     * retourne -> Le temps mis pour la réduction, en nanosecondes
     */
    private final static BiFunction<Stable.Graph, Formule[], Long> reductionPerf = (graph, formulaContainer) -> {
        Instant before, after;
        Stable stable = new Stable(graph, STABLE_SIZE_GLOBAL);
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
    private final static Function<Formule, Long> bruteForcePerf = (formule) -> {
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
    private final static Function<Formule, Long> solverPerf = (formule) -> {
        Instant before, after;
        ProcessBuilder kissatBuilder = new ProcessBuilder(KISSAT_EXEC_PATH, "tmp_formula.txt", "--relaxed");
        //kissatBuilder.inheritIO();

        formule.createCNFFile("tmp_formula.txt");

        before = Instant.now();
        try {
            Process kissat = kissatBuilder.start();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'exéuction de kissat.");
        }        after = Instant.now();

        return before.until(after, ChronoUnit.NANOS);
    };
}
