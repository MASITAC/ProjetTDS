package ImageProcessing.Lineaire;

import ImageProcessing.Complexe.Complexe;
import ImageProcessing.Complexe.MatriceComplexe;
import ImageProcessing.Fourier.Fourier;

public class FiltrageLineaireLocal {

    public static int[][] filtreMasqueConvolution(int[][] image, double[][] masque) {
        int hauteur = image.length;
        int largeur = image[0].length;
        int tailleMasque = masque.length;

        // Vérification que le masque est carré et de taille impaire
        if (tailleMasque % 2 == 0 || tailleMasque != masque[0].length) {
            throw new IllegalArgumentException("Le masque doit être carré et de taille impaire.");
        }

        int demiTaille = tailleMasque / 2;
        int[][] resultat = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                double somme = 0.0;

                for (int u = -demiTaille; u <= demiTaille; u++) {
                    for (int v = -demiTaille; v <= demiTaille; v++) {
                        int x = i + u;
                        int y = j + v;

                        // Gestion des bords : extension par symétrie (padding miroir)
                        x = Math.max(0, Math.min(x, hauteur - 1));
                        y = Math.max(0, Math.min(y, largeur - 1));
                        somme += image[x][y] * masque[u + demiTaille][v + demiTaille];
                    }
                }
                // Clamp des valeurs entre 0 et 255 pour rester dans une image 8 bits
                resultat[i][j] = Math.min(Math.max((int) Math.round(somme), 0), 255);
            }
        }

        return resultat;
    }


    public static int[][] filtreMoyenneur(int[][] image, int tailleMasque) {
        if (tailleMasque % 2 == 0 || tailleMasque <= 0) {
            throw new IllegalArgumentException("La taille du masque doit être impaire et positive.");
        }

        double valeur = 1.0 / (tailleMasque * tailleMasque);
        double[][] masque = new double[tailleMasque][tailleMasque];

        for (int i = 0; i < tailleMasque; i++) {
            for (int j = 0; j < tailleMasque; j++) {
                masque[i][j] = valeur;
            }
        }

        return filtreMasqueConvolution(image, masque);
    }



}
