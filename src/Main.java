import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class Main {
    public static void main(String[] args){
        Historique historique = Historique.chargerHistorique("res/historique.csv");
        ArrayList<String> donneeChemin = ImportDonnee.importDonneCSV("res/data.csv");
        ArrayList<String> donneeCorrespondance = ImportDonnee.importDonneCSV("res/donneeCorrespondance.csv");

        Map<TypeCout, Double> borneTypeCout = new HashMap<TypeCout, Double>();
        borneTypeCout.put(TypeCout.TEMPS, 200.0);
        borneTypeCout.put(TypeCout.PRIX, 200.0);

        Utilisateur user = new Utilisateur(TypeCout.PRIX, borneTypeCout);
        Plateforme p = new Plateforme();

        boolean simpleModaliteTransport = false;
        ModaliteTransport transportVoulue = null;


        p.creerReseau(donneeChemin);
        p.ajouterCorrespondance(donneeCorrespondance);
            
        if (simpleModaliteTransport) {
            transportVoulue = ModaliteTransport.TRAIN;
        }

        double[] preference = new double[]{100.0, 100.0, 100.0};
        Sommet s1 = null;
        Sommet s2 = null;
        try {
            s1 = p.getSommetParNom("L");
            s2 = p.getSommetParNom("M");
            ArrayList<Trajet> trajets = p.kpcc(user.getCritere(), s1, s2, transportVoulue, user.getBorne(), preference);
            for (Trajet trajet : trajets) {
                System.out.println(trajet.toString());
            }

        } catch (VilleNonTrouveException e) {
            System.out.println(e.getMessage());
        } catch (AucunTrajetTouveException e) {
            System.out.println(e.getMessage());
        }

        historique.ajouterRecherche(new Historique(user.getCritere(), user.getBorne(), simpleModaliteTransport, transportVoulue, s1.getName(), s2.getName(), preference));
        Historique.sauvegardeHistorique(historique);    
    }
}
