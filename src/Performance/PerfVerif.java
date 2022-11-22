package Performance;

import main.Formule;
import main.Verificateur;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.IntToLongFunction;

/** Teste les performances pour premier mini-projet, le vérificateur de formules */
public class PerfVerif {

    /*NOTE(Axel): On pourrait probablement avoir de bien meilleures performances en évitant de générer la formule
     * et l'affectation à chaque appel de getPerformance, et la compléter petit à petit au fur et à mesure
     * des appels… */

    public static Double[] getVerifTautologyPerf(int upTo, int nbOfMeasures) {
        IntToLongFunction func = (size) -> {
            Instant before, after;
            Formule formule = Formule.generateTautology(size);
            Verificateur.Affectations affectations = Verificateur.Affectations.generateEverythingTrue(size);

            before = Instant.now();
            formule.verify(affectations);
            after = Instant.now();

            return before.until(after, ChronoUnit.NANOS);
        };

        return Performance.getPerformance(upTo, nbOfMeasures, func);
    }

    public static Double[] getVerifContradictionPerf(int upTo, int nbOfMeasures) {
        IntToLongFunction func = (size) -> {
            Instant before, after;
            Formule formule = Formule.generateContradiction(size);
            Verificateur.Affectations affectations = Verificateur.Affectations.generateEverythingTrue(size);

            before = Instant.now();
            formule.verify(affectations);
            after = Instant.now();

            return before.until(after, ChronoUnit.NANOS);
        };

        return Performance.getPerformance(upTo, nbOfMeasures, func);
    }
}
