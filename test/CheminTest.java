import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class CheminTest{
    public Sommet s1, s2, s3, s4;
    public Chemin c1, c2, c3, c4;

    @BeforeEach
    public void initialization(){
        this.s1 = new Sommet("villeA");
		this.s2 = new Sommet("villeB");
        this.s3 = new Sommet("villeC");
		this.s4 = s1;

        Map<TypeCout, Double> coutParTypeCout = new HashMap<>();
        coutParTypeCout.put(TypeCout.TEMPS, 180.0);
        coutParTypeCout.put(TypeCout.CO2, 5.0);
        coutParTypeCout.put(TypeCout.PRIX, 200.0);

        this.c1 = new Chemin(s1, s2, ModaliteTransport.TRAIN, coutParTypeCout);
        this.c2 = new Chemin(s2, s3, ModaliteTransport.AVION, coutParTypeCout);
        this.c3 = new Chemin(s3, s4, ModaliteTransport.TRAIN, coutParTypeCout);
        this.c4 = new Chemin(s3, s4, ModaliteTransport.TRAIN, coutParTypeCout);
    }

    @Test
    public void equalsTest(){
        assertFalse(c1.equals(c3));
        assertFalse(c1.equals(c2));
        assertTrue(c3.equals(c4));
    }

    @Test
    public void getCoutTest(){
        assertEquals(5.0, c1.getCout(TypeCout.CO2));
        assertEquals(200.0, c2.getCout(TypeCout.PRIX));
        assertEquals(180.0, c3.getCout(TypeCout.TEMPS));
    }
}