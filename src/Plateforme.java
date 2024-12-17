import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;

public class Plateforme {
    private ArrayList<Chemin> cheminsReseau;
    private ArrayList<Sommet> sommetsReseau;
    private ArrayList<Correspondance> correspondance;

    public Plateforme(){
        this.cheminsReseau = new ArrayList<Chemin>();
        this.sommetsReseau = new ArrayList<Sommet>();
        this.correspondance = new ArrayList<Correspondance>();        
    }

    public ArrayList<Sommet> getSommetsReseau() {
        return sommetsReseau;
    }

    public void addSommet(Sommet s){
        if (!this.sommetsReseau.contains(s)) {
            this.sommetsReseau.add(s);
        }
    }

    public void addChemin(Chemin c){
        this.cheminsReseau.add(c);
    }

    public void ajouterCorrespondance(ArrayList<String> correspondances){
        for (String c : correspondances) {
            String[] ligne = c.split(";");

            Map<TypeCout, Double> coutParTypeCout = new HashMap<>();
            coutParTypeCout.put(TypeCout.TEMPS, Double.parseDouble(ligne[3]));
            coutParTypeCout.put(TypeCout.CO2, Double.parseDouble(ligne[4]));
            coutParTypeCout.put(TypeCout.PRIX, Double.parseDouble(ligne[5]));

            Correspondance correspondance = new Correspondance(ligne[0], ModaliteTransport.valueOf(ligne[1].toUpperCase()), ModaliteTransport.valueOf(ligne[2].toUpperCase()), coutParTypeCout);
            this.correspondance.add(correspondance);
        }
    }

    public boolean verifSommetDejaExistant(String name){
        for (Sommet sommet : this.sommetsReseau) {
            if (sommet.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Sommet getSommetParNom(String name) throws VilleNonTrouveException{
        Sommet s = null;
        for (Sommet sommet : this.sommetsReseau) {
            if (sommet.getName().equals(name)) {
                s = sommet;
            }
        }
        if (s == null) {
            throw new VilleNonTrouveException("La ville " + name + " n'a pas été trouvé");
        }
        return s;
    }

    public Chemin getCheminParToString(String text) throws CheminNonTrouveException{
        Chemin c = null;
        for (Chemin chemin : this.cheminsReseau) {
            if (chemin.toString().equals(text)) {
                c = chemin;
            }
        }
        if (c == null) {
            throw new CheminNonTrouveException("Le chemin [" + text + "] n'a pas été trouvé");
        }
        return c;
    }

    public void creerReseau(ArrayList<String> data){
        for (String ligne : data) {
            String[] lignes = ligne.split(";");

            Sommet[] sommets = new Sommet[2];
            for (int i = 0; i < sommets.length; i++) {
                if(verifSommetDejaExistant(lignes[i])){
                    try {
                        sommets[i] = getSommetParNom(lignes[i]);
                    } catch (VilleNonTrouveException e) {
                        System.out.println(e.getMessage());
                    }
                }else{
                    sommets[i] = new Sommet(lignes[i]);
                }
            }
            
            ModaliteTransport modaliteTransport = ModaliteTransport.valueOf(lignes[2].toUpperCase());

            Map<TypeCout, Double> coutParTypeCout = new HashMap<>();
            coutParTypeCout.put(TypeCout.PRIX, Double.parseDouble(lignes[3]));
            coutParTypeCout.put(TypeCout.CO2, Double.parseDouble(lignes[4]));
            coutParTypeCout.put(TypeCout.TEMPS, Double.parseDouble(lignes[5]));

            Chemin chemin = new Chemin(sommets[0], sommets[1], modaliteTransport, coutParTypeCout);

            addSommet(sommets[0]);
            addSommet(sommets[1]);
            addChemin(chemin);
        }
    }

    public ArrayList<Trajet> kpcc(TypeCout typeCout, Sommet depart, Sommet arrive, ModaliteTransport modaliteTransportUnique, Map<TypeCout, Double> limitePoid, double[] preference) throws AucunTrajetTouveException {
        MultiGrapheOrienteValue graphe = new MultiGrapheOrienteValue();

        for (Sommet sommet : this.sommetsReseau) {
            graphe.ajouterSommet(sommet);
        }

        for (Chemin chemin : this.cheminsReseau) {
            if (modaliteTransportUnique != null && chemin.getModalite() == modaliteTransportUnique) {
                graphe.ajouterArete(chemin, chemin.getCout(typeCout));
            }else if(modaliteTransportUnique == null){
                graphe.ajouterArete(chemin, chemin.getCout(typeCout));
            }
        }
        
        List<fr.ulille.but.sae_s2_2024.Chemin> resAlgo = AlgorithmeKPCC.kpcc(graphe, depart, arrive, 5);
        ArrayList<Trajet> trajets = new ArrayList<Trajet>();
        String ligne;
        
        for (fr.ulille.but.sae_s2_2024.Chemin chemin : resAlgo) {
            ligne = chemin.toString();
            Trajet trajet = new Trajet(ligne, calculPoids(ligne));
            trajet.setScore(calculScore(trajet, preference));
            if(poidInferieur(trajet, limitePoid)){  
                trajets.add(trajet);
            }
        }

        if (trajets.isEmpty()) {
            throw new AucunTrajetTouveException("Aucun trajet satisfaisant trouvé entre " + depart.getName() + " et " + arrive.getName() + " avec une durée inférieure à "+ limitePoid.get(TypeCout.TEMPS) + " et un prix inférieur à " + limitePoid.get(TypeCout.PRIX));
        }

        return organiseSelonPreference(trajets, preference);
    }

    public Map<TypeCout, Double> calculPoids(String ligne){
        String[] chemins = ligne.substring(ligne.indexOf("[")+1, ligne.indexOf("]")).split(", ");
        Map<TypeCout, Double> poids = new HashMap<TypeCout, Double>();
        double temps = 0;
        double prix = 0;
        double CO2 = 0;

        for(int i=0; i<chemins.length; i++){
            try {
                Chemin c = getCheminParToString(chemins[i]);
                temps += c.getCout(TypeCout.TEMPS);
                prix += c.getCout(TypeCout.PRIX);
                CO2 += c.getCout(TypeCout.CO2);  
            } catch (CheminNonTrouveException e) {
                System.out.println(e.getMessage());
            }
        }
        temps += ajoutPoidSiCorrespondance(ligne, TypeCout.TEMPS);
        prix += ajoutPoidSiCorrespondance(ligne, TypeCout.PRIX);
        CO2 += ajoutPoidSiCorrespondance(ligne, TypeCout.CO2);

        poids.put(TypeCout.TEMPS, temps);
        poids.put(TypeCout.CO2, CO2);
        poids.put(TypeCout.PRIX, prix);

        return poids;
    }

    public boolean poidInferieur(Trajet trajet, Map<TypeCout, Double> limitePoid){
        return trajet.getPoids(TypeCout.TEMPS) < limitePoid.get(TypeCout.TEMPS) && trajet.getPoids(TypeCout.PRIX) < limitePoid.get(TypeCout.PRIX);
    }

    public ArrayList<Trajet> organiseSelonPreference(ArrayList<Trajet> trajets, double[] preference) {
        ArrayList<Trajet> ordreAffichage = new ArrayList<Trajet>();
    
        int tailleTrajet = trajets.size();
        for (int i = 0; i < tailleTrajet; i++) {
            int indicePlusPetit = 0;
            for (int j=1; j<trajets.size(); j++) {
                if (trajets.get(indicePlusPetit).getScore() > trajets.get(j).getScore()) {
                    indicePlusPetit = j;
                }
            }
            ordreAffichage.add(trajets.get(indicePlusPetit));
            trajets.remove(indicePlusPetit);
            
        }
        return ordreAffichage;
    }

    public double calculScore(Trajet trajet, double[] preference){
        return trajet.getPoids(TypeCout.TEMPS)*preference[0] + trajet.getPoids(TypeCout.PRIX)*preference[1] + trajet.getPoids(TypeCout.CO2)*20*preference[2];
    }


    public double ajoutPoidSiCorrespondance(String ligne, TypeCout typeCout){
        double poid = 0;
        String[] trajet = ligne.substring(ligne.indexOf("[")+1, ligne.indexOf("]")).split(", ");

        for(int i=0; i<trajet.length-1; i++){
            String[] t1 = trajet[i].split(" ");
            String[] t2 = trajet[i+1].split(" ");
            if (t1[4].equals(t2[2]) && !t1[0].equals(t2[0])) {
                for (Correspondance c : this.correspondance) {
                    ModaliteTransport mt1 = ModaliteTransport.valueOf(t1[0].toUpperCase());
                    ModaliteTransport mt2 = ModaliteTransport.valueOf(t2[0].toUpperCase());
                    if(c.getVille().equals(t1[4]) && (c.getMt1() == mt1 || c.getMt1() == mt2) && (c.getMt2() == mt1 || c.getMt2() == mt2)){
                        poid += c.getCout(typeCout);
                    }
                }
            }
        }


        return poid;
    }
}