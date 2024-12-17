import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SommetTest{
    public Sommet s1, s2, s3;

    @BeforeEach
    public void initialization(){
        this.s1 = new Sommet("villeA");
		this.s2 = new Sommet("villeB");
		this.s3 = s1;
    }

    @Test
    public void getNameTest(){
        assertEquals("villeA", this.s1.getName());
        assertEquals("villeB", this.s2.getName());
        assertEquals("villeA", this.s3.getName());
    }

    @Test
    public void equalsTest(){
        assertTrue(s1.equals(s3));
        assertFalse(s1.equals(s2));
        assertFalse(s2.equals(s3));
    }
}