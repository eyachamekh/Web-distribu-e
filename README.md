# Web-distribuee
Technologies et Architecture

Ce projet utilise une architecture microservices, où chaque service est développé et déployé indépendamment. Les services communiquent entre eux via des API REST, ce qui rend le système modulable, évolutif et sécurisé de projet E-Commerce: application de vente de produit alimentaires .

Eureka Server - Service de Découverte
Rôle : Sert de registre pour les microservices. Permet aux services de se découvrir automatiquement. Dépendance principale : spring-cloud-starter-netflix-eureka-server

API Gateway - Point d’entrée unique
Rôle : Reçoit toutes les requêtes des clients. Redirige les requêtes vers les bons microservices via Eureka. Gère la sécurité avec Keycloak (authentification et autorisation).
Intègre Keycloak pour :
Authentification (connexion via Keycloak)
Autorisation (droits d’accès selon les rôles) 
Dépendances : spring-cloud-starter-gateway spring-cloud-starter-netflix-eureka-client
Keycloak
 Serveur d’identité
 Fournit les tokens (JWT)
 Gère :
Utilisateurs
Rôles
Groupes
Clients

Service Produits
Permet d’ajouter, modifier ou supprimer les produits alimentaires. Chaque fiche produit contient les informations nécessaires pour faciliter la navigation et l’achat.

Service Stock
Assure le suivi des quantités disponibles en temps réel. Les stocks sont automatiquement mis à jour après chaque commande. Permet d’éviter les ruptures de stock et de planifier les réapprovisionnements.

Service Blog
Propose des articles autour de l’alimentation, des recettes, des conseils santé, ou des nouveautés. Outil marketing pour engager les visiteurs, améliorer le SEO et fidéliser les clients.

Service Réclamation
Rôle : Gère les opérations liées aux réclamations . Utilise MySQL pour stocker les données. chaque reclamation ajouté un mail envoyé au admin

Service  Offres et Promotions
Crée et planifie des réductions, codes promo ou ventes flash sur certains produits ou catégories. Attire les clients avec des offres personnalisées et augmente les ventes durant les périodes stratégiques