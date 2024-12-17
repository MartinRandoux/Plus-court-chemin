import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportDonnee {
    public static ArrayList<String> importDonneCSV(String cheminFichier){
        ArrayList<String> donnee = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))){
            
            String ligne = br.readLine();
            while (ligne != null) {
                donnee.add(ligne);
                ligne = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            validiteDonnee(donnee);
        } catch (DonneeFournieInvalideException e) {
            System.out.println(e.getMessage() + cheminFichier);
            return new ArrayList<String>();
        }

        return donnee;
    }

    public static boolean validiteDonnee(ArrayList<String> data) throws DonneeFournieInvalideException{
        for (String ligne : data) {
            if(!analyseLigne(ligne)){
                return false;
            }
        }
        return true;
    }

    public static boolean analyseLigne(String ligne) throws DonneeFournieInvalideException{
        int nbPointsVirgule = 0;
        for (int i = 0; i < ligne.length(); i++) {
            if (ligne.charAt(i) == ';') {
                nbPointsVirgule++;
            }
        }

        if (nbPointsVirgule != 5) {
            throw new DonneeFournieInvalideException("Les données fournie ne sont pas valide pour le fichier ");
        }
        return nbPointsVirgule == 5;
    }
}
