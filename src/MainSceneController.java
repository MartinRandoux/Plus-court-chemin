import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class MainSceneController {
    Historique historique;
    ArrayList<String> donneeChemin;
    ArrayList<String> donneeCorrespondance;
    ObservableList<Sommet> listeSommet;
    ObservableList<ModaliteTransport> listeModaliteTransports;
    ObservableList<TypeCout> listeCritere;
    Label[] listeLabelHistorique;
    Label[] listeAutreTrajet;

    Map<TypeCout, Double> borneTypeCout;

    Utilisateur user;
    Plateforme p;

    boolean simpleModaliteTransport;
    ModaliteTransport transportVoulue;

    double[] preference;

    Sommet depart, arrivee;
    int numeroTrajet;
    ArrayList<Trajet> trajets;

    @FXML
    private Ellipse ePoidAffichage;

    @FXML
    private Button btnOuvrirHistorique;

    @FXML
    private ChoiceBox<TypeCout> cbCritere;

    @FXML
    private ChoiceBox<ModaliteTransport> cbSimpleModalite;

    @FXML
    private ComboBox<Sommet> cbVilleArrive;

    @FXML
    private ComboBox<Sommet> cbVilleDepart;

    @FXML
    private VBox conteneurHistorique;

    @FXML
    private ImageView ivTemps;

    @FXML
    private ImageView ivPrix;

    @FXML
    private Label lblAutreTrajet1;

    @FXML
    private Label lblAutreTrajet2;

    @FXML
    private Label lblAutreTrajet3;

    @FXML
    private Label lblAutreTrajet4;

    @FXML
    private Label lblHistorique1;

    @FXML
    private Label lblHistorique2;

    @FXML
    private Label lblHistorique3;

    @FXML
    private Label lblHistorique4;

    @FXML
    private Label lblHistorique5;

    @FXML
    private Label lblAffichagePoids;

    @FXML
    private CheckBox simpleModalite;

    @FXML
    private Slider sliderCritPrix;

    @FXML
    private Slider sliderCritTemps;

    @FXML
    private Slider sliderPrefCo2;

    @FXML
    private Slider sliderPrefPrix;

    @FXML
    private Slider sliderPrefTemps;

    @FXML
    private TextField tfCritPrix;

    @FXML
    private TextField tfCritTemps;

    @FXML
    private VBox vbHistorique;

    @FXML
    private VBox vbAffichageTrajet;

    @FXML
    public void initialize(){
        historique = Historique.chargerHistorique("res/historique.csv");
        donneeChemin = ImportDonnee.importDonneCSV("res/data.csv");
        donneeCorrespondance = ImportDonnee.importDonneCSV("res/donneeCorrespondance.csv");

        borneTypeCout = new HashMap<TypeCout, Double>();
        borneTypeCout.put(TypeCout.TEMPS, 200.0);
        borneTypeCout.put(TypeCout.PRIX, 200.0);

        user = new Utilisateur(TypeCout.PRIX, borneTypeCout);
        p = new Plateforme();

        simpleModaliteTransport = false;
        transportVoulue = null;

        p.creerReseau(donneeChemin);
        p.ajouterCorrespondance(donneeCorrespondance);

        preference = new double[]{100.0, 100.0, 100.0};

        listeSommet = FXCollections.observableArrayList();
        listeModaliteTransports = FXCollections.observableArrayList();
        listeCritere = FXCollections.observableArrayList();

        for (Sommet sommet : p.getSommetsReseau()) {
            listeSommet.add(sommet); 
        }

        cbVilleDepart.setItems(listeSommet);
        cbVilleArrive.setItems(listeSommet);
        cbVilleDepart.setValue(listeSommet.get(0));
        cbVilleArrive.setValue(listeSommet.get(1));

        listeModaliteTransports.addAll(ModaliteTransport.values());
        listeCritere.addAll(TypeCout.values());

        cbSimpleModalite.setItems(listeModaliteTransports);
        cbCritere.setItems(listeCritere);
        cbCritere.setValue(listeCritere.get(0));

        Image imagePrix = new Image("res/prix.png");
        Image imageTemps = new Image("res/temps.png");
        ivPrix.setImage(imagePrix);
        ivTemps.setImage(imageTemps);

        listeLabelHistorique = new Label[]{lblHistorique1, lblHistorique2, lblHistorique3, lblHistorique4, lblHistorique5};
        listeAutreTrajet = new Label[]{lblAutreTrajet1, lblAutreTrajet2, lblAutreTrajet3, lblAutreTrajet4};

        ePoidAffichage.setVisible(false);
        lblAffichagePoids.setText("");

        for (int i = 0; i < 5; i++) {
            if(i < historique.getHistoriques().size()){
                listeLabelHistorique[i].setText(historique.getHistoriques().get(i).toString());
            }else{
                listeLabelHistorique[i].setText("");
            }
            listeLabelHistorique[i].setVisible(false);
        }

        for (int i = 0; i < listeAutreTrajet.length; i++) {
            listeAutreTrajet[i].setText("");
        }

        numeroTrajet = 0;
    }

    @FXML
    void modifSliderCritPrix(MouseEvent event) {
        tfCritPrix.setText("" + (int) sliderCritPrix.getValue());
    }

    @FXML
    void modifSliderCritTemps(MouseEvent event) {
        tfCritTemps.setText("" + (int) sliderCritTemps.getValue());
    }

    @FXML
    void modifTfCritPrix(ActionEvent event) {
        sliderCritPrix.setValue(Integer.parseInt(tfCritPrix.getText()));
    }

    @FXML
    void modifTfCritTemps(ActionEvent event) {
        sliderCritTemps.setValue(Integer.parseInt(tfCritTemps.getText()));
    }

    @FXML
    void modifierSimpleModaliteTransport(ActionEvent event) {
        if (simpleModalite.isSelected()) {
            simpleModaliteTransport = true;
            cbSimpleModalite.setDisable(false);
            cbSimpleModalite.setValue(listeModaliteTransports.get(0));
        }else{
            simpleModaliteTransport = false;
            cbSimpleModalite.setDisable(true);
            cbSimpleModalite.setValue(null);
        }
    }

    @FXML
    void ouvrirFermerHistorique(ActionEvent event) {
        if (vbHistorique.getWidth() == 300.0) {
            btnOuvrirHistorique.setText("Ouvrir l'historique");
            vbHistorique.setPrefWidth(125.0);
            vbHistorique.setBorder(null);
            for (int i = 0; i < listeLabelHistorique.length; i++) {
                listeLabelHistorique[i].setVisible(false);
            }
        }else {
            btnOuvrirHistorique.setText("Fermer l'historique");
            vbHistorique.setPrefWidth(300.0);
            vbHistorique.setBorder(new Border(new BorderStroke(Color.valueOf("#9E9E9E"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            for (int i = 0; i < listeLabelHistorique.length; i++) {
                listeLabelHistorique[i].setVisible(true);
            }
        }
    }

    @FXML
    void rechercheHistorique1(MouseEvent event) {
        if (historique.getHistoriques().size() > 0) {
            rechercheHistorique(historique.getHistoriques().get(0));
        }
    }

    @FXML
    void rechercheHistorique2(MouseEvent event) {
        if (historique.getHistoriques().size() > 1) {
            rechercheHistorique(historique.getHistoriques().get(1));
        }
    }

    @FXML
    void rechercheHistorique3(MouseEvent event) {
        if (historique.getHistoriques().size() > 2) {
            rechercheHistorique(historique.getHistoriques().get(2));
        }
    }

    @FXML
    void rechercheHistorique4(MouseEvent event) {
        if (historique.getHistoriques().size() > 3) {
            rechercheHistorique(historique.getHistoriques().get(3));
        }
    }

    @FXML
    void rechercheHistorique5(MouseEvent event) {
        if (historique.getHistoriques().size() > 4) {
            rechercheHistorique(historique.getHistoriques().get(4));
        }
    }

    void rechercheHistorique(Historique h){
        double prix = h.getBorneTypeCout().get(TypeCout.PRIX);
        double temps = h.getBorneTypeCout().get(TypeCout.TEMPS);
        sliderCritPrix.setValue((int) prix);
        sliderCritTemps.setValue((int) temps);
        tfCritPrix.setText("" + (int) prix);
        tfCritTemps.setText("" + (int) temps);

        cbCritere.setValue(h.getCritere());

        sliderPrefTemps.setValue(h.getPreference()[0]);
        sliderPrefPrix.setValue(h.getPreference()[1]);
        sliderPrefCo2.setValue(h.getPreference()[2]);

        simpleModalite.setSelected(h.isSimpleModaliteTransport());

        user.setBorneTypeCout(h.getBorneTypeCout());
        user.setCritere(h.getCritere());
        preference = h.getPreference();

        if (h.isSimpleModaliteTransport()) {
            cbSimpleModalite.setDisable(false);
            transportVoulue = h.getTransportVoulue();

        }else{
            transportVoulue = null;
            cbSimpleModalite.setDisable(true);
        }
        cbSimpleModalite.setValue(transportVoulue);

        Sommet s1 = null;
        Sommet s2 = null;
        try {
            s1 = p.getSommetParNom(h.getVilleDepart());
            s2 = p.getSommetParNom(h.getVilleArrive());

            cbVilleDepart.setValue(s1);
            cbVilleArrive.setValue(s2);

            trajets = p.kpcc(user.getCritere(), s1, s2, transportVoulue, user.getBorne(), preference);

            afficherTrajetSelectionner(trajets.get(numeroTrajet));

            for (int i = 0; i < 4; i++) {
                if(i < trajets.size()-1){
                    listeAutreTrajet[i].setText(trajets.get(i+1).affichageLabel());
                }else{
                    listeAutreTrajet[i].setText("");
                }
            }

        } catch (VilleNonTrouveException e) {
            System.out.println(e.getMessage());
        } catch (AucunTrajetTouveException e) {
            System.out.println(e.getMessage());
        }

        
    }

    @FXML
    void rechercherButton(ActionEvent event) {
        user.setBorneTypeCout(TypeCout.TEMPS, Integer.parseInt(tfCritTemps.getText()));
        user.setBorneTypeCout(TypeCout.PRIX, Integer.parseInt(tfCritPrix.getText()));
        user.setCritere(cbCritere.getValue());

        preference[0] = sliderPrefTemps.getValue();
        preference[1] = sliderPrefPrix.getValue();
        preference[2] = sliderPrefCo2.getValue();

        if (simpleModaliteTransport) {
            transportVoulue = cbSimpleModalite.getValue();
        }else{
            transportVoulue = null;
        }
        
        Sommet s1 = null;
        Sommet s2 = null;
        try {
            s1 = p.getSommetParNom(cbVilleDepart.getValue().getName());
            s2 = p.getSommetParNom(cbVilleArrive.getValue().getName());
            trajets = p.kpcc(user.getCritere(), s1, s2, transportVoulue, user.getBorne(), preference);

            afficherTrajetSelectionner(trajets.get(numeroTrajet));

            for (int i = 0; i < 4; i++) {
                if(i < trajets.size()-1){
                    listeAutreTrajet[i].setText(trajets.get(i+1).affichageLabel());
                }else{
                    listeAutreTrajet[i].setText("");
                }
            }

        } catch (VilleNonTrouveException e) {
            System.out.println(e.getMessage());
        } catch (AucunTrajetTouveException e) {
            System.out.println(e.getMessage());
        }

        historique.ajouterRecherche(new Historique(user.getCritere(), user.getBorne(), simpleModaliteTransport, transportVoulue, s1.getName(), s2.getName(), preference));
        Historique.sauvegardeHistorique(historique);

        for (int i = 0; i < 5; i++) {
            if(i < historique.getHistoriques().size()){
                listeLabelHistorique[i].setText(historique.getHistoriques().get(i).toString());
            }else{
                listeLabelHistorique[i].setText("");
            }
        }
    }

    @FXML
    void changerAffichage1(MouseEvent event) {
        echangerTrajet(lblAutreTrajet1, 1);
    }

    @FXML
    void changerAffichage2(MouseEvent event) {
        echangerTrajet(lblAutreTrajet2, 2);
    }

    @FXML
    void changerAffichage3(MouseEvent event) {
        echangerTrajet(lblAutreTrajet3, 3);
    }

    @FXML
    void changerAffichage4(MouseEvent event) {
        echangerTrajet(lblAutreTrajet4, 4);
    }

    void echangerTrajet(Label label, int nouveauTrajetSelectionner){
        label.setText(trajets.get(0).affichageLabel());
        

        afficherTrajetSelectionner(trajets.get(nouveauTrajetSelectionner));
    }

    void afficherTrajetSelectionner(Trajet trajet){
        vbAffichageTrajet.getChildren().clear();

        

        String[] elementTrajet = trajet.getTexteTrajet().split(", ");
        for (int i = 0; i < elementTrajet.length; i++) {
            HBox hb = new HBox();
            hb.setAlignment(Pos.CENTER_LEFT);
            Circle circle = new Circle(100, 150, 25, Color.ORANGE);
            String element = elementTrajet[i];
            Label l = new Label(element.split(" ")[2]);
            l.setPadding(new Insets(0, 0, 0, 20));
            l.setFont(new Font(15));

            hb.getChildren().addAll(circle, l);

            HBox milieu = new HBox();
            milieu.setPrefWidth(50);
            milieu.setPadding(new Insets(0, 0, 0, 25));
            Line ligne = new Line(0, 0, 0, 50);
            ligne.setStrokeWidth(2);

            Label lm = new Label(element.split(" ")[0]);
            lm.setPadding(new Insets(12, 0, 0, 20));
            lm.setFont(new Font(15));

            milieu.getChildren().addAll(ligne, lm);


            vbAffichageTrajet.getChildren().addAll(hb, milieu);
        }
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);
        Circle circle = new Circle(100, 150, 25, Color.ORANGE);
        String element = elementTrajet[elementTrajet.length-1];
        Label l = new Label(element.split(" ")[4]);
        l.setPadding(new Insets(0, 0, 0, 20));
        l.setFont(new Font(15));

        hb.getChildren().addAll(circle, l);
        vbAffichageTrajet.getChildren().add(hb);

        ePoidAffichage.setVisible(true);
        lblAffichagePoids.setText(trajet.getTextePoids());
        lblAffichagePoids.setVisible(true);

    }
}
