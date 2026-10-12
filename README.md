# IFT3913 – Tâche 2 : génération de tests par LLM et analyse de mutation sur Apache Tika

[![tache2 - tests et mutation (tika-core)](https://github.com/Ost98/tika/actions/workflows/tache2.yml/badge.svg)](https://github.com/Ost98/tika/actions/workflows/tache2.yml)

|                              |                                                                                                                                                                      |
| ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Équipe** (`CORREA_ZELAYA`) | David Correa Ruiz · Carlos Daniel Zelaya Quinteros                                                                                                                   |
| **Référentiel**              | https://github.com/Ost98/tika (fork de [umontreal-diro/tika](https://github.com/umontreal-diro/tika), lui-même issu d'[Apache Tika](https://github.com/apache/tika)) |
| **Module étudié**            | `tika-core`                                                                                                                                                          |
| **Classes ciblées**          | `org.apache.tika.io.EndianUtils`, `org.apache.tika.mime.MediaType`, `org.apache.tika.mime.Patterns`                                                                  |
| **GitHub Action**            | [`.github/workflows/tache2.yml`](.github/workflows/tache2.yml)                                                                                                       |
| **Utilisation de l'IA**      | voir la [section 12](#12-déclaration-dutilisation-de-lia)                                                                                                            |

Ce fichier remplace le README d'Apache Tika ; l'original est disponible sur [apache/tika](https://github.com/apache/tika/blob/main/README.md).

## Résumé

| Étape                                                | Score de mutation (3 classes) | Mutants tués / 335 |
| ---------------------------------------------------- | ----------------------------- | ------------------ |
| Tests originaux de Tika                              | **29 %**                      | 98                 |
| \+ tests générés par ChatUniTest (après corrections) | **59 %**                      | 198                |
| \+ tests écrits à la main                            | **93 %**                      | 310                |

- ChatUniTest, avec un modèle local (Qwen2.5-Coder 7B via Ollama), a produit **32 fichiers / 92 tests** pour les 3 classes.
- Tous **compilent** sans intervention, mais seulement **68 / 92 (74 %) passent** : **22 corrections** ont été nécessaires (24 tests en échec). Aucun échec ne révélait un bogue de Tika : tous venaient d'oracles faux.
- **32 tests manuels** tuent tous les mutants restants sauf **25 mutants équivalents** (justifiés à la section 8).
- La GitHub Action exécute les 873 tests de `tika-core` (dont les 124 nouveaux) et pitest : **verte**, avec un résultat pitest identique, mutant par mutant, à celui obtenu en local.

## Table des matières

1. [Choix des classes](#1-choix-des-classes)
2. [Installation de ChatUniTest dans Maven](#2-installation-de-chatunitest-dans-maven)
3. [Génération des tests](#3-génération-des-tests)
4. [Intégration : compilation, exécution et corrections](#4-intégration--compilation-exécution-et-corrections)
5. [Critique des oracles générés](#5-critique-des-oracles-générés-comparés-aux-tests-écrits-à-la-main)
6. [Analyse de mutation avec pitest](#6-analyse-de-mutation-avec-pitest)
7. [Tests ajoutés manuellement](#7-tests-ajoutés-manuellement)
8. [Mutants restants : mutants équivalents](#8-mutants-restants--mutants-équivalents)
9. [GitHub Action](#9-github-action)
10. [Reproduire les résultats](#10-reproduire-les-résultats)
11. [Fichiers ajoutés ou modifiés](#11-fichiers-ajoutés-ou-modifiés)
12. [Déclaration d'utilisation de l'IA](#12-déclaration-dutilisation-de-lia)

---

## 1. Choix des classes

**Démarche.** Nous avons exécuté les 749 tests de `tika-core` avec JaCoCo, puis retenu les classes qui (1) ont déjà une classe de test, (2) ne sont pas couvertes à 100 %, (3) contiennent de la logique (calculs, conditions) et peu de dépendances (fichiers, réseau, threads), ce qui convient à un LLM local. Pitest a ensuite été exécuté sur 5 candidates pour confirmer la présence de mutants vivants. `FilenameUtils` (63 %) et `CharsetUtils` (80 %, aucun survivant) ont été écartées.

**Couverture et mutants avec les tests originaux** (`EndianUtilsTest`, `MediaTypeTest`, `PatternsTest`) :

| Classe        | Tests originaux | Couverture lignes (JaCoCo) | Couverture branches | Mutants | Tués   | Survivants | Non couverts | **Score** |
| ------------- | --------------- | -------------------------- | ------------------- | ------- | ------ | ---------- | ------------ | --------- |
| `EndianUtils` | 4               | 31/121 (25 %)              | 10/28               | 206     | 38     | 14         | 154          | **18 %**  |
| `MediaType`   | 9               | 125/155 (80 %)             | 67/90               | 85      | 48     | 19         | 18           | **56 %**  |
| `Patterns`    | 4               | 69/77 (89 %)               | 42/64               | 44      | 12     | 17         | 15           | **27 %**  |
| **Total**     | 17              |                            |                     | **335** | **98** | **50**     | **187**      | **29 %**  |

**Méthodes non couvertes ou mal vérifiées** (mutants vivants par méthode, extrait) :

- `EndianUtils` : toutes les lectures sur flux `readLongBE`/`readLongLE` (24 chacune), `readIntBE`/`readIntLE` (12), `readUShortBE`/`readUShortLE` (6), et la plupart des lectures sur tableau `getIntBE`/`getIntLE` (16), `getLongLE` (8), `getUShort*` (7). Les 4 tests originaux ne testent que `readUE7`, `readUIntLE`, `readUIntBE` et `readIntME`.
- `MediaType` : `union` (7), `isSimpleName` (6), `set` (4), `equals` (4), `parse` (3).
- `Patterns` : `matches` (9), `compile` (6), `add` (6), `addName`/`addGlob`/`addExtension`.

`Patterns` illustre bien l'écart entre couverture et qualité des tests : **89 % des lignes** sont exécutées, mais seulement **27 % des mutants** sont tués, car `PatternsTest.testMatches` vérifie uniquement que `matches(null)` lève une exception, jamais qu'un nom de fichier est reconnu.

Rapport pitest complet : [`tache2/pitest-original/index.html`](tache2/pitest-original/index.html).

---

## 2. Installation de ChatUniTest dans Maven

### Environnement

| Élément        | Version                                                                   |
| -------------- | ------------------------------------------------------------------------- |
| ChatUniTest    | `io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1`                   |
| Ollama         | 0.40.3                                                                    |
| Modèle         | `qwen2.5-coder:7b` (Q4, 4,7 Go), exécuté à 100 % sur GPU (RTX 3070, 8 Go) |
| JDK local / CI | 24 / 17 (Tika cible Java 17)                                              |
| Maven          | 3.9.12 (wrapper `mvnw` du projet)                                         |
| JUnit (Tika)   | 6.1.3                                                                     |

### Configuration (`tika-core/pom.xml`)

```xml
<plugin>
  <groupId>io.github.zju-aces-ise</groupId>
  <artifactId>chatunitest-maven-plugin</artifactId>
  <version>2.1.1</version>
  <configuration>
    <apiKeys>ollama</apiKeys>                                   <!-- obligatoire, ignorée par Ollama -->
    <model>code-llama</model>                                   <!-- alias Ollama, voir ci-dessous -->
    <url>http://localhost:11434/v1/chat/completions</url>       <!-- API compatible OpenAI d'Ollama -->
    <testNumber>${chatunitest.testNumber}</testNumber>          <!-- 2 par défaut -->
    <maxRounds>3</maxRounds>                                    <!-- tours de réparation -->
    <enableMultithreading>true</enableMultithreading>
    <maxThreads>2</maxThreads>                                  <!-- le GPU traite peu de requêtes à la fois -->
    <testOutput>${project.basedir}/chatunitest-tests</testOutput>
    <tmpOutput>${project.build.directory}/chatunitest-info</tmpOutput>
  </configuration>
</plugin>
```

### Difficultés rencontrées et solutions

1. **Liste fermée de modèles.** ChatUniTest 2.1.1 n'accepte que les noms de l'énumération `zju.cst.aces.api.config.Model` (`gpt-*`, `code-llama`, `codeqwen:v1.5-chat`) et envoie ce nom tel quel à l'API. Nous avons donc créé dans Ollama un **alias `code-llama` qui est en réalité Qwen2.5-Coder 7B** :
    ```
    # Modelfile
    FROM qwen2.5-coder:7b
    PARAMETER num_ctx 16384
    ```
    `ollama create code-llama -f Modelfile`
2. **Taille du contexte.** Ollama limite le contexte à 4 096 tokens par défaut, alors que ChatUniTest suppose 16 385 tokens pour `code-llama` (prompts jusqu'à ≈ 10 900 tokens + 1 024 de réponse). L'alias fixe `num_ctx` à 16 384.
3. **Conflit de versions JUnit.** La documentation recommande la dépendance `chatunitest-starter`, mais elle apporte JUnit 5.7.2 et JUnit Platform 1.7.2 en scope `compile`, incompatibles avec le JUnit 6.1.3 de Tika. Nous ne l'avons pas ajoutée ; seules les dépendances réellement utilisées par les tests générés ont été ajoutées en scope `test` (`junit-jupiter-params`, `mockito-core`, `mockito-junit-jupiter`), dans les versions gérées par le pom parent de Tika.
4. **Bogue de ChatUniTest avec le JDK 24.** `ProjectParser.parseJavaVersion` exige un point dans la propriété `java.version` ; le JDK 24 renvoie `"24"` et le plugin plante (`Invalid Java version format: 24`). Contournement : passer `-Djava.version=24.0.0` à Maven (les propriétés `-D` de Maven deviennent des propriétés système de la JVM).
5. **`testNumber` non surchargeable.** Une valeur écrite en dur dans `<configuration>` a priorité sur `-DtestNumber=…`. La valeur passe donc par la propriété `chatunitest.testNumber`.

ChatUniTest n'est **pas** exécuté dans la GitHub Action (il exige Ollama en local) : les tests générés sont commités.

---

## 3. Génération des tests

Commande (une par classe) :

```bash
./mvnw -B -pl tika-core -Drat.skip -Dcheckstyle.skip -Dforbiddenapis.skip -Djava.version=24.0.0 \
  io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1:class -DselectClass=EndianUtils
```

### Où sont les tests générés ?

- **Sortie brute de ChatUniTest**, non modifiée : [`tika-core/chatunitest-tests/tika-parent/tika-core/org/apache/tika/`](tika-core/chatunitest-tests/tika-parent/tika-core/org/apache/tika/) (un fichier `Classe_méthode_i_j_Test.java` par tentative réussie + un `Classe_Suite.java` par classe).
- **Tests intégrés au projet** (avec corrections) : `tika-core/src/test/java/org/apache/tika/io/EndianUtils_*_Test.java` et `tika-core/src/test/java/org/apache/tika/mime/{MediaType,Patterns}_*_Test.java`.
- Messages d'erreur de compilation conservés par ChatUniTest : `tika-core/target/chatunitest-info/.../error-message/` (non commité) ; ceux de `Patterns` sont archivés dans [`tache2/chatunitest-patterns-essai1/`](tache2/chatunitest-patterns-essai1/).

### Bilan

| Classe                                | Durée  | Méthodes ignorées par ChatUniTest              | Échecs de compilation pendant la génération | Fichiers produits                | Tests  |
| ------------------------------------- | ------ | ---------------------------------------------- | ------------------------------------------- | -------------------------------- | ------ |
| `EndianUtils`                         | 9 min  | 0                                              | 74                                          | 21 (15 méthodes sur 23)          | 55     |
| `MediaType`                           | 3 min  | 16 (privées, accesseurs, constructeurs, `set`) | 9                                           | 10 (10 méthodes sur 11)          | 33     |
| `Patterns` – essai 1 (`testNumber=2`) | 4 min  | 5 (méthodes privées)                           | 16                                          | **1** (`add`, réparé au 2ᵉ tour) | 4      |
| `Patterns` – essai 2 (`testNumber=5`) | 10 min | 5                                              | 45                                          | **0**                            | 0      |
| **Total retenu**                      |        |                                                |                                             | **32**                           | **92** |

**Méthodes sans aucun test généré :** `EndianUtils.readIntBE`, `readIntLE`, `readIntME`, `readLongBE`, `readLongLE`, `readShortBE`, `readUIntBE`, `readUShortBE` (lecture sur flux, gestion d'exceptions), `MediaType.audio`, `Patterns.matches`. Ce sont précisément les méthodes qui portaient le plus de mutants.

**Causes des échecs de compilation pendant la génération** (messages conservés par ChatUniTest) :

| Erreur                                                                   | Occurrences | Explication                                                                                                              |
| ------------------------------------------------------------------------ | ----------- | ------------------------------------------------------------------------------------------------------------------------ |
| `cannot find symbol: BufferUnderrunException`                            | 100         | classe interne `EndianUtils.BufferUnderrunException` utilisée sans qualification ; 3 tours de réparation n'ont pas suffi |
| `possible lossy conversion from int to byte`                             | 55          | `byte[] b = {0xFF}` au lieu de `(byte) 0xFF`                                                                             |
| `cannot find symbol: variable names / extensions / globs`, `class Field` | ≈ 50        | accès aux champs **privés** de `Patterns` par réflexion, sans même importer `Field`                                      |
| `MimeTypeRegistry`, `MediaTypeException`                                 | 6           | classes **inventées** (les vraies : `MediaTypeRegistry`, `MimeTypeException`)                                            |

**Essai 2 de `Patterns`.** Augmenter `testNumber` de 2 à 5 n'a rien amélioré (0 succès en 45 tentatives) : les tentatives sont indépendantes et le modèle reproduit les mêmes erreurs. Le seul test obtenu à l'essai 1 relève du hasard.

---

## 4. Intégration : compilation, exécution et corrections

**Les tests compilent-ils sans intervention ?** Oui, les 32 fichiers compilent tels quels.
**S'exécutent-ils sans intervention ?** Non : **68 / 92 tests passent (74 %)**, 24 échouent (21 échecs d'assertion, 3 erreurs) dans 13 fichiers.

| Classe        | Tests | Passent tels quels | Échouent |
| ------------- | ----- | ------------------ | -------- |
| `EndianUtils` | 55    | 35                 | 20       |
| `MediaType`   | 33    | 29                 | 4        |
| `Patterns`    | 4     | 4                  | 0        |

ChatUniTest annonçait pourtant « _compile and execute successfully_ » pour ces tests : l'outil ne vérifie que la compilation et l'absence de plantage, pas la justesse des assertions.

### Interventions

- **Fichiers `*_Suite.java` non copiés** (3) : ils utilisent le lanceur JUnit 4 `org.junit.platform.runner.JUnitPlatform`, qui n'existe plus avec JUnit 6. Ils ne font que regrouper les tests, que Maven découvre de toute façon.
- **22 corrections de tests**, chacune marquée `// CORRECTION IFT3913 (Cn)` dans le code. Pour chaque échec, nous avons lu le code de Tika pour décider si le test révélait un bogue ou si l'oracle était faux : **les 24 échecs venaient tous d'oracles faux**. Les corrections conservent l'intention du test.
- **Style** : les 32 fichiers utilisent des imports avec étoile, interdits par le `checkstyle.xml` de Tika (128 violations `AvoidStarImport`). Pour garder le code généré tel quel, `**/*_Test.java` est exclu de checkstyle dans `tika-core/pom.xml`. (Le formateur Spotless de Tika ajoute automatiquement l'en-tête de licence et trie les imports lors du build ; ce n'est pas une correction manuelle.)

| #          | Fichier (`EndianUtils_…` / `MediaType_…`) | Catégorie                                 | Problème                                                                                                                                    | Correction                                         |
| ---------- | ----------------------------------------- | ----------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------- |
| C1         | `getIntBE_22_0`                           | Ordre des octets                          | attend `0x04030201` pour `{01,02,03,04}` (little-endian)                                                                                    | `0x01020304` (3 assertions)                        |
| C2, C3     | `getShortBE_17_0`                         | Ordre des octets                          | `0x0201`, `0x0403`                                                                                                                          | `0x0102`, `0x0304`                                 |
| C4, C5, C6 | `getUIntBE_26_0`                          | Taille de la donnée                       | croit que `getUIntBE` lit 8 octets (car il retourne un `long`)                                                                              | tableaux de 4 octets / offset 4                    |
| C7         | `getUShortBE_18_0`                        | Oracle copié-collé                        | 258 recopié du test précédent pour d'autres données                                                                                         | `0x0405`                                           |
| C8, C9     | `getUShortBE_18_0`                        | Oracle trop spécifique **et** trop faible | `try/catch` + comparaison de la classe exacte : échoue sur `ArrayIndexOutOfBoundsException` (sous-classe) et passerait si rien n'était levé | `assertThrows(IndexOutOfBoundsException.class, …)` |
| C10        | `getUShortLE_15_0`                        | Oracle sans fondement                     | 1028 ne correspond à aucun octet                                                                                                            | `0x0605`                                           |
| C11, C12   | `getUShortLE_15_0`                        | Oracle trop spécifique                    | message exact de la JVM inventé (`"Index 2, size 2"`)                                                                                       | vérification du message retirée                    |
| C13        | `readShortLE_0_0`                         | Mauvaise exception                        | `java.nio.BufferUnderflowException` (JDK, nom voisin)                                                                                       | `EndianUtils.BufferUnderrunException`              |
| C14        | `readShortLE_0_0`                         | Type signé                                | `0xFEFF` (int 65279) comparé à un `short` (−257)                                                                                            | `(short) 0xFEFF`                                   |
| C15        | `readUE7_11_0`                            | Format mal compris                        | ignore le bit de continuation `0x80` ; 2047 et 8191 inventés                                                                                | données `{8F,7F}` → 2047, `{81,80,00}` → 16384     |
| C16        | `readUIntLE_4_0`                          | Ordre des octets                          | `{0,0,0,1}` en little-endian vaut `0x01000000`, pas 1                                                                                       | flux `{1,0,0,0}`                                   |
| C17        | `readUShortLE_2_0`                        | Fin de flux                               | `0xFF` pris pour la fin de flux (−1) + mauvaise exception                                                                                   | flux `{0x01, -1}`, `BufferUnderrunException`       |
| C18        | `ubyteToInt_29_0`                         | Mock inutile                              | stubbing d'un `InputStream` jamais utilisé → `UnnecessaryStubbingException` (3 tests)                                                       | mock supprimé                                      |
| C19        | `compareTo_20_0`                          | Contrat d'API                             | attend −1 alors que `Comparable` garantit seulement « négatif » (−14) ; `try/catch(Exception)` qui avale les erreurs                        | `assertTrue(result < 0)`, `try/catch` retiré       |
| C20        | `hashCode_19_0`                           | Oubli                                     | `toString()` inclut les paramètres (`; charset=UTF-8`)                                                                                      | oracle complété                                    |
| C21        | `image_2_0`                               | Comportement non spécifié                 | suppose `image(null) == null` ; le code produit `image/null`                                                                                | test de caractérisation `"image/null"`             |
| C22        | `image_2_0`                               | Oubli                                     | `MediaType` normalise en minuscules                                                                                                         | `"image/invalidtype"`                              |

Après corrections : **92 / 92 tests générés passent**.

---

## 5. Critique des oracles générés, comparés aux tests écrits à la main

### Ce que le LLM fait bien

- **Valeurs exactes sur des données distinctes**, quand il comprend le format : `getUShortBE({0x01, 0x02}) == 258`, `getIntLE({01,02,03,04}) == 0x04030201`. Ces oracles sont précis et tuent beaucoup de mutants arithmétiques (section 6).
- **Cas limites d'exception** pertinents : tableau vide, offset négatif, offset trop grand (`assertThrows(IndexOutOfBoundsException.class, …)`).
- **Oracles métier ciblés**, parfois : `Patterns_add_1_1_Test` vérifie qu'un même motif associé à deux types MIME différents lève `MimeTypeException`. Ce seul test tue 15 mutants.
- `MediaType_equals_18_0_Test` couvre réflexivité, `null`, autre type et paramètres différents.

### Ses défauts

1. **Oracles faux** (24 tests sur 92) : le modèle « devine » la valeur attendue au lieu de la déduire du code. Les erreurs portent sur la logique du domaine (ordre des octets, taille, signe, encodage `readUE7`), voir la section 4.
2. **Hallucinations plausibles** : `BufferUnderflowException` (JDK) au lieu de `BufferUnderrunException` (Tika), messages d'exception de la JVM inventés, classes inexistantes (`MimeTypeRegistry`).
3. **Faux positifs : tests verts qui ne vérifient rien.** `MediaType_application_0_0_Test` et `MediaType_text_3_0_Test` enveloppent tout dans `try { … } catch (Exception e) { e.printStackTrace(); }`. Si `application()` retourne `null`, la `NullPointerException` est avalée et le test reste vert : c'est pourquoi les mutants « retourne null » de ces méthodes survivent. Pire, dans `application_0_0`, l'appel `invoke(null, null)` lève une `IllegalArgumentException` (un `null` passé comme tableau de varargs) : **3 assertions sur 5 ne sont jamais exécutées**, dont une est fausse.
4. **Oracles trop spécifiques ou trop faibles** : classe d'exception exacte, message exact de la JVM, `-1` au lieu de « négatif » ; à l'inverse, `try/catch` sans `fail()` qui passe si aucune exception n'est levée.
5. **Données uniformes** : le test de `getLongLE` n'utilise que des octets `0xFF` ; avec ces données, remplacer `0xff & data[j]` par `0xff | data[j]` donne le même résultat, et le mutant survit.
6. **Complexité inutile** : 6 fichiers `MediaType` sur 10 appellent par **réflexion** (`getDeclaredMethod` + `setAccessible`) des méthodes publiques ; mocks Mockito inutiles (C18), champs jamais utilisés (`Patterns_add_1_1_Test` déclare `globs`, `names`, `extensions` sans s'en servir).
7. **Tests redondants** : vérifications dupliquées (`ubyteToInt((byte) 0xFF)` et `ubyteToInt((byte) -1)` testent la même valeur).
8. **Angle mort sur les méthodes difficiles** : aucun test pour la lecture sur flux en big-endian ou en 64 bits, ni pour `Patterns.matches`, le cœur fonctionnel de la classe.

### Comparaison avec les tests écrits par les développeurs de Tika

| Aspect               | Tests humains (`EndianUtilsTest`, `MediaTypeTest`, `PatternsTest`)                                                                                                                            | Tests générés                                                                     |
| -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
| Justesse des oracles | toujours justes                                                                                                                                                                               | 26 % faux avant correction                                                        |
| Choix des données    | cas tirés de la spécification ou de bogues réels : normalisation de la casse et des espaces, valeurs entre guillemets, charset en premier (TIKA-350), encodages `readUE7` réels (542, 728855) | valeurs « simples » choisies au hasard ; aucune donnée issue de formats réels     |
| Couverture           | lacunaire : `PatternsTest.testMatches` ne teste que `matches(null)` ; seulement 4 tests pour `EndianUtils`                                                                                    | large mais superficielle : beaucoup de méthodes simples, aucune méthode difficile |
| Lisibilité           | API publique, assertions directes                                                                                                                                                             | réflexion, mocks inutiles, `try/catch`                                            |

En résumé, les tests humains sont **peu nombreux mais justes et ciblés** ; les tests générés sont **nombreux, souvent faux ou superficiels**, mais leur volume a doublé le score de mutation (29 % → 59 %) une fois corrigés. Le LLM est utile pour produire rapidement des tests de calcul sur des méthodes simples ; la vérification humaine des oracles reste indispensable.

---

## 6. Analyse de mutation avec pitest

### Configuration (`tika-core/pom.xml`)

- `pitest-maven` 1.30.0 + `pitest-junit5-plugin` 1.2.3, mutateurs par défaut (`DEFAULTS`).
- `targetClasses` : les 3 classes ; `targetTests` : leurs 3 classes de test originales + `${pit.generatedTests}` (`org.apache.tika.*_*_Test`) + `${pit.manualTests}` (`org.apache.tika.*MutationTest`). Chaque groupe peut être exclu (`-Dpit.generatedTests=aucun`), ce qui permet de reproduire chaque étape.
- Ajout de la dépendance de test `junit-platform-launcher`, nécessaire au plugin JUnit 5 de pitest pour découvrir les tests.
- Problème rencontré : sous Windows, pitest ne trouvait aucun test (`ClassNotFoundException` dans le processus secondaire) parce que le chemin du projet contenait un `é` (« Qualité »). Le dossier a été renommé.

### Scores

| Classe        | Tests originaux   | + tests générés    | Gain     |
| ------------- | ----------------- | ------------------ | -------- |
| `EndianUtils` | 38/206 (18 %)     | 109/206 (**53 %**) | +71      |
| `MediaType`   | 48/85 (56 %)      | 62/85 (**73 %**)   | +14      |
| `Patterns`    | 12/44 (27 %)      | 27/44 (**61 %**)   | +15      |
| **Total**     | **98/335 (29 %)** | **198/335 (59 %)** | **+100** |

Couverture des lignes des classes mutées : 59 % → 77 % ; _test strength_ (tués / couverts) : 66 % → 85 %.
Rapports : [`tache2/pitest-original/`](tache2/pitest-original/index.html), [`tache2/pitest-generes/`](tache2/pitest-generes/index.html).

**Les tests générés ne détectent pas tous les mutants : 137 restent vivants** (101 non couverts, 36 survivants), principalement dans les méthodes sans test généré (section 3).

### Mutants tués par les tests générés, et pourquoi

| Méthode                                                                                    | Mutants nouvellement tués                                                | Test(s) responsable(s)                                                  |
| ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------ | ----------------------------------------------------------------------- |
| `EndianUtils.getIntBE`                                                                     | 10 Math, 3 Increments, 2 PrimitiveReturns                                | `getUIntBE_26_0` (14 : `getUIntBE` appelle `getIntBE`), `getIntBE_22_0` |
| `EndianUtils.getIntLE`                                                                     | 10 Math, 3 Increments, 1 PrimitiveReturns                                | `getIntLE_21_0`                                                         |
| `EndianUtils.getLongLE`                                                                    | 4 Math, 1 ConditionalsBoundary, 1 NegateConditionals, 1 PrimitiveReturns | `getLongLE_28_0`                                                        |
| `EndianUtils.getUShortBE` / `getUShortLE`                                                  | 5 + 5 Math, 2 + 2 PrimitiveReturns                                       | `getUShortBE_18_0`, `getShortLE_12_0`, `getUShortLE_14_0`               |
| `EndianUtils.getUIntBE`, `getUIntLE`, `getUByte`, `getShortBE`, `getShortLE`, `ubyteToInt` | 13 (Math, PrimitiveReturns)                                              | test de la méthode correspondante                                       |
| `EndianUtils.readUShortLE`, `readShortLE`                                                  | 3 Math, 1 NegateConditionals, 2 PrimitiveReturns                         | `readShortLE_0_0`                                                       |
| `EndianUtils.readUE7`                                                                      | 2 ConditionalsBoundary                                                   | `readUE7_11_0` (corrigé, C15)                                           |
| `MediaType.equals`                                                                         | 1 NegateConditionals, 2 BooleanTrueReturn, 1 BooleanFalseReturn          | `equals_18_0`                                                           |
| `MediaType.isSimpleName`, `parse`                                                          | 4 + 1                                                                    | `image_2_0` (cas `""` et `"   "`)                                       |
| `MediaType.hasParameters`, `compareTo`, `image`, `video`                                   | 5                                                                        | test de la méthode correspondante                                       |
| `Patterns.addName`, `addExtension`, `addGlob`                                              | 11 NegateConditionals                                                    | `Patterns_add_1_1`                                                      |
| `Patterns.add`                                                                             | 3 VoidMethodCall                                                         | `Patterns_add_1_1`                                                      |
| `Patterns.compile`                                                                         | 1 ConditionalsBoundary                                                   | `Patterns_add_1_1`                                                      |

**Explications :**

- **Mutants arithmétiques (`Math`, `Increments`, ≈ 55)** : pitest remplace `b0 << 24` par `b0 >> 24`, `+` par `-`, `&` par `|`, `i++` par `i--`. Les tests générés utilisent des octets **tous différents et non nuls** et comparent la valeur **exacte** : toute modification d'un décalage, d'une addition ou d'un indice change le résultat.
- **`PrimitiveReturns` (la méthode retourne 0)** : tués par n'importe quel `assertEquals` attendant une valeur non nulle.
- **`Patterns` (15 mutants par un seul test)** : `Patterns_add_1_1_Test` ajoute deux fois le même motif (nom, extension, glob) avec deux types différents et attend `MimeTypeException`. Inverser la condition de conflit (`previous == null || …`) dans `addName`/`addExtension`/`addGlob`, ou supprimer l'appel à ces méthodes dans `add`, fait disparaître l'exception. Le mutant de borne dans `compile` (`i <= glob.length()`) provoque une `StringIndexOutOfBoundsException` au lieu de l'exception attendue.
- **`MediaType.isSimpleName` tué indirectement** : `image("")` appelle `parse("image/")`, donc `isSimpleName("")`, qui doit retourner `false` ; plusieurs mutations le font retourner `true` et le résultat n'est plus `null`.
- **`MediaType.equals`** : le test compare des types égaux, différents, `null` et d'une autre classe ; les mutants qui forcent `true`/`false` ou inversent la condition sont détectés.

**Remarque :** plusieurs mutants ne sont tués que grâce à nos corrections (par exemple les 14 mutants de `getIntBE` tués par `getUIntBE_26_0` corrigé, C4-C6). Pitest exige que tous les tests passent avant de muter : sans correction, ces tests n'auraient rien apporté.

### Mutants encore vivants après les tests générés

| Type         | Méthodes                                                                                                                                                                                             | Nombre |
| ------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------ |
| Non couverts | `readLongBE`, `readLongLE`                                                                                                                                                                           | 48     |
| Non couverts | `readIntBE`, `readIntLE`, `readUShortBE`, `readShortBE`, surcharges `getIntLE(byte[])`, `getUIntLE(byte[])`                                                                                          | 33     |
| Non couverts | `Patterns.matches`                                                                                                                                                                                   | 9      |
| Non couverts | `MediaType.union`, `set`, `audio`, `parse` (charset en premier)                                                                                                                                      | 11     |
| Survivants   | `Patterns.compile` (5), `Patterns.add` (3), `readUIntBE` (4), `readIntME` (3), `readUIntLE` (3), `readUE7` (2), `MediaType.getBaseType`/`getSubtype`/`getType`/`application`/`text`, `isSimpleName`… | 36     |

---

## 7. Tests ajoutés manuellement

Trois classes, **32 tests** : [`EndianUtilsMutationTest`](tika-core/src/test/java/org/apache/tika/io/EndianUtilsMutationTest.java) (18), [`MediaTypeMutationTest`](tika-core/src/test/java/org/apache/tika/mime/MediaTypeMutationTest.java) (10), [`PatternsMutationTest`](tika-core/src/test/java/org/apache/tika/mime/PatternsMutationTest.java) (4).

### Résultat

| Classe        | Original | + générés | **+ manuels**      |
| ------------- | -------- | --------- | ------------------ |
| `EndianUtils` | 18 %     | 53 %      | **182/206 (88 %)** |
| `MediaType`   | 56 %     | 73 %      | **84/85 (99 %)**   |
| `Patterns`    | 27 %     | 61 %      | **44/44 (100 %)**  |
| **Total**     | 29 %     | 59 %      | **310/335 (93 %)** |

Plus aucun mutant non couvert ; couverture des lignes : 350/354 (99 %). Rapport : [`tache2/pitest-final/`](tache2/pitest-final/index.html).

### `EndianUtilsMutationTest`

**Stratégie commune aux méthodes `read*`.** Chaque méthode lit N octets avec `stream.read()`, vérifie `(ch1 | … | chN) < 0` (fin de flux) puis combine les octets par décalages et additions. Trois types de données visent trois familles de mutants :

1. **octets tous distincts et non nuls** : toute mutation d'un décalage ou d'une addition change la valeur (mutants `Math`, `PrimitiveReturns`) ;
2. **tous les octets à 0** : valeur valide (0) à la limite exacte de la condition `< 0` ; le mutant `<= 0` lève une exception à tort (`ConditionalsBoundary`) ;
3. **flux trop court d'un octet** : le dernier `read()` retourne −1 et une `BufferUnderrunException` est attendue (`NegateConditionals`, dernier OR).

**Oracle des valeurs** : calcul à la main selon la définition de l'ordre des octets décrite dans la javadoc ; en little-endian (LE), le premier octet lu est le moins significatif, en big-endian (BE) le plus significatif.

| Test                                 | Intention                                  | Données et motivation                                                                                                                    | Oracle                                                                   | Mutants visés                                                                       |
| ------------------------------------ | ------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------ | ----------------------------------------------------------------------------------- |
| `testReadLongLEDistinctBytes`        | lecture d'un `long` LE                     | `01 02 03 84 05 06 07 08` ; `0x84` sur l'octet décalé de 24 vérifie aussi le cast en `long` qui préserve le bit 31 (commentaire du code) | `0x0807060584030201`                                                     | `Math`, `PrimitiveReturns` de `readLongLE` (non couverts)                           |
| `testReadLongBEDistinctBytes`        | lecture d'un `long` BE                     | `01 02 03 04 85 06 07 08` (même raisonnement)                                                                                            | `0x0102030485060708`                                                     | idem `readLongBE`                                                                   |
| `testReadLongAllZeroBytes`           | huit zéros sont une valeur valide          | `00 × 8`                                                                                                                                 | `0`                                                                      | `ConditionalsBoundary` de la fin de flux                                            |
| `testReadLongStreamTooShort`         | 7 octets ne suffisent pas                  | `01 … 07`                                                                                                                                | `BufferUnderrunException`                                                | `NegateConditionals`, dernier `\|` → `&`                                            |
| `testReadIntLEDistinctBytes`         | `int` LE, résultat négatif                 | `01 02 03 84`                                                                                                                            | `0x84030201`                                                             | `Math` de `readIntLE`                                                               |
| `testReadIntBEDistinctBytes`         | `int` BE                                   | `81 02 03 04`                                                                                                                            | `0x81020304`                                                             | `Math` de `readIntBE`                                                               |
| `testReadIntAllZeroBytes`            | zéros valides (LE, BE, ME)                 | `00 × 4`                                                                                                                                 | `0`                                                                      | `ConditionalsBoundary` de `readIntLE`, `readIntBE`, `readIntME`                     |
| `testReadIntStreamTooShort`          | 3 octets ne suffisent pas                  | `01 02 03`                                                                                                                               | `BufferUnderrunException`                                                | `NegateConditionals`, dernier OR                                                    |
| `testReadUIntBEHighBitIsUnsigned`    | la valeur non signée reste positive        | `87 65 43 21` (négatif en `int`)                                                                                                         | `0x87654321L`                                                            | survivants de `readUIntBE`                                                          |
| `testReadUIntAllZeroBytes`           | zéros valides                              | `00 × 4`                                                                                                                                 | `0`                                                                      | `ConditionalsBoundary` de `readUIntLE`/`readUIntBE` (survivants)                    |
| `testReadUIntBEStreamTooShort`       | 3 octets ne suffisent pas                  | `01 02 03`                                                                                                                               | `BufferUnderrunException`                                                | dernier OR de `readUIntBE`                                                          |
| `testReadUShortBEDistinctBytes`      | 16 bits BE                                 | `12 34`                                                                                                                                  | `0x1234`                                                                 | `Math` de `readUShortBE`                                                            |
| `testReadShortBENegative`            | signe d'un `short`                         | `FF FE`                                                                                                                                  | `(short) -2`                                                             | `PrimitiveReturns` de `readShortBE`                                                 |
| `testReadUShortAllZeroBytes`         | zéros valides                              | `00 00`                                                                                                                                  | `0`                                                                      | `ConditionalsBoundary` de `readUShortLE` (survivant) et `readUShortBE`              |
| `testReadUShortBEStreamTooShort`     | 1 octet ne suffit pas                      | `12`                                                                                                                                     | `BufferUnderrunException`                                                | `\|` → `&` (unique OR, donc tuable)                                                 |
| `testReadUE7StopsAfterSixBytes`      | `readUE7` lit au plus 6 octets (`max = 6`) | six `0x81` (bit de continuation + valeur 1) puis `0x01` : 7 octets pour dépasser la limite                                               | Σ 128ᵏ (k = 0…5) = 34 630 287 489 ; le 7ᵉ octet est consommé mais ignoré | `Increments` sur `read++` et `ConditionalsBoundary` sur `read++ < max` (survivants) |
| `testGetIntLEAndUIntLEWithoutOffset` | surcharges sans offset                     | `01 02 03 04` ; `FF FF FF FF`                                                                                                            | `0x04030201` ; `0xFFFFFFFFL`                                             | `PrimitiveReturns` (non couverts)                                                   |
| `testGetLongLEDistinctBytes`         | `getLongLE` sur octets distincts           | `01 … 08` (le test généré n'utilisait que `FF`)                                                                                          | `0x0807060504030201`                                                     | `&` → `\|` dans `0xff & data[j]` (survivant)                                        |

### `MediaTypeMutationTest`

**Oracle** : la javadoc de `MediaType` et le format des types MIME (RFC 2045) : `type/sous-type`, minuscules, paramètres `; clé=valeur` triés par clé.

| Test                                       | Intention                                                                     | Données et motivation                                              | Oracle                                                                                               | Mutants visés                                                                            |
| ------------------------------------------ | ----------------------------------------------------------------------------- | ------------------------------------------------------------------ | ---------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- |
| `testFactoryMethodsBuildTypeOfTheirFamily` | `application`, `audio`, `text` construisent le bon type                       | `"xml"`, `"mpeg"`, `"html"`                                        | `application/xml`, `audio/mpeg`, `text/html`                                                         | « retourne `null` » (survivants : les tests générés avalaient la `NullPointerException`) |
| `testParseCharsetFirst`                    | format inversé envoyé par certains serveurs (TIKA-350)                        | `"charset=UTF-8; text/html"`                                       | `text/html; charset=UTF-8`                                                                           | branche `CHARSET_FIRST_PATTERN` de `parse` (non couverte)                                |
| `testAccessorsIgnoreParameters`            | accesseurs sur un type avec paramètres                                        | `"text/plain; charset=UTF-8"`                                      | `getType()` = `text`, `getSubtype()` = `plain`, `getBaseType()` = `TEXT_PLAIN` sans paramètres       | `getType`, `getSubtype` (`+` → `-`, `""`), `getBaseType`                                 |
| `testAddParametersToTypeWithoutParameters` | `union` quand le type de base n'a pas de paramètres                           | `TEXT_PLAIN` + `{charset=UTF-8}`                                   | `text/plain; charset=UTF-8`                                                                          | `union` : `return b` → map vide                                                          |
| `testAddNoParametersKeepsExistingOnes`     | `union` quand on n'ajoute rien                                                | base avec `charset` + `{}`                                         | inchangé                                                                                             | `union` : branche `b.isEmpty()` (non couverte)                                           |
| `testAddParametersMergesAndNewValueWins`   | fusion des paramètres                                                         | base `charset=UTF-8; format=flowed` + `{charset=ISO-8859-1}`       | `text/plain; charset=ISO-8859-1; format=flowed` (la nouvelle valeur remplace, l'autre est conservée) | `union` : condition inversée, appels `putAll` supprimés                                  |
| `testSetOfTypesIgnoresNull`                | `set(MediaType...)` ignore `null`                                             | `TEXT_PLAIN, null, TEXT_HTML`                                      | ensemble de taille 2 contenant les deux types                                                        | `set` (non couvert)                                                                      |
| `testSetOfStringsIgnoresUnparsable`        | `set(String...)` ignore les chaînes invalides                                 | `"pas-un-type"` (sans `/`, `parse` retourne `null`)                | taille 2                                                                                             | `set` (non couvert)                                                                      |
| `testParseNormalizesUpperCase`             | les majuscules sont normalisées                                               | `"TEXT/PLAIN"` et `"TEXT/Plain"`                                   | `text/plain`                                                                                         | `isSimpleName` : condition `'a' > c` inversée                                            |
| `testParseCachesSimpleNames`               | cache des noms simples (« _Optimization for the common cases_ » dans `parse`) | nom contenant `a`, `z`, `0`, `9`, **différent à chaque exécution** | `assertSame` sur deux appels                                                                         | `isSimpleName` : borne `c <= 'z'` → `c < 'z'`                                            |

Deux observations faites en écrivant ces tests :

- Notre première version utilisait seulement `"TEXT/Plain"` et ne tuait pas le mutant de `isSimpleName` : la minuscule envoie la chaîne vers l'expression régulière, qui normalise quand même. Il faut un type **entièrement** en majuscules.
- Le mutant de borne `c <= 'z'` ne change pas le résultat de `parse`, seulement la mise en cache. Un test de cache avec un nom **fixe** ne le tuait pas dans pitest, alors qu'il le tue quand on applique la mutation à la main. Pitest exécute plusieurs mutants dans **la même JVM** : le cache statique `SIMPLE_TYPES` survit d'un mutant à l'autre et contenait déjà le nom. D'où un nom unique par exécution. La même cause explique pourquoi le test généré `testParseOptimization` (`assertSame` sur `"text/plain"`, toujours en cache) ne tue rien.

### `PatternsMutationTest`

**Oracle** : la javadoc de `Patterns.matches`, qui précise que les noms littéraux (ex. `Makefile`) sont reconnus avant tout le reste, puis les motifs `*.xxx`, puis les autres globs, et que le motif le plus long l'emporte (`Data.tar.gz` est reconnu par `*.tar.gz` plutôt que par `*.gz`) ; et la sémantique des globs (`*` = suite quelconque, `?` = un caractère, `.` littéral). Contrairement au test généré, on utilise de vrais `MimeType` et un vrai `MediaTypeRegistry`, comme `PatternsTest`.

| Test                                    | Intention                                | Données et motivation                             | Oracle                                                                                           | Mutants visés                                                                      |
| --------------------------------------- | ---------------------------------------- | ------------------------------------------------- | ------------------------------------------------------------------------------------------------ | ---------------------------------------------------------------------------------- |
| `testExactNameHasPriorityOverExtension` | un nom exact l'emporte sur l'extension   | `*.txt` → text, `README.txt` → xml                | `matches("README.txt")` = xml, `matches("notes.txt")` = text                                     | `add` lignes 74-75 (un nom routé ailleurs perdrait sa priorité), `matches` 147-148 |
| `testLongestExtensionWins`              | l'extension la plus longue l'emporte     | `*.gz`, `*.tar.gz` ; `.gz` a la longueur minimale | `archive.tar.gz` → xml, `file.gz` → text, `file.zip` → `null`                                    | boucle des extensions de `matches` (bornes, `-` → `+`, retours)                    |
| `testGlobWildcardsAndLiteralDot`        | traduction d'un glob en regex            | `*.x?z` (contient `*`, `?` et `.`)                | `a.xyz` reconnu ; `aaxyz` (le point est littéral) et `a.xyyz` (`?` = un seul caractère) → `null` | `compile` (4 conditions inversées, retour `""`), `matches` lignes 162-163          |
| `testJavaRegexPatternIsUsedAsIs`        | une regex Java est utilisée telle quelle | `[0-9]+\.dat`, `isJavaRegex = true`               | `123.dat` reconnu, `abc.dat` → `null`                                                            | `add` ligne 71 (appel `addGlob` supprimé)                                          |

---

## 8. Mutants restants : mutants équivalents

Les **25 mutants** encore vivants ne changent le comportement observable pour aucune entrée valide.

| Nombre | Mutant                                                                                                                                                    | Justification                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| ------ | --------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 22     | `Replaced bitwise OR with AND` dans `(ch1 \| ch2 \| … \| chN) < 0` des méthodes `read*` (2 pour chaque `readInt*`/`readUInt*`, 6 pour chaque `readLong*`) | L'expression est évaluée de gauche à droite : `((ch1 \| ch2) \| ch3) \| ch4`. Seul le **dernier** `\|` est tuable (flux trop court d'un octet), et nos tests le tuent. Pour les autres : un `InputStream` qui a atteint la fin retourne −1 à **tous** les appels suivants, donc si un octet vaut −1, le dernier aussi, et `x & … \| -1` reste négatif. Ces mutants ne sont tuables qu'avec un flux non conforme qui renverrait des données après −1. |
| 2      | `Changed increment from 1 to -1` sur le dernier `data[i++]` de `getIntLE` et `getIntBE`                                                                   | La valeur de `i` après le dernier accès n'est jamais lue.                                                                                                                                                                                                                                                                                                                                                                                            |
| 1      | `replaced return value with null` sur `return null;` (ligne 257 de `MediaType.parse`)                                                                     | Remplace `null` par `null`.                                                                                                                                                                                                                                                                                                                                                                                                                          |

---

## 9. GitHub Action

[`.github/workflows/tache2.yml`](.github/workflows/tache2.yml), déclenchée à chaque `push` ou `pull_request` sur `main` (et manuellement), sur Ubuntu avec le JDK 17 :

1. `./mvnw -B -pl tika-core -am install` : compile `tika-core` et exécute **tous** ses tests, avec les vérifications de Tika (checkstyle, RAT, forbiddenapis) ;
2. `./mvnw -B -pl tika-core surefire:test -Dtest='*_Test,*MutationTest'` : réexécute **uniquement les 124 nouveaux tests** (92 générés + 32 manuels), pour que leur résultat soit visible séparément ;
3. `./mvnw -B -pl tika-core org.pitest:pitest-maven:mutationCoverage` : analyse de mutation, avec un tableau du score affiché dans le résumé de l'exécution ;
4. publication des artefacts `rapport-pitest` et `rapports-surefire`.

Les workflows d'Apache Tika (build complet de tous les modules, publication Docker) ont été déplacés dans `.github/workflows-apache-desactives/` pour ne pas s'exécuter sur ce fork.

**Résultat** : ✅ **873 tests, 0 échec** (dont **124 nouveaux, 0 échec**) ; pitest **310/335 (93 %)**. Le statut de chaque mutant est **identique** à celui obtenu en local sous Windows.

---

## 10. Reproduire les résultats

Prérequis : JDK 17+, chemin du projet **sans caractère accentué** (voir la section 6).

```bash
# Tous les tests de tika-core (originaux + générés + manuels)
./mvnw -B -pl tika-core -am install

# Score de mutation à chaque étape (rapport dans tika-core/target/pit-reports/)
./mvnw -B -pl tika-core org.pitest:pitest-maven:mutationCoverage -Dpit.generatedTests=aucun -Dpit.manualTests=aucun  # originaux
./mvnw -B -pl tika-core org.pitest:pitest-maven:mutationCoverage -Dpit.manualTests=aucun                              # + générés
./mvnw -B -pl tika-core org.pitest:pitest-maven:mutationCoverage                                                      # + manuels

# Régénérer des tests (Ollama lancé, alias code-llama créé, voir la section 2)
./mvnw -B -pl tika-core -Djava.version=24.0.0 \
  io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1:class -DselectClass=Patterns -Dchatunitest.testNumber=5
```

`-Djava.version=24.0.0` n'est nécessaire qu'avec un JDK dont la version ne contient pas de point (contournement du bogue de ChatUniTest).

---

## 11. Fichiers ajoutés ou modifiés

| Chemin                                                                                             | Contenu                                                                                                                                    |
| -------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| `README.md`                                                                                        | ce document                                                                                                                                |
| `tika-core/pom.xml`                                                                                | plugins pitest et ChatUniTest, dépendances de test, propriétés `pit.*` et `chatunitest.testNumber`, exclusion checkstyle des tests générés |
| `tika-core/src/test/java/org/apache/tika/io/EndianUtils_*_Test.java`                               | 21 fichiers générés (corrigés)                                                                                                             |
| `tika-core/src/test/java/org/apache/tika/mime/MediaType_*_Test.java`, `Patterns_add_1_1_Test.java` | 11 fichiers générés (corrigés)                                                                                                             |
| `tika-core/src/test/java/org/apache/tika/**/*MutationTest.java`                                    | 3 classes de tests écrits à la main                                                                                                        |
| `tika-core/chatunitest-tests/`                                                                     | sortie brute de ChatUniTest                                                                                                                |
| `tache2/chatunitest-patterns-essai1/`                                                              | test, erreurs et log du 1er essai sur `Patterns`                                                                                           |
| `tache2/pitest-original/`, `tache2/pitest-generes/`, `tache2/pitest-final/`                        | rapports pitest HTML et CSV des trois étapes                                                                                               |
| `.github/workflows/tache2.yml`                                                                     | GitHub Action                                                                                                                              |
| `.github/workflows-apache-desactives/`                                                             | workflows d'Apache Tika désactivés                                                                                                         |

---

## 12. Déclaration d'utilisation de l'IA

On a utilisé Claude Opus 5.5 en Octobre 2026 tout le long du devoir. Prémièrement, on l'a utilisé pour nous expliquer mieux comment Tika marchait. Ensuite, on l'a utilisé pour configurer l'environnement à travailler. Ensuite, vu qu'il y avait de problèmes, il nous a aidé à trouver des problèmes avec SSL.
On a tout le temps révisé le code pour savoir que tout était correct et on a executé les tests nous memes pour s'assurer qu'il a bien marché.
Claude a executé les analyses et il a proposé les tests manuels qui a après écrit suite d'une révision de notre part, aussi qu'une première génération du README avec les resultats pour donner un touche d'organisation et compréhension.
