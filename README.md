
# MQ Message Core - Backend

Une application de gestion de messages techniques et de partenaires développée avec Spring Boot, PostgreSQL, et IBM MQ côté backend. Les messages reçus depuis une file MQ sont stockés en base de données, tandis que la gestion des partenaires s’effectue via une API REST.

## Technologies utilisées

- Java 21
- Spring Boot 3
- IBM MQ
- PostgreSQL
- Maven
- Docker & Docker Compose

## Prérequis

- **Java 21** : Assurez-vous d'avoir Java 21 installé si vous exécutez en dehors de Docker.
- **Docker & Docker Compose** : Utilisés pour lancer IBM MQ, PostgreSQL et l'application backend.
- **Maven** : Non requis si vous utilisez uniquement Docker Compose pour construire et exécuter l'application. Nécessaire uniquement si vous souhaitez exécuter les tests localement ou packager manuellement le projet.

## Installation et Démarrage

Cloner le dépôt :

```bash
git clone <URL_DU_RÉPÔT>
cd mq-msg-core
```

Lancer le projet avec Docker Compose (construit automatiquement le backend) :

```bash
docker-compose up --build
```

Cela va démarrer 3 conteneurs :

- Une application Spring Boot (backend)
- Un serveur IBM MQ
- Une base de données PostgreSQL

## Interface d'administration IBM MQ

Une interface web est accessible pour interagir avec IBM MQ :

- **Lien** : [https://localhost:9443/ibmmq/console](https://localhost:9443/ibmmq/console)
- **Port** : `9443`
- **Utilisateur** : `admin`
- **Mot de passe** : `passw0rd`

Vous pouvez utiliser ce tableau de bord pour envoyer un message à la file **DEV.QUEUE.1**, qui est celle écoutée par l’application dans le cas nominal.

## Accès à PostgreSQL

- **Port** : `5432`
- **Utilisateur** : `postgres`
- **Mot de passe** : `root`
- **Base de données** : `mydb`

## Tests

Les tests unitaires et d’intégration sont intégrés dans le projet. Ils sont automatiquement exécutés lors de la phase de build dans Docker.

Pour les lancer manuellement (en local) :

```bash
./mvnw test
```

## Performance et Résilience

Cette application devrait répondre à des contraintes de performance et de résilience étant donnée la volumétrie importante de messages à traiter. Plusieurs mécanismes sont mis en place :

- **Pagination des messages** : Les messages sont consultés via une API paginée afin de limiter la charge sur la base de données et améliorer la rapidité de réponse.
- **Listener IBM MQ robuste** : Implémentation d'un listener IBM MQ gérant la redélivrance des messages et la mise en backout des messages échoués, garantissant ainsi une résilience accrue dans le traitement.
- **Tuning HikariCP** : Le pool de connexions à PostgreSQL est optimisé pour garantir une performance continue sous charge élevée.

## Remarques

- Toutes les configurations (ports, credentials, files MQ, etc.) sont centralisées dans `docker-compose.yml`.
- Aucun besoin de packager manuellement l’application. Le `Dockerfile` s’en occupe.
- Une seule commande suffit pour tout lancer. Ce projet est conçu pour être simple à démarrer, tester et déployer grâce à Docker.
