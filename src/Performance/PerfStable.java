package Performance;

import main.Stable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.IntToLongFunction;

public class PerfStable {
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

        return Performance.getPerformance(upTo, nbOfMeasures, func);
    }
}
