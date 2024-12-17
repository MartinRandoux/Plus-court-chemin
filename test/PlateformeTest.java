import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlateformeTest{
    public Plateforme p;
    public ArrayList<String> data1, data2;
    public Sommet s1, s2, s3;
    

    @BeforeEach
    public void initialization(){
        this.p = new Plateforme();

        data1 = new ArrayList<String>();
        data1.add("villeA;villeB;Train;60;1.7;80");
        data1.add("villeB;villeD;Train;22;2.4;40");
        data1.add("villeA;villeC;Train;42;1.4;50");

        data2 = new ArrayList<String>();
        data2.add("villeA;villeB;Train;60;1.7;80");
        data2.add("villeB;villeD;Train;22;2.4;40");
        data2.add("villeA;villeC;Train;42");

        this.s1 = new Sommet("villeA");
        this.s2 = new Sommet("villeB");
        this.s3 = new Sommet("villeC");

        p.addSommet(s1);
    }

    @Test
    public void validiteDonneeTest(){
        assertTrue(this.p.validiteDonnee(data1));
        assertFalse(this.p.validiteDonnee(data2));
    }

    @Test
    public void analyseLigneTest(){
        assertTrue(this.p.analyseLigne(data2.get(1)));
        assertFalse(this.p.analyseLigne(data2.get(2)));
    }

    @Test
    public void verifSommetDejaExistantTest(){
        assertTrue(this.p.verifSommetDejaExistant("villeA"));
        assertFalse(this.p.verifSommetDejaExistant("villeB"));
        p.addSommet(s2);
        assertTrue(this.p.verifSommetDejaExistant("villeB"));
    }

    @Test
    public void getSommetParNomTest(){
        assertEquals(s1, this.p.getSommetParNom("villeA"));
    }
}