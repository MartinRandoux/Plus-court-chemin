import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtilisateurTest{
    public Utilisateur u1, u2, u3, u4, u5;

    @BeforeEach
    public void initialization(){
        this.u1 = new Utilisateur("Pierre", TypeCout.TEMPS, 200, 60, 4);
		this.u2 = new Utilisateur("Mathéo", TypeCout.PRIX, 100, 180, 5);
		this.u3 = u1;
		this.u4 = new Utilisateur(TypeCout.TEMPS);
		this.u5 = new Utilisateur(TypeCout.CO2);
    }

    @Test
    public void borneTest(){
        assertEquals(60, this.u1.getBorne());
        assertEquals(100, this.u2.getBorne());
        assertEquals(60, this.u3.getBorne());
        assertEquals(180, this.u4.getBorne());
        assertEquals(5, this.u5.getBorne());
    }
}