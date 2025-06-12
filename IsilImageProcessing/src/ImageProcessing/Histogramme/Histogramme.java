package ImageProcessing.Histogramme;

/**
 * Fonctions utilitaires pour l'analyse d'images en niveaux de gris. Les images
 * sont manipulées sous forme de matrices d'entiers dans l'intervalle [0,255].
 */
public class Histogramme 
{
    /**
     * Calcule l'histogramme 256 niveaux d'une image.
     * Fonction du prof
     */
    public static int[] Histogramme256(int mat[][])
    {
        int M = mat.length;
        int N = mat[0].length;
        int histo[] = new int[256];
        
        for(int i=0 ; i<256 ; i++) histo[i] = 0;
        
        for(int i=0 ; i<M ; i++)
            for(int j=0 ; j<N ; j++)
                if ((mat[i][j] >= 0) && (mat[i][j]<=255)) histo[mat[i][j]]++;
        
        return histo;
    }


    /**
     * Cherche la plus petite intensité présente dans l'image.
     *
     * @param image matrice d'intensités
     * @return valeur minimale rencontrée
     */
    public static int minimum(int[][] image) {
        int min = 255;
        for (int[] row : image) {
            for (int pixel : row) {
                if (pixel < min) min = pixel;
            }
        }
        return min;
    }


    /**
     * Cherche la plus grande intensité présente dans l'image.
     *
     * @param image matrice d'intensités
     * @return valeur maximale rencontrée
     */
    public static int maximum(int[][] image) {
        int max = 0;
        for (int[] row : image) {
            for (int pixel : row) {
                if (pixel > max) max = pixel;
            }
        }
        return max;
    }


    /**
     * Calcule la luminance moyenne de l'image selon la formule (1.28).
     *
     * @param image matrice d'intensités
     * @return valeur moyenne des niveaux de gris
     */
    public static int luminance(int[][] image) {
        long sum = 0;
        int totalPixels = 0;

        for (int[] row : image) {
            for (int pixel : row) {
                sum += pixel;
                totalPixels++;
            }
        }

        return (int)(sum / totalPixels);
    }


    /**
     * Premier indicateur de contraste : écart-type des niveaux de gris
     * tel que défini par la formule (1.29).
     *
     * @param image matrice d'intensités
     * @return écart type des intensités
     */
    public static double contraste1(int[][] image) {
        int M = image.length;
        int N = image[0].length;

        double moy = luminance(image);
        double somme = 0.0;
        double totalPixels = M * N;

        for (int[] row : image) {
            for (int pixel : row) {
                double diff = pixel - moy;
                somme += diff * diff;
            }
        }

        return Math.sqrt(somme / totalPixels);
    }


    /**
     * Second indicateur de contraste : rapport dynamique
     * (max - min) / (max + min) comme dans la formule (1.30).
     *
     * @param image matrice d'intensités
     * @return valeur normalisée du contraste
     */
    public static double contraste2(int[][] image)
    {
        int min = minimum(image);
        int max = maximum(image);

        if (max + min == 0) return 0.0;

        return (double)(max - min) / (double)(max + min);
    }
}
