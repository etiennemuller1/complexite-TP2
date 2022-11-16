package Performance;

import main.Verificateur;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.IntToLongFunction;

public class Performance {

    final static int NANOSECONDS_TO_SECONDS = 1_000_000_000;

    /** Représente un résultat de test de performance */
    public static class PerformanceResult {
        public int[] indexes;
        public double[] results;

        public PerformanceResult(int size) {
            this.indexes = new int[size];
            this.results = new double[size];
        }

        /** Exporte les résultats sous forme de fichier
         * La première colonne représente l'index, la deuxième
         * le temps associé
         *
         * @param filename Le nom du fichier
         */
        public void toFile(String filename) {
            FileWriter writer;
            try {
                writer = new FileWriter(filename, false);
            } catch (IOException e) {
                System.out.println("Error when exporting the benchmark file.");
                return;
            }

            try {
                for (int i = 0; i < indexes.length; i++) {
                    writer.write(indexes[i] + " " + results[i] + "\n");
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

    /** Calcule les performances d'un algorithme
     *
     * @param upTo Jusqu'à quelle taille tester, inclus
     * @param nbOfMeasures Le nombre de mesures effectuées par taille
     * @param func Le testeur d'algorithme à utiliser, prenant la taille de entrée à générer pour les tests et
     *             retournant le temps nécessaire pour exécuter l'algorithme sur cette entrée générée
     * @return
     */
    private static Double[] getPerformance(int upTo, int nbOfMeasures, IntToLongFunction func) {
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

    public static Double[] getVerifTautologyPerf(int upTo, int nbOfMeasures) {
        IntToLongFunction func = (size) -> {
            Instant before, after;
            Verificateur.Formule formule = Verificateur.Formule.generateTautology(size);
            Verificateur.Affectations affectations = Verificateur.Affectations.generateEverythingTrue(size);

            before = Instant.now();
            formule.verify(affectations);
            after = Instant.now();

            return before.until(after, ChronoUnit.NANOS);
        };

        return getPerformance(upTo, nbOfMeasures, func);
    }

    private static Double[] getVerifContradictionPerf(int upTo, int nbOfMeasures) {
        IntToLongFunction func = (size) -> {
            Instant before, after;
            Verificateur.Formule formule = Verificateur.Formule.generateContradiction(size);
            Verificateur.Affectations affectations = Verificateur.Affectations.generateEverythingTrue(size);

            before = Instant.now();
            formule.verify(affectations);
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

}