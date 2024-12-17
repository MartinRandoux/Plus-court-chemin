import java.util.Map;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

public class Chemin implements Trancon {
    private Sommet depart;
    private Sommet arrive;
    private ModaliteTransport modalite;
    private Map<TypeCout, Double> coutParTypeCout;

    public Chemin(Sommet depart, Sommet arrive, ModaliteTransport modalite, Map<TypeCout, Double> coutParTypeCout){
        this.depart = depart;
        this.arrive = arrive;
        this.modalite = modalite;
        this.coutParTypeCout = coutParTypeCout;
    }

    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Chemin other = (Chemin) obj;
        if(this.depart == null){
            if(other.depart != null) return false;
        }else if(!this.depart.equals(other.depart)) return false;

        if(this.arrive == null){
            if(other.arrive != null) return false;
        }else if(!this.arrive.equals(other.arrive)) return false;

        if(this.modalite != other.modalite) return false;

        if(!this.coutParTypeCout.equals(other.coutParTypeCout)) return false;

        return true;
    }

    public double getCout(TypeCout typeCout) {
        return coutParTypeCout.get(typeCout);
    }

    public Sommet getArrivee(){
        return this.arrive;
    }

    @Override
    public Sommet getDepart() {
        return this.depart;
    }

    @Override
    public ModaliteTransport getModalite() {
        return modalite;
    }

    @Override
    public String toString() {
        return this.modalite + " de " + this.depart + " à " + this.arrive;
    }
}
