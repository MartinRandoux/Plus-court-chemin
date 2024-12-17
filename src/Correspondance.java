import java.util.Map;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class Correspondance {
    private String ville;
    private ModaliteTransport mt1;
    private ModaliteTransport mt2;
    private Map<TypeCout, Double> coutParTypeCout;

    public Correspondance(String ville, ModaliteTransport mt1, ModaliteTransport mt2, Map<TypeCout, Double> coutParTypeCout){
        this.ville = ville;
        this.mt1 = mt1;
        this.mt2 = mt2;
        this.coutParTypeCout = coutParTypeCout;
    }

    public String getVille() {
        return ville;
    }

    public ModaliteTransport getMt1() {
        return mt1;
    }

    public ModaliteTransport getMt2() {
        return mt2;
    }

    public double getCout(TypeCout typeCout) {
        return coutParTypeCout.get(typeCout);
    }
}