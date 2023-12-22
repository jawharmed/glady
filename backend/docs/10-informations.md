# API : Glady WeDooGift

## Lancement de l'application

Par defaut, l'application se lance sur le port 8080

+ Swagger : http://localhost:8080/swagger-ui/index.html
  + vous pouvez également visualiser directement la definition swagger du votre IDE en ouvrant le fichier swagger-definition.json (compatible par défaut dans Intellij)
  
+ Base de donnée : http://localhost:8080/h2-console
  + username : 'sa'
  + password : 'password'

+ Postman : vous pouvez également importer le fichier glady-challenge-collection.json pour créer une collection api avec tous les endpoints de l'api.


### Notes
+ J'ai opté pour une base de données H2 avec un stockage en fichier './datasource/challenge.db', ce choix permet de conserver les données même après l'arrêt de l'application.

+ Des améliorations potentielles pourraient inclure l'utilisation du design pattern Strategy afin de permettre un choix plus dynamique et extensible en ce qui concerne les types de distributions et deposit

