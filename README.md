# Projet Tree Ftp

## Introduction

Le projet Tree Ftp a pour objectif d'afficher l'arborescence des répertoires d'un serveur Ftp en utilisant les sockets Java.

## Lancer l'executable Jar

La commande TreeFtp possède 4 parametres :

`java -jar TreeFtp.jar <NomServeur> <NomUtilisateur> <MotDePasse> <ProfondeurMax>`

Le parametre <NomServeur> est obligatoire et correspond à l'adresse IP du serveur Ftp ou de son nom de domaine.
Les parametres <NomUtilisateur> et <MotDePasse> sont optionnels et permette de s'authentifier sur le serveur, si ils ne sont pas spécifiés
alors la commande TreeFtp se connectera en mode anonyme si cela est possible.

Le dernier parametre <ProfondeurMax> est aussi un parametre optionnel de la commande et permet d'indiquer la profondeur 
maximale à laquelle on souhaite explorer l'arborescence des répertoires du serveur.
  
  

  
  
Le Guennec Yaakoub
Master E-Services - Université de Lille
