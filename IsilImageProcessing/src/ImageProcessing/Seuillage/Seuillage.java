package ImageProcessing.Seuillage;

public class Seuillage {


    /**
     * Seuillage simple :
     *      Pixels >= seuil → 255 (blanc)
     *      Pixels < seuil → 0 (noir)
     */
    public static int[][] seuillageSimple(int[][] image, int seuil) {
        int h = image.length;
        int w = image[0].length;
        int[][] res = new int[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                res[i][j] = image[i][j] > seuil ? 255 : 0;
            }
        }

        return res;
    }


    /**
     * Seuillage double avec deux seuils :
     *      Pixels < seuil1  → 0 (noir)
     *      Pixels entre seuil1 et seuil2 → 128 (gris)
     *      Pixels >= seuil2 → 255 (blanc)
     */
    public static int[][] seuillageDouble(int[][] image, int seuil1, int seuil2) {
        int h = image.length;
        int w = image[0].length;
        int[][] res = new int[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int val = image[i][j];

                if (val < seuil1)
                    res[i][j] = 0;
                else if (val < seuil2)
                    res[i][j] = 128;
                else
                    res[i][j] = 255;
            }
        }

        return res;
    }


    /**
     * Seuillage automatique par l'algorithme itératif :
     *      Initialisation du seuil comme la moyenne globale.
     *      Réitération jusqu'à stabilisation du seuil.
     */
    public static int[][] seuillageAutomatique(int[][] image) {
        int h = image.length;
        int w = image[0].length;

        // Étape 1 : calcul du seuil initial (moyenne de l’image)
        int total = 0;
        for (int[] row : image) {
            for (int pixel : row) {
                total += pixel;
            }
        }
        int seuil = total / (h * w);

        int newSeuil = seuil;
        do {
            seuil = newSeuil;

            // Moyenne des pixels < seuil
            int sum1 = 0, count1 = 0;
            int sum2 = 0, count2 = 0;

            for (int[] row : image) {
                for (int pixel : row) {
                    if (pixel < seuil) {
                        sum1 += pixel;
                        count1++;
                    } else {
                        sum2 += pixel;
                        count2++;
                    }
                }
            }

            int m1 = count1 == 0 ? 0 : sum1 / count1;
            int m2 = count2 == 0 ? 0 : sum2 / count2;

            newSeuil = (m1 + m2) / 2;

        } while (Math.abs(newSeuil - seuil) > 1); // Convergence

        // Application du seuillage simple final avec le seuil trouvé
        return seuillageSimple(image, newSeuil);
    }
}
