# TP3 : choix des DAO avec Spring IoC

Dans ce travail, nous avons utilise Spring pour fournir un DAO a la classe `MetierImpl`. Cette classe depend de l'interface `IDao` et recoit son implementation par le constructeur. Elle garde le meme calcul : recuperer une valeur et la multiplier par 2.

Les classes DAO sont des composants Spring. `AppConfig` les trouve avec `@ComponentScan` et charge le fichier `app.properties` avec `@PropertySource`. Les captures ci-dessous montrent les etapes realisees pendant le TP.

## Premier choix : @Primary

Avec `@Primary`, nous indiquons a Spring le bean a utiliser en priorite. Pour le choix par defaut, `PrimaryConfig` fournit une instance de `DaoImpl`. Cette implementation retourne `100.0`, donc le calcul donne `200.0`.

![Execution avec la source de production](Screenshot%202026-10-14%20094514.png)

Nous retrouvons le meme resultat dans cette deuxieme execution.

![Deuxieme execution avec un resultat de 200.0](Screenshot%202026-10-14%20094553.png)

## Deuxieme choix : un bean de configuration

Nous avons utilise `DaoAliasConfig` pour choisir la source dans une methode annotee `@Bean`. La methode retourne `new DaoApi()` et porte aussi `@Primary`.

La source API fournit `220.0`. Le resultat du calcul devient alors `440.0`, sans changer `MetierImpl`.

![Execution avec le DAO de l'API](Screenshot%202026-10-14%20095114.png)

## Troisieme choix : une propriete

Le fichier `app.properties` contient `dao.target=daoApi`. `DynamicDaoConfig` lit cette propriete avec `@Value`, puis son `switch` retourne le DAO choisi.

Avec la valeur `daoApi`, le programme affiche `440.0`.

![Resultat avec la selection par propriete](Screenshot%202026-10-14%20101353.png)

Les autres choix sont `dao`, `dao2` et `daoFile`. Ils donnent respectivement `200.0`, `300.0` et `360.0`. Ces DAO retournent des valeurs fixes pour simuler les sources.

Dans IntelliJ, nous lancons `Main` avec l'argument `primary`, `alias` ou `dynamic` pour essayer une strategie. Sans argument, le programme utilise `dynamic`. Une seule configuration de selection est chargee a la fois. Apres un changement de propriete, nous relancons le programme.

## Test du calcul

La capture montre le premier test JUnit avec `DaoImpl`. Ce test attendait `200.0` et son execution etait reussie.

![Premier test JUnit reussi](Screenshot%202026-10-14%20102614.png)

Ce premier test chargeait seulement `AppConfig`. Le nom `dao` du parametre du constructeur correspondait au bean `dao`. Il ne verifiait donc pas la selection par propriete. Nous avons ensuite ajoute des tests pour verifier les quatre sources et une cible inconnue.

## Trace de la dependance injectee

La methode annotee `@PostConstruct` affiche le nom du DAO recu par `MetierImpl`. Dans cette capture, la trace indique `DaoImpl`, puis le programme affiche `200.0`.

![Trace de DaoImpl et resultat du calcul](Screenshot%202026-10-14%20103328.png)

## Conclusion

Nous avons garde la classe metier independante du choix de la source. `@Primary` permet de definir une priorite, `@Bean` permet de choisir dans la configuration et une propriete permet de changer le choix sans recompiler le code Java. Le calcul reste identique dans `MetierImpl`, ce qui respecte l'objectif du principe OCP dans ce TP.
