# Documentation de Configuration (application.yml)

## Paramètres du Serveur

### Port du Serveur

Le paramètre `port` sous la section `server` définit le port sur lequel le serveur Spring Boot écoutera les requêtes.

```yaml
server:
  port: 8080
```

## Configuration de la Base de Données

### Datasource
La section datasource sous la configuration spring spécifie les détails de la base de données.

```yaml
spring:
  datasource:
    url: jdbc:h2:file:./backend/datasource/challenge.db
    username: sa
    password: password
    driver-class-name: org.h2.Driver
```
+ <strong>url</strong>: L'URL JDBC de la base de données. J'ai utilisé une base de données H2 avec un stockage de donnée dans un fichier, qui dans le chemin relatif ./backend/data/challenge.db.
+ <strong>username</strong>: Le nom d'utilisateur de la base de données.
+ <strong>password</strong>: Le mot de passe de la base de données.
+ <strong>driver-class-name</strong>: Le nom de la classe du driver JDBC.

## Configuration JPA
La section jpa sous la configuration spring gère la configuration JPA.

```yaml
jpa:
  show-sql: true
  database-platform: org.hibernate.dialect.H2Dialect
  hibernate:
    ddl-auto: update
```

+ <strong>show-sql</strong>: Affiche les déclarations SQL générées par Hibernate dans les logs.
+ <strong>database-platform</strong>: La plateforme de base de données utilisée par Hibernate, dans ce cas, le dialecte H2.
+ <strong>ddl-auto</strong>: Stratégie de mise à jour du schéma de base de données.

## Console H2
La section h2 sous la configuration spring active la console H2, une interface utilisateur pour la base de données H2.

```yaml
  h2:
    console:
      enabled: true
      path: /h2-console
```

+ enabled: Indique tout simplement si la console H2 est activée.
+ path: Le chemin d'accès à la console H2, elle est accessible à l'adresse [h2-console](localhost:8080/h2-console).
