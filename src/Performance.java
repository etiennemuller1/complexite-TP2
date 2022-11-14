import java.io.FileWriter;
import java.io.IOException;

public class Performance {

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
}
