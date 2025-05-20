# 🧬 Jeu de la Vie - Simulation de Conway

Une implémentation Java du célèbre automate cellulaire imaginé par **John Conway**, avec deux modèles de simulation et une interface graphique interactive.

## Auteurs
- **Yocoli Konan Jean Epiphane**  
- **Fotso Kamel**  
- **Li Jie**

---

## ✨ Fonctionnalités

### 🎮 Deux modes de simulation
- `SimpleUnivers` : implémentation classique avec une grille traditionnelle
- `HashLifeUnivers` : version optimisée utilisant **quadtree** et **mémoïsation**

### 🖥️ Deux interfaces disponibles
- **Interface graphique (Swing)** : manipulation intuitive
- **Interface console** : pour les puristes du terminal

### 🎨 Personnalisation avancée
- Choix parmi plusieurs motifs de départ *(Glider, Pulsar, Gosper Gun, etc.)*
- Palette de couleurs configurable pour les cellules
- Vitesse de simulation ajustable

---

## 🚀 Lancement du projet

### 🔧 Prérequis
- Java JDK 11 ou supérieur

### ⚙️ Compilation et exécution

```bash
# Compiler les fichiers Java
javac -d build src/Main.java

# Lancer l'application
java -cp build src.Main
#ou
java -jar dist/JeuDeLaVie.jar

#test
javac -d build -cp build:junit-platform-console-standalone-1.8.1.jar src/**/*.java tests/*.java
#et
java -jar junit-platform-console-standalone-1.8.1.jar --class-path build --scan-class-path
