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

## Fonctionnalités

- Ajout, modification et suppression de jeux
- Collection avec couverture image, note personnelle, plateforme, année
- Système de favoris (étoile) avec affichage dédié
- Recherche en temps réel avec suggestions
- Page d'accueil avec jeux aléatoires depuis la base de données
- Popup de détail avec informations complètes
- Navigation multi-pages (Accueil, Collection, Ajouter, À Propos)
- Persistance SQLite via Hibernate

## Prérequis

- **JDK 21+** (le projet inclut un wrapper Maven, pas besoin de Maven installé)

## Lancer en développement

```powershell
# Windows
$env:JAVA_HOME = "C:\Users\[users]\.jdks\openjdk-25.0.2"
.\mvnw.cmd javafx:run
```

```bash
# Linux / macOS
./mvnw javafx:run
```

## Générer le JAR exécutable

```powershell
$env:JAVA_HOME = "C:\Users\[users]\.jdks\openjdk-25.0.2"
.\mvnw.cmd package
```

Produit : `target/gamevault-1.0-SNAPSHOT-shaded.jar`

Lancer le JAR :

```powershell
"C:\Users\[users]\.jdks\openjdk-25.0.2\bin\java.exe" -jar target\gamevault-1.0-SNAPSHOT-shaded.jar
```

Ou double-cliquer sur **`launch.bat`** à la racine du projet.

## Structure du projet

```
src/
├── main/
│   ├── java/com/gamevault/
│   │   ├── App.java
│   │   ├── Launcher.java
│   │   ├── controller/
│   │   │   ├── MainController.java
│   │   │   ├── GameListController.java
│   │   │   ├── AddGameController.java
│   │   │   ├── DetailController.java
│   │   │   └── LoginController.java
│   │   ├── model/
│   │   │   ├── Game.java
│   │   │   ├── Platform.java
│   │   │   └── GameStatus.java
│   │   ├── repository/
│   │   │   ├── GameRepository.java
│   │   │   └── GameRepositoryImpl.java
│   │   ├── service/
│   │   │   └── GameService.java
│   │   └── util/
│   │       ├── HibernateUtil.java
│   │       ├── UserSession.java
│   │       └── SearchHelper.java
│   └── resources/
│       ├── fxml/
│       │   ├── login.fxml
│       │   ├── main.fxml
│       │   ├── gamelists.fxml
│       │   ├── addgame.fxml
│       │   └── detail.fxml
│       ├── css/
│       │   ├── style.css
│       │   ├── main.css
│       │   ├── login.css
│       │   └── gamelists.css
│       ├── images/
│       │   └── game-images/
│       ├── hibernate.cfg.xml
│       └── config.properties
└── test/
    └── java/com/gamevault/
        └── AppTest.java
```

## Maquette Figma

> 🔗 [Lien Figma](https://www.figma.com/design/OdegrN42JWzY95VcM9TTpL/Sans-titre?node-id=0-1&t=ROyZuE2jsLntp2Zv-1)

## Roadmap

- [x] Initialisation Maven (Java 21, JavaFX, Hibernate, SQLite)
- [x] Architecture en couches (model / repository / service / controller)
- [x] Configuration Hibernate + SQLite
- [x] Interface multi-pages (Login, Accueil, Collection, Ajout, Détail)
- [x] CRUD complet des jeux
- [x] Recherche avec suggestions en temps réel
- [x] Gestion des jaquettes (image cover)
- [x] Système de favoris
- [x] Popup de détail avec modification/suppression
- [x] JAR exécutable via maven-shade-plugin
