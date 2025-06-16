package ImageProcessing.Contours;

import ImageProcessing.NonLineaire.MorphoElementaire;

public class ContoursNonLineaire {


    /**
     * Vérifie et force une valeur à rester entre 0 et 255
     * Permet d'éviter des débordements pendant les calculs sur les images
     */
    private static int verify(int value) {
        if (value < 0) return 0;
        if (value > 255) return 255;
        return value;
    }

    /**
     * Calcule le gradient d’érosion : image - erosion(image)
     * Permet de faire ressortir les contours sombres
     */
    public static int[][] gradientErosion(int[][] image) {
        int[][] erosion = MorphoElementaire.erosion(image, 3); // Erosion avec un noyau 3x3
        int Height = image.length;
        int Width = image[0].length;
        int[][] res = new int[Height][Width];

        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                // On applique le gradient d’érosion et on vérifie le résultat
                res[i][j] = verify(image[i][j] - erosion[i][j]);
            }
        }

        return res;
    }


    /**
     * Calcule le gradient de dilatation : dilatation(image) - image
     * Permet de faire ressortir les contours clairs
     */
    public static int[][] gradientDilatation(int[][] image) {
        int[][] dilatation = MorphoElementaire.dilatation(image, 3); // Dilatation 3x3
        int Height = image.length;
        int Width = image[0].length;
        int[][] res = new int[Height][Width];

        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                res[i][j] = verify(dilatation[i][j] - image[i][j]);
            }
        }
        return res;
    }


    /**
     * Calcule le gradient de Beucher : dilatation(image) - erosion(image)
     * Permet de donne une idée complète du contraste local, utile pour les contours nets.
     */
    public static int[][] gradientBeucher(int[][] image) {
        int[][] dilatation = MorphoElementaire.dilatation(image, 3);
        int[][] erosion = MorphoElementaire.erosion(image, 3);

        int Height = image.length;
        int Width = image[0].length;
        int[][] res = new int[Height][Width];

        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                res[i][j] = verify(dilatation[i][j] - erosion[i][j]);
            }
        }
        return res;
    }


    /**
     * Calcule le laplacien non-linéaire : dilatation - érosion
     * Met en évidence les zones de transition fortes, alternative au laplacien linéaire
     */
    public static int[][] laplacienNonLineaire(int[][] image) {
        int[][] dilatation = MorphoElementaire.dilatation(image, 3);
        int[][] erosion = MorphoElementaire.erosion(image, 3);

        int Height = image.length;
        int Width = image[0].length;
        int[][] res = new int[Height][Width];

        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                int val = dilatation[i][j] - erosion[i][j];
                res[i][j] = verify(Math.abs(val)); // on prend la valeur absolue au cas où
            }
        }

        return res;
    }
}
