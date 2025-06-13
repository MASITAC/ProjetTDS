package ImageProcessing.Contours;

public class ContoursLineaire {


    /**
     * Applique un masque 3x3 à une image (filtrage spatial).
     * @param image l’image en niveaux de gris
     * @param masque le masque de convolution 3x3
     * @return l’image filtrée
     */
    public static int[][] appliqueMasque(int[][] image, int[][] masque) {
        int M = image.length;
        int N = image[0].length;
        int[][] resultat = new int[M][N];

        // Parcours de l'image en évitant les bords (comme on à besoin des 8 voisins pour le calcul)
        for (int i = 1; i < M - 1; i++) {
            for (int j = 1; j < N - 1; j++) {
                int somme = 0;

                // Application du masque 3x3 centré sur (i,j)
                for (int u = -1; u <= 1; u++) {
                    for (int v = -1; v <= 1; v++) {
                        somme += image[i + u][j + v] * masque[u + 1][v + 1];
                    }
                }

                // Valeur absolue + saturation à 255
                resultat[i][j] = Math.min(255, Math.abs(somme));
            }
        }

        return resultat;
    }


    /**
     * Calcule le gradient de Prewitt de l’image.
     * @param image l’image à traiter
     * @param dir 1 = horizontal (bords verticaux), 2 = vertical (bords horizontaux)
     * @return l’image filtrée par le masque de Prewitt
     */
    public static int[][] gradientPrewitt(int[][] image, int dir) {
        int[][] masque;
        if (dir == 1) { // horizontal
            masque = new int[][] {
                    {-1, 0, 1},
                    {-1, 0, 1},
                    {-1, 0, 1}
            };
        } else if (dir == 2) { // vertical
            masque = new int[][] {
                    {1, 1, 1},
                    {0, 0, 0},
                    {-1, -1, -1}
            };
        } else {
            return null;
        }
        return appliqueMasque(image, masque);
    }


    /**
     * Calcule le gradient de Sobel de l’image.
     * @param image l’image à traiter
     * @param dir 1 = horizontal (bords verticaux), 2 = vertical (bords horizontaux)
     * @return l’image filtrée par le masque de Sobel
     */
    public static int[][] gradientSobel(int[][] image, int dir) {
        int[][] masque;
        if (dir == 1) { // horizontal
            masque = new int[][] {
                    {-1, 0, 1},
                    {-2, 0, 2},
                    {-1, 0, 1}
            };
        } else if (dir == 2) { // vertical
            masque = new int[][] {
                    {1, 2, 1},
                    {0, 0, 0},
                    {-1, -2, -1}
            };
        } else {
            return null;
        }
        return appliqueMasque(image, masque);
    }


    /**
     * Calcule le laplacien 4 de l’image
     * @param image l’image à traiter
     * @return l’image filtrée par le masque laplacien 4
     */
    public static int[][] laplacien4(int[][] image) {
        int[][] masque = new int[][] {
                {0, -1, 0},
                {-1, 4, -1},
                {0, -1, 0}
        };
        return appliqueMasque(image, masque);
    }


    /**
     * Calcule le laplacien 8 de l’image
     * @param image l’image à traiter
     * @return l’image filtrée par le masque laplacien 8
     */
    public static int[][] laplacien8(int[][] image) {
        int[][] masque = new int[][] {
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}
        };
        return appliqueMasque(image, masque);
    }
}
