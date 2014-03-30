EGG - Extended Generator Generator
==================================

**EGG** est un générateur de compilateur en Java, essentiellement destiné à l'enseignement de la compilation.

EGG est basé sur une analyse descendante (grammaires *LL(k)*) et sur les grammaires attribuées pour l'analyse sémantique.

Compilation
-----------

EGG est écrit avec lui-même, c'est pourquoi nous fournissons une version compilée : *src/eggc-5.3.1.jar*.
Il ne s'agit pas forcément de la dernière version, mais elle suffit pour compiler EGG.

Pour compiler EGG, rendez-vous dans le dossier *src* et faites un simple `make`.
Vous obtiendrez le fichier *eggc.jar* contenant l'ensemble de EGG.

Utilisation
-----------

EGG utilise un fichier **.egg** contenant la grammaire attribuée pour engendrer l'analyseur lexical, syntaxique et sémantique.

Un fichier **.ecf** au format *xml* permet de configurer la génération de code.

`java -jar eggc.jar ma_grammaire.egg` va engendrer les fichiers demandés.

Auteurs
-------

Marcel Gandriau est le principal mainteneur de EGG.
Des étudiants en stage ont aussi contribué à son développement.
