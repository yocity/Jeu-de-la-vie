package src;

import java.awt.*;
import java.io.FileReader;
import javax.swing.*;
import src.modele.HashLifeUnivers;
import src.modele.SimpleUnivers;
import src.modele.UniversModel;
import src.vuecontroleur.UniversViewConsole;
import src.vuecontroleur.UniversViewPanel;

public class Main extends JFrame {

    private JComboBox<String> universCombo;
    private JComboBox<String> interfaceCombo;
    private JButton lancerBtn;
    private UniversModel univers;

    public Main() {
        setTitle("Simulation du Jeu de la Vie");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        universCombo = new JComboBox<>(new String[]{"SimpleUnivers", "HashLifeUnivers"});
        interfaceCombo = new JComboBox<>(new String[]{"Console", "Interface graphique"});
        lancerBtn = new JButton("Lancer la simulation");

        add(new JLabel("Choisissez l'univers :"));
        add(universCombo);
        add(new JLabel("Choisissez l'interface :"));
        add(interfaceCombo);
        add(lancerBtn);

        lancerBtn.addActionListener(e -> lancerSimulation());

        setVisible(true);
    }

    private void lancerSimulation() {
        // Choix de l'univers
        String choixUnivers = (String) universCombo.getSelectedItem();
        switch (choixUnivers) {
            case "SimpleUnivers":
                univers = new SimpleUnivers();
                break;
            case "HashLifeUnivers":
                univers = new HashLifeUnivers();
                break;
        }

        // Demander à l'utilisateur de choisir un motif
        String[] motifs = {"Glider", "Glider 2", "Pulsar", "Gosper Gun" , "Gun132"};
        String choixMotif = (String) JOptionPane.showInputDialog(this, "Choisissez un motif",
                "Choix du motif", JOptionPane.QUESTION_MESSAGE,
                null, motifs, motifs[0]);

        if (choixMotif == null) {
            JOptionPane.showMessageDialog(this, "Vous devez choisir un motif pour continuer.");
            return; // Empêcher de quitter sans choix
        }
        String nomFichier = "";

        // Choix du fichier correspondant au motif
        switch (choixMotif) {
            case "Glider 2":
                nomFichier = "src/pattern.rle1";
                break;
            case "Pulsar":
                nomFichier = "src/pattern.rle2";
                break;
            case "Gun132":
                nomFichier = "src/gun132.rle3"; // Change si tu as un autre fichier pour celui-ci
                break;
            case "Glider Gun":
                nomFichier = "src/pattern.rle3"; // Change si tu as un autre fichier pour celui-ci
                break;
            case "Glider":
                nomFichier = "src/pattern.rle"; // Change si tu as un autre fichier pour celui-ci
                break;

        }

        // Lire le motif
        try {
            FileReader fr = new FileReader(nomFichier);
            ReadPattern.readPattern(univers, fr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du motif : " + ex.getMessage());
            return;
        }

        // Choix de l'interface
        String choixInterface = (String) interfaceCombo.getSelectedItem();
        if (choixInterface.equals("Interface graphique")) {
            UniversViewPanel view = new UniversViewPanel(univers);
            dispose();
            view.creerInterface();
        }
        if (choixInterface.equals("Console")) {
            UniversViewConsole console = new UniversViewConsole(univers);
            dispose();
            console.runSimulation();
        }
    }

    public static void lancerDepuisMotif() {
        SwingUtilities.invokeLater(() -> new Main());
    }

    public static void main(String[] args) {
        lancerDepuisMotif();
    }
}
