package Performance;

/** Classe permettant de lancer un test sur une instance
 * d'un problème (caractérisé par sa taille) et retournant
 * le temps mis pour l'exécution
 */
public interface Tester {

    /** Exécute la fonction sur une instance de taille size
     *
     * @param size La taille de l'instance (qui sera probablement générée en fonction)
     * @return Le temps mis à l'exécution
     */
    public long test(int size);
}
