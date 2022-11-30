# TP2 de Complexité

Réalisé par Jeff ALLARD, Elsbeth MONRROY, Étienne MULLER & Axel POULAIN

## Description du dépôt

Ce dépôt contient le code des trois parties du TP.
* Le vérificateur de la partie 1 se trouve partagé entre
  `Formule.java` et `Clause.java` (également utilisé dans les projets suivants)
  ainsi que `Verificateur.java`.
* La réduction de zone vide peut se trouver dans le fichier `Stable.java`
* Enfin, la réduction de Sudoku se trouve dans le fichier `Sudoku.java`

Les tests de Performance se trouvent répartis dans les différentes
classes du package Performance. Idéalement, `Performance.java` contient
des méthodes adaptables à tous les cas d'utilisation spécifiques utilisés par les
tests de performance contenus dans leurs classes respectives ; mais beaucoup de cas
de figure ne se sont pas révélés compatibles avec les méthodes génériques de `Performance.java`,
et certaines méthodes ont dont été ré-implémentées dans les classes de performance
spécifique.

Pour plus de détails, consulter le rapport.