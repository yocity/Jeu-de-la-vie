package src.vuecontroleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.util.Hashtable;
import javax.swing.*;
import src.ReadPattern;
import src.modele.UniversModel;
import src.util.EcouteurModele;

/**
 * Classe qui gere l'interface du jeu de la vie. DESIGN PATTERN : OBSERVATEUR
 */
public class UniversViewPanel extends JPanel implements EcouteurModele, Runnable {

    /**
     * Jeu de la vie
     */
    private UniversModel jeu;
    /**
     * Booleen pour savoir si le jeu est lance ou en pause
     */
    private boolean enCours;
    /**
     * Thread pour l'affichage
     */
    private Thread thread;
    /**
     * Vitesse de le rafraichissement de l'affichage
     */
    private int vitesse;
    /**
     * Zone de texte ou est affiche le numero de la generation en cours ainsi
     * que le nombre cellules vivantes
     */
    private JTextArea infos;

    /**
     * Couleur pour les cellules vivantes
     */
    private Color coulVivantes;
    /**
     * Couleur pour les cellules mortes
     */
    private Color coulMortes;

    /**
     * Couleur marron
     */
    private static final Color MARRON = new Color(103, 68, 0);
    /**
     * Couleur orange
     */
    private static final Color ORANGE = new Color(255, 100, 31);
    /**
     * Couleur verte
     */
    private static final Color VERT = new Color(30, 95, 20);
    /**
     * Couleur bleue
     */
    private static final Color BLEU = new Color(0, 43, 155);
    /**
     * Couleur violette
     */
    private static final Color VIOLET = new Color(129, 0, 185);
    /**
     * Couleur grise
     */
    private static final Color GRIS = new Color(220, 220, 220);

    // Taille de la grille par défaut : 80 x 80
    private int xmax = 125;
    private int ymax = 125;

    /**
     * Constructeur de UniversViewPanel
     *
     * @param jeu jeu de la vie
     */
    public UniversViewPanel(UniversModel jeu) {
        this.jeu = jeu;
        jeu.ajoutEcouteur(this);
        // Valeurs par défaut
        this.vitesse = 5;
        this.coulVivantes = BLEU;
        this.coulMortes = GRIS;
    }

    /**
     * Methode qui permet de rafraichir l'affichage avec la nouvelle generation
     * de cellules
     */
    public void actualise() {
        repaint();
    }

    /**
     * Methode qui permet de mettre en pause l'automate
     */
    public void arreter() {
        this.enCours = false;
    }

    /**
     * Methode qui permet de lancer l'automate => lancement d'un thread.
     */
    public void lancer() {
        this.enCours = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Methode qui permet de permet de dessiner les cellules vivantes
     *
     * @param g Graphics (= une case)
     *
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Taille adaptative : plus grande possible sans déborder
        int cellSize = Math.min(getWidth() / xmax, getHeight() / ymax);

        // Centrer la grille
        int offsetX = (getWidth() - (xmax * cellSize)) / 2;
        int offsetY = (getHeight() - (ymax * cellSize)) / 2;

        for (int x = 0; x < xmax; x++) {
            for (int y = 0; y < ymax; y++) {
                int cell = jeu.getCell(x, y);

                // Choix de la couleur selon l'état
                if (cell == 1) {
                    g.setColor(coulVivantes);
                } else {
                    g.setColor(coulMortes);
                }

                // Dessiner la cellule avec marge de 1px
                int drawX = offsetX + x * cellSize;
                int drawY = offsetY + y * cellSize;
                g.fillRect(drawX, drawY, cellSize - 1, cellSize - 1);
            }
        }
    }

    /**
     * Methode qui permet de creer l'interface du jeu de la vie
     */
    public void creerInterface() {
        /*----------------------------- Gestion de la fenêtre -----------------------------*/
        JFrame fenetre = new JFrame("Le super jeu de la vie");
        fenetre.setResizable(true); // fenetre non redimensionable
        fenetre.setSize(new Dimension(670, 720));// regler la taille
        fenetre.setLocationRelativeTo(null); // centrer la fenetre
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //gestion de la fermeture de la fenetre
        fenetre.setVisible(true); // afficher
        /*----------------------------- Afficher un message explicatif sur le jeu -----------------------------*/
        String message = "Bienvenue dans le super jeu de la vie!\n\n"
                + "Le Jeu de la Vie est un automate cellulaire inventé par John Conway.\n"
                + "Voici comment jouer :\n"
                + "1. Cliquez sur les cellules pour les activer ou les désactiver.\n"
                + "2. Utilisez les boutons 'Lancer', 'Pause' et 'Suivant' pour contrôler le jeu.\n"
                + "3. Ajustez la vitesse avec le curseur.\n"
                + "4. Changez les couleurs des cellules vivantes et mortes avec les menus déroulants.\n"
                + "5. Utilisez le bouton 'Réinitialiser' pour recommencer.\n\n"
                + "Amusez-vous bien!";
        JOptionPane.showMessageDialog(fenetre, message, "Bienvenue", JOptionPane.INFORMATION_MESSAGE);


        /*----------------------------- Gestion de la grille -----------------------------*/
        //this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setPreferredSize(new Dimension(this.ymax * 9, this.xmax * 9));
        fenetre.add(this, BorderLayout.CENTER);

        /*-----------------------------  Gestion de la partie droite de l'application -----------------------------*/
        JPanel droite = new JPanel();
        droite.setLayout(new FlowLayout());
        droite.setPreferredSize(new Dimension(200, 200));
        droite.setMinimumSize(new Dimension(400, 400));

        JPanel haut = new JPanel();
        haut.setLayout(new BorderLayout(5, 25));

        /*----------------------------- Gestion de l'affichage du nombre de générations + nombre de cellules vivantes -----------------------------*/
        JPanel nbGen = new JPanel();
        nbGen.setPreferredSize(new Dimension(400, 50));
        infos = new JTextArea(2, 20);
        infos.setEditable(false);
        infos.setText(jeu.stats() + "\n  Temps écoulé en secondes: " + 0);

        nbGen.add(infos);
        haut.add(nbGen);

        /*----------------------------- Gestion du positionnement des boutons de lancement - pause - mode 'pas à pas' -----------------------------*/
        JPanel boutons = new JPanel();
        boutons.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        // Creation des boutons avec un label et une icone
        JButton btnLancer = new JButton("Lancer", new ImageIcon("play.png"));
        JButton btnPause = new JButton("Pause", new ImageIcon("pause.png"));
        JButton btnSuivant = new JButton("Suivant", new ImageIcon("suivant.png"));

        btnLancer.setPreferredSize(new Dimension(100, 30));
        btnPause.setPreferredSize(new Dimension(100, 30));
        btnSuivant.setPreferredSize(new Dimension(100, 30));
        /*----------------------------- Gestion du bouton de lancement -----------------------------*/
        btnLancer.addActionListener(evenement -> {
            if (!enCours) {
                lancer();
        
            }});

        /*----------------------------- Gestion du bouton de pause -----------------------------*/
        btnPause.addActionListener(evenement -> arreter());

        /*----------------------------- Gestion du bouton pour avancer "pas à pas" -----------------------------*/
        btnSuivant.addActionListener(evenement -> {
            if (enCours) {
                arreter();
            } else {
                long startTime = System.currentTimeMillis();
                jeu.runStep();
                long endTime = System.currentTimeMillis();  // Temps à la fin en millisecondes
                double seconds = (endTime - startTime) / 1000.0;  // Conversion en secondes
                updateAffichage(seconds);
            }
        });

        /*----------------------------- Ajout des boutons -----------------------------*/
        boutons.add(btnLancer);
        boutons.add(btnPause);
        boutons.add(btnSuivant);
        haut.add(boutons, BorderLayout.SOUTH);
        droite.add(haut, BorderLayout.NORTH);

        /*----------------------------- Panneau des options -----------------------------*/
        JPanel options = new JPanel();
        options.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 35));

        /*----------------------------- Gestion du slider pour la vitesse -----------------------------*/
        JSlider s = new JSlider(JSlider.HORIZONTAL, 2, 8, 5);
        // hashtable pour les labels
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(2, new JLabel("Lent"));
        labels.put(8, new JLabel("Rapide"));
        // gestion des espacements (petits traits)
        s.setLabelTable(labels);
        s.setMinorTickSpacing(1);
        s.setMajorTickSpacing(8);
        s.setPaintTicks(true);
        s.setPaintLabels(true);

        /*----------------------------- Gestion de la vitesse -----------------------------*/
        s.addChangeListener(changement -> vitesse = s.getValue());
        options.add(new JLabel("Vitesse"));
        options.add(s);


        /*----------------------------- Gestion du reset -----------------------------*/
        JButton reset = new JButton("Réinitialiser", new ImageIcon("reset.png"));

        // Gestion de la reinitialisation
        reset.addActionListener(evenement -> {
            updateAffichage(0);
            FileReader fr;
            try {
                this.jeu.initialiseGrille(); // Réinitialiser la grille avant de charger un nouveau motif
                fr = new FileReader("Projet/pattern.rle");
                ReadPattern.readPattern(jeu, fr);
            } catch (Exception ex) {
            }
            s.setValue(5);
            vitesse = 5;

        });

        options.add(reset);
        /*------------------------------Choix pattern------------------------------*/
        JPanel choixPattern = new JPanel(new FlowLayout(FlowLayout.TRAILING, 15, 5));
        choixPattern.add(new JLabel("Motif (RLE)"));

        JComboBox<String> listeMotifs = new JComboBox<>();
        listeMotifs.addItem("Glider");
        listeMotifs.addItem("Pulsar");
        listeMotifs.addItem("Gun132");
        listeMotifs.addItem("Gosper Gun");
        listeMotifs.addItem("Glider 2");
        // Ajout d'un item "Aucun" pour réinitialiser
        listeMotifs.setSelectedIndex(0);

        listeMotifs.addActionListener(e -> {
            String choix = listeMotifs.getSelectedItem().toString();
            String nomFichier = "";

            switch (choix) {
                case "Glider 2":
                    nomFichier = "src/pattern.rle1";
                    break;
                case "Pulsar":
                    nomFichier = "src/pattern.rle2";
                    break;
                case "Gun132":
                    nomFichier = "src/gun132.rle3"; // Change si tu as un autre fichier pour celui-ci
                    break;
                case "Gosper Gun":
                    nomFichier = "src/pattern.rle3"; // Change si tu as un autre fichier pour celui-ci
                    break;
                case "Glider":
                    nomFichier = "src/pattern.rle"; // Change si tu as un autre fichier pour celui-ci
                    break;
            }
            this.jeu.initialiseGrille(); // Réinitialiser la grille avant de charger un nouveau motif
            try (FileReader fr = new FileReader(nomFichier)) {
                ReadPattern.readPattern(jeu, fr);
                this.repaint(); // Redessine la grille après chargement
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur dans le fichier RLE !");
            }
        });

        choixPattern.add(listeMotifs);
        options.add(choixPattern);

        /*----------------------------- Gestion du choix des couleurs des cellules vivantes -----------------------------*/
        JPanel choixCoulV = new JPanel();
        choixCoulV.setLayout(new FlowLayout(FlowLayout.TRAILING, 15, 5));
        choixCoulV.add(new JLabel("Cellules vivantes"));

        JComboBox<String> coulV = new JComboBox<>();
        coulV.addItem("Bleu");
        coulV.addItem("Noir");
        coulV.addItem("Marron");
        coulV.addItem("Orange");
        coulV.addItem("Vert");
        coulV.addItem("Violet");
        coulV.addItem("Rouge");
        coulV.setSelectedIndex(0);

        // Gestion du changement de couleur pour les cellules vivantes
        coulV.addActionListener(evenement -> {
            String choix = coulV.getSelectedItem().toString();

            switch (choix) {
                case "Noir":
                    coulVivantes = Color.BLACK;
                    break;
                case "Marron":
                    coulVivantes = MARRON;
                    break;
                case "Orange":
                    coulVivantes = ORANGE;
                    break;
                case "Vert":
                    coulVivantes = VERT;
                    break;
                case "Violet":
                    coulVivantes = VIOLET;
                    break;
                case "Rouge":
                    coulVivantes = Color.RED;
                    break;
                default:
                    coulVivantes = BLEU;
                    break;
            }
        });
        choixCoulV.add(coulV);

        /*----------------------------- Gestion du choix des couleurs des cellules mortes -----------------------------*/
        JPanel choixCoulM = new JPanel();
        choixCoulM.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 5));
        choixCoulM.add(new JLabel("Cellules mortes"));

        JComboBox<String> coulM = new JComboBox<>();
        coulM.addItem("Gris");
        coulM.addItem("Blanc");
        coulM.setSelectedIndex(0);

        // Gestion du changement de couleur pour les cellules mortes
        coulM.addActionListener(evenement -> {
            String choix = coulM.getSelectedItem().toString();

            if (choix.equals("Gris")) {
                coulMortes = GRIS;
            } else {
                coulMortes = Color.WHITE;
            }
        });
        choixCoulM.add(coulM);

        options.add(choixCoulV);
        options.add(choixCoulM);

        droite.add(options, BorderLayout.CENTER);

        fenetre.add(droite, BorderLayout.NORTH);
        fenetre.pack();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellSize = 1 + Math.min(getWidth() / xmax, getHeight() / ymax);
                int offsetX = (getWidth() - (xmax * cellSize)) / 2;
                int offsetY = (getHeight() - (ymax * cellSize)) / 2;

                // Coords relatives à la grille
                int x = (e.getX() - offsetX) / cellSize;
                int y = (e.getY() - offsetY) / cellSize;

                if (x >= 0 && x < xmax && y >= 0 && y < ymax) {
                    jeu.setCellule(x, y);
                    repaint(); // Redessine la grille après modification
                }
            }

        });
    }

    /**
     * Methode qui est lancer quand l'automate est en cours (Thread en cours)
     */
    @Override
    public void run() {
        int attente;
        while (enCours) {
            long startTime = System.currentTimeMillis();
            jeu.runStep();
            long endTime = System.currentTimeMillis();  // Temps à la fin en millisecondes
            double seconds = (endTime - startTime) / 1000.0;  // Conversion en secondes
            updateAffichage(seconds);

            try {
                int tmp = vitesse;
                // Gestion de la vitesse (en fonction de de la position du curseur sur le slider)
                if (tmp == 5) {
                    attente = tmp * 100; 
                }else if (tmp > 5) {
                    attente = 1000 - (tmp * 100); 
                }else {
                    attente = 500 + (tmp * 100);
                }

                Thread.sleep(attente); // temps a attendre entre 2 générations
            } catch (InterruptedException ex) {
                System.out.println("Erreur : " + ex);
            }
        }
    }

    /**
     * Methode qui met a jour l'affichage le numero de la generation et les
     * cellulles vivantes
     */
    public void updateAffichage(double time) {
        this.infos.setText(jeu.stats() + "\n  Temps écoulé en secondes: " + time);
    }

}
