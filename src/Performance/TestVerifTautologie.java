package Performance;

import main.Verificateur;

public class TestVerifTautologie implements Tester {
    /**
     * Exécute la fonction sur une instance de taille size
     *
     * @param size La taille de l'instance (qui sera probablement générée en fonction)
     * @return Le temps mis à l'exécution
     */
    @Override
    public long test(int size) {
        Verificateur.Formule formule = Verificateur.Formule.generateTautology(size);

        return 0;   /*TODO: À faire */
    }
}
