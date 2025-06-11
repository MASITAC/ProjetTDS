package ImageProcessing.NonLineaire;

public class MorphoElementaire {

    // Appliquer une érosion : minimum local
    public static int[][] erosion(int[][] image, int tailleMasque) {
        int hauteur = image.length;
        int largeur = image[0].length;
        int rayon = tailleMasque / 2;

        int[][] resultat = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                int min = Integer.MAX_VALUE;

                for (int di = -rayon; di <= rayon; di++) {
                    for (int dj = -rayon; dj <= rayon; dj++) {
                        int ni = i + di;
                        int nj = j + dj;

                        if (ni >= 0 && ni < hauteur && nj >= 0 && nj < largeur) {
                            min = Math.min(min, image[ni][nj]);
                        }
                    }
                }

                resultat[i][j] = min;
            }
        }

        return resultat;
    }

    // Appliquer une dilatation : maximum local
    public static int[][] dilatation(int[][] image, int tailleMasque) {
        int hauteur = image.length;
        int largeur = image[0].length;
        int rayon = tailleMasque / 2;

        int[][] resultat = new int[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                int max = Integer.MIN_VALUE;

                for (int di = -rayon; di <= rayon; di++) {
                    for (int dj = -rayon; dj <= rayon; dj++) {
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

    // Ouverture = Erosion suivie de dilatation
    public static int[][] ouverture(int[][] image, int tailleMasque) {
        return dilatation(erosion(image, tailleMasque), tailleMasque);
    }

    // Fermeture = Dilatation suivie d'érosion
    public static int[][] fermeture(int[][] image, int tailleMasque) {
        return erosion(dilatation(image, tailleMasque), tailleMasque);
    }
}
