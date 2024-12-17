import java.text.DecimalFormat;
import java.util.Map;

public class Trajet {
    private String texteTrajet;
    private Map<TypeCout, Double> poids;
    private double score;

    public Trajet(String texte, Map<TypeCout, Double> poids){
        this.texteTrajet = affichageLigne(texte);
        this.poids = poids;
    }

    public double getPoids(TypeCout typeCout) {
        return poids.get(typeCout);
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public String getTexteTrajet() {
        return texteTrajet;
    }

    public String getTextePoids(){
        DecimalFormat df = new DecimalFormat("#.#");
        return "Temps : " + df.format(getPoids(TypeCout.TEMPS)) + "\nCO2 : " + df.format(getPoids(TypeCout.CO2)) + "\nPrix : " + df.format(getPoids(TypeCout.PRIX));
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.#");
        return "[" + this.texteTrajet +"] Temps : " + df.format(getPoids(TypeCout.TEMPS)) + "; CO2 : " + df.format(getPoids(TypeCout.CO2)) + "; Prix : " + df.format(getPoids(TypeCout.PRIX));
    }

    public String affichageLabel(){
        DecimalFormat df = new DecimalFormat("#.#");
        return this.texteTrajet +"\n Temps : " + df.format(getPoids(TypeCout.TEMPS)) + " ; CO2 : " + df.format(getPoids(TypeCout.CO2)) + " ; Prix : " + df.format(getPoids(TypeCout.PRIX));
    }

    public String affichageLigne(String ligne){
        String[] trajet = ligne.substring(ligne.indexOf("[")+1, ligne.indexOf("]")).split(", ");
        String[] nouvTrajet = trajet[0].split(" ");
        String resultat = "";
        
        for(int i=1; i<trajet.length; i++){
            String[] transport2 = trajet[i].split(" ");
            if(nouvTrajet[nouvTrajet.length - 5].equals(transport2[0]) && nouvTrajet[nouvTrajet.length - 1].equals(transport2[2])){
                nouvTrajet[nouvTrajet.length - 1] = transport2[4];
            }else{
                for(int j=0; j<nouvTrajet.length; j++){
                    resultat += nouvTrajet[j];
                    if (j < nouvTrajet.length-1) {
                        resultat += ' ';
                    }else{
                        resultat += ", ";
                    }
                }
                nouvTrajet = trajet[i].split(" ");
            }
        }

        for(int i=0; i<nouvTrajet.length; i++){
            resultat += nouvTrajet[i];
            if (i < nouvTrajet.length-1) {
                resultat += ' ';
            }
        }

        

        return resultat;
    }
}
