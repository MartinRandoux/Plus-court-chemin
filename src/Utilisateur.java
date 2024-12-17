import java.util.Map;

public class Utilisateur {
    private TypeCout critere;
    private Map<TypeCout, Double> borneTypeCout;

    public Utilisateur(TypeCout critere, Map<TypeCout, Double> borneTypeCout){
        this.critere = critere;
        this.borneTypeCout = borneTypeCout;
    }

    public TypeCout getCritere() {
        return critere;
    }

    public double getBorneTypeCout(TypeCout typeCout){
        return this.borneTypeCout.get(typeCout);
    }

    public Map<TypeCout, Double> getBorne(){
        return this.borneTypeCout;
    }

    public void setBorneTypeCout(TypeCout typeCout, double borne){
        this.borneTypeCout.replace(typeCout, borne);
    }

    public void setBorneTypeCout(Map<TypeCout, Double> borneTypeCout){
        this.borneTypeCout = borneTypeCout;
    }

    public void setCritere(TypeCout critere) {
        this.critere = critere;
    }
}