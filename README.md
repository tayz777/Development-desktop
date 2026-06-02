# GameVault 🎮

> Gestionnaire de collection de jeux vidéo – Projet final B3 Ynov

## Technologies

| Technologie | Version |
|-------------|---------|
| Java        | 21+     |
| JavaFX      | 21.0.4  |
| Hibernate   | 6.4.4   |
| SQLite      | 3.45.3  |
| Maven       | 3.9+    |

## Prérequis

- **JDK 21** ou supérieur ([Télécharger OpenJDK](https://adoptium.net/))
- **Maven 3.9+** ([Télécharger Maven](https://maven.apache.org/download.cgi))

## Lancer l'application

```bash
# Compiler
mvn compile

# Lancer
mvn javafx:run

# Tests
mvn test
```

## Structure du projet

```
src/
├── main/
│   ├── java/com/gamevault/
│   │   ├── App.java                  # Application JavaFX
│   │   ├── Launcher.java             # Point d'entrée (compatibilité JAR)
│   │   ├── controller/
│   │   │   └── MainController.java   # Contrôleur FXML principal
│   │   ├── model/
│   │   │   ├── Game.java             # Entité JPA
│   │   │   ├── Platform.java         # Enum plateformes
│   │   │   └── GameStatus.java       # Enum statuts
│   │   ├── repository/
│   │   │   ├── GameRepository.java       # Interface
│   │   │   └── GameRepositoryImpl.java   # Implémentation Hibernate
│   │   ├── service/
│   │   │   └── GameService.java      # Logique métier
│   │   └── util/
│   │       └── HibernateUtil.java    # SessionFactory singleton
│   └── resources/
│       ├── fxml/
│       │   └── main.fxml             # Vue principale
│       ├── css/
│       │   └── style.css             # Feuille de style globale
│       ├── hibernate.cfg.xml         # Configuration Hibernate / SQLite
│       └── config.properties         # Paramètres externalisés
└── test/
    └── java/com/gamevault/
        └── AppTest.java
```

## Maquette Figma

> 🔗 Lien à ajouter

## Documentation UX/UI

> 📄 Document à ajouter

## Roadmap

- [x] Initialisation Maven (Java 21, JavaFX, Hibernate, SQLite)
- [x] Architecture en couches (model / repository / service / controller)
- [x] Configuration Hibernate + SQLite prête
- [x] Paramètres externalisés (`config.properties`)
- [ ] Interface principale (FXML + CSS)
- [ ] CRUD complet des jeux
- [ ] Recherche, filtres et tri
- [ ] Gestion des jaquettes
- [ ] Export de la collection
