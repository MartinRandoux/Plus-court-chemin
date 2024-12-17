import fr.ulille.but.sae_s2_2024.Lieu;

public class Sommet implements Lieu{
    private String name;

    public Sommet(String name){
        this.name = name;
    }

    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Sommet other = (Sommet) obj;
        if(this.name == null){
            if(other.name != null) return false;
        }else if(!this.name.equals(other.name)) return false;

        return true;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
