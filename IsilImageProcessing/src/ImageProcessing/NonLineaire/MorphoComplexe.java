package ImageProcessing.NonLineaire;

import java.util.Arrays;

public class MorphoComplexe {

    public static int[][] dilatationGeodesique(int[][] image, int[][] masqueGeodesique, int nbIter) {
        int[][] resultat = copyImage(image);

        for (int k = 0; k < nbIter; k++) {
            int[][] dilate  = dilatation(resultat);
            resultat = intersection(dilate, masqueGeodesique);

            System.out.println("iteration : " + k);
        }

        return resultat;
    }

    public static int[][] reconstructionGeodesique(int[][] image, int[][] masqueGeodesique) {
        int[][] resultat = copyImage(image);
        boolean stable;
        int i = 0 ;
        do {
            int[][] precedent = resultat;
            int[][] dilate = dilatation(resultat);
            resultat = intersection(dilate, masqueGeodesique);
            stable = imagesEgales(precedent, resultat);
            System.out.println("iteration : " + i);
            i++;
        } while (!stable);

        return resultat;
    }

    public static int[][] filtreMedian(int[][] image, int tailleMasque) {
        int hauteur = image.length;
        int largeur = image[0].length;
        int rayon = tailleMasque / 2;
        int[][] resultat = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                int[] voisins = new int[tailleMasque * tailleMasque];
                int idx = 0;

                for (int di = -rayon; di <= rayon; di++) {
                    for (int dj = -rayon; dj <= rayon; dj++) {
                        int ni = i + di;
                        int nj = j + dj;
                        if (ni >= 0 && ni < hauteur && nj >= 0 && nj < largeur) {
                            voisins[idx++] = image[ni][nj];
                        }
                    }
                }

                Arrays.sort(voisins, 0, idx);
                // on retourne donc la valeur dans le nombre impair présnete au centre => voir concept de median
                resultat[i][j] = voisins[idx / 2];
            }
        }

        return resultat;
    }

    // -----------------------------
    // Fonctions internes auxiliaires
    // -----------------------------

    private static int[][] dilatation(int[][] image) {
        int hauteur = image.length;
        int largeur = image[0].length;
        int[][] resultat = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                int max = image[i][j];

                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        int ni = i + di;
                        int nj = j + dj;

                        if (ni >= 0 && ni < hauteur && nj >= 0 && nj < largeur) {
                            max = Math.max(max, image[ni][nj]);
                        }
                    }
                }

                resultat[i][j] = max;
            }
        }

        return resultat;
    }

    private static int[][] intersection(int[][] image1, int[][] image2) {
        int hauteur = image1.length;
        int largeur = image1[0].length;
        int[][] resultat = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                resultat[i][j] = Math.min(image1[i][j], image2[i][j]);
            }
        }

        return resultat;
    }

    private static boolean imagesEgales(int[][] img1, int[][] img2) {
        for (int i = 0; i < img1.length; i++) {
            for (int j = 0; j < img1[0].length; j++) {
                if (img1[i][j] != img2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] copyImage(int[][] image) {
        int[][] copie = new int[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            System.arraycopy(image[i], 0, copie[i], 0, image[0].length);
        }
        return copie;
    }
}
