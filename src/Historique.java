import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class Historique {
    private ArrayList<Historique> historiques;

    private TypeCout critere;
    private Map<TypeCout, Double> borneTypeCout;
    private boolean simpleModaliteTransport;
    private ModaliteTransport transportVoulue;
    private String villeDepart;
    private String villeArrive;
    private double[] preference;

    public Historique(){
        historiques = new ArrayList<Historique>();
    }

    public Historique(TypeCout critere, Map<TypeCout, Double> borneTypeCout, boolean simpleModaliteTransport, ModaliteTransport transportVoulue, String villeDepart, String villeArrive, double[] preference){
        this.critere = critere;
        this.borneTypeCout = borneTypeCout;
        this.simpleModaliteTransport = simpleModaliteTransport;
        this.transportVoulue = transportVoulue;
        this.villeDepart = villeDepart;
        this.villeArrive = villeArrive;
        this.preference = preference;
    }

    public ArrayList<Historique> getHistoriques() {
        return historiques;
    }

    public TypeCout getCritere() {
        return critere;
    }

    public Map<TypeCout, Double> getBorneTypeCout() {
        return borneTypeCout;
    }

    public boolean isSimpleModaliteTransport() {
        return simpleModaliteTransport;
    }

    public ModaliteTransport getTransportVoulue() {
        return transportVoulue;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public String getVilleArrive() {
        return villeArrive;
    }

    public double[] getPreference() {
        return preference;
    }

    public static void sauvegardeHistorique(Historique historiques){
        String csvFile = "res/historique.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFile))) {
            for (Historique h : historiques.historiques) {
                String prefString = h.preference[0] + ";" + h.preference[1] + ";" + h.preference[2];
                pw.write(h.critere.name() + ";" + h.borneTypeCout.get(TypeCout.TEMPS) + ";" + h.borneTypeCout.get(TypeCout.PRIX) + ";" + h.simpleModaliteTransport + ";" + h.transportVoulue + ";" + h.villeDepart + ";" + h.villeArrive + ";" + prefString + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Historique chargerHistorique(String cheminFichier){
        Historique historiques = new Historique();

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))){
            
            String ligne = br.readLine();
            while (ligne != null) {
                String[] l = ligne.split(";");

                TypeCout critere = TypeCout.valueOf(l[0]);
                
                Map<TypeCout, Double> borneTypeCout = new HashMap<TypeCout, Double>();

                borneTypeCout.put(TypeCout.TEMPS, Double.parseDouble(l[1]));
                borneTypeCout.put(TypeCout.PRIX, Double.parseDouble(l[2]));

                boolean simpleModaliteTransport;
                if (l[3].equals("true")) {
                    simpleModaliteTransport = true;
                }else{
                    simpleModaliteTransport = false;
                }
                ModaliteTransport transportVoulue;
                if (l[4].equals("null")) {
                    transportVoulue = null;
                }else{
                    transportVoulue = ModaliteTransport.valueOf(l[4]);
                }
                String villeDepart = l[5];
                String villeArrive = l[6];
                double[] preference = new double[3];
                preference[0] = Double.parseDouble(l[7]);
                preference[1] = Double.parseDouble(l[8]);
                preference[2] = Double.parseDouble(l[9]);

                historiques.ajouterRecherche(new Historique(critere, borneTypeCout, simpleModaliteTransport, transportVoulue, villeDepart, villeArrive, preference), true);

                ligne = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return historiques;
    }

    public void ajouterRecherche(Historique historique){
        ajouterRecherche(historique, false);
    }

    public void ajouterRecherche(Historique historique, boolean importationDonnee){
        if (importationDonnee) {
            this.historiques.addLast(historique);
        }else{
            this.historiques.addFirst(historique);
        }
        if(this.historiques.size() > 5){
            this.historiques.removeLast();
        }
    }

    @Override
    public String toString() {
        return "Voyage de : " + this.villeDepart + " à " + this.villeArrive;
    }
}
