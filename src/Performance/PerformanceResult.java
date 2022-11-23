package Performance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/** Représente une ou plusieurs suites de résultats de test
 *
 * Cette classe est particulièrement utile lorsque l'on souhaite
 * encoder plusieurs résultats de test différents liés entre eux (on les appellera ici des benchmarks)
 * (eg. le temps d'un algo de réduction + le temps mis pour résoudre les pb SAT réduits ) */
public class PerformanceResult {

    /** Représente un benchmark
     * (eg. le temps d'un algo de réduction, et rien d'autre) */
    public static class PerformanceBenchmark {
        private String name;

        public Double[] perf;

        /**
         *
         * @param name Le nom du benchmark
         * @param upTo Jusqu'à quelle taille on peut coder des mesures, exclus
         */
        public PerformanceBenchmark(String name, int upTo) {
            this.name = name;
            this.perf = new Double[upTo];
        }

        public void export() {
            FileWriter writer;
            try {
                writer = new FileWriter(name, false);
            } catch (IOException e) {
                System.out.println("Error when exporting the benchmark file.");
                return;
            }

            try {
                for (Double d : this.perf) {
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

    private int upTo;
    private PerformanceBenchmark[] benchmarks;

    /** Créée un nouveau PerformanceResult
     *
     * @param upTo Jusqu'à quelle taille on peut coder des mesures,
     *             pour tous les benchmarks
     * @param nbOfBenchmarks Le nombre de benchmark
     * @param names Tableau contenant le nom des différents benchmarks, dans l'ordre
     *              Doit avoir nbOfBenchmarks comme taille
     */
    public PerformanceResult(int upTo, int nbOfBenchmarks, String[] names) {
        if (names.length != nbOfBenchmarks)
            throw new IllegalArgumentException("Il y a trop/pas assez de noms pour les benchmarks.");

        this.upTo = upTo;
        this.benchmarks = new PerformanceBenchmark[nbOfBenchmarks];
        for (int i = 0; i < nbOfBenchmarks; i++) {
            this.benchmarks[i] = new PerformanceBenchmark(names[i], upTo);
        }
    }

    public void addMeasure(int index, Double[] measures) {
        if (measures.length != this.benchmarks.length)
            throw new IllegalArgumentException("Pas assez/trop de mesures pour cette instance.");

        /* On itère sur tous les benchmark */
        for (int bench = 0; bench < benchmarks.length; bench++) {
            benchmarks[bench].perf[index] = measures[bench];
        }
    }

    /** Exporte tous les benchmarks contenus dans cette instance */
    public void export() {
        for (PerformanceBenchmark bench : this.benchmarks)
            bench.export();
    }
}
