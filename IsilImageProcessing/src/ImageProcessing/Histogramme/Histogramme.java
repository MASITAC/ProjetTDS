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
        int Height = image.length;
        int Width = image[0].length;

        double moy = luminance(image);
        double somme = 0.0;
        double totalPixels = Height * Width;

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

    // ----------------------

    /**
     * Applique une courbe tonale sur l'image.
     * Chaque niveau de gris i est remplace par la valeur
     * courbeTonale[i].
     *
     * @param image        matrice d'intensites de l'image d'origine
     * @param courbeTonale table de 256 valeurs entre 0 et 255
     * @return nouvelle image rehaussee
     */
    public static int[][] rehaussement(int[][] image, int[] courbeTonale) {
        int Height = image.length;
        int Width = image[0].length;
        int[][] res = new int[Height][Width];

        for (int i = 0; i < Height; i++) {
            for (int j = 0; j < Width; j++) {
                int val = image[i][j];
                res[i][j] = courbeTonale[val];
            }
        }
        return res;
    }


    /**
     * Cree une courbe tonale correspondant a une transformation
     * lineaire avec saturation telle que definie dans la formule (1.32).
     *
     * @param smin seuil minimum
     * @param smax seuil maximum
     * @return courbe tonale de taille 256
     */
    public static int[] creeCourbeTonaleLineaireSaturation(int smin, int smax) {
        int[] courbe = new int[256];

        for (int i = 0; i < 256; i++) {
            if (i <= smin) {
                courbe[i] = 0;
            } else if (i >= smax) {
                courbe[i] = 255;
            } else {
                courbe[i] = (int) Math.round(255.0 * (i - smin) / (smax - smin));
            }
        }

        return courbe;
    }


    /**
     * Cree une courbe tonale correspondant a la correction gamma
     * (formule 1.33).
     *
     * @param gamma valeur de l'exposant gamma
     * @return courbe tonale de taille 256
     */
    public static int[] creeCourbeTonaleGamma(double gamma) {
        int[] courbe = new int[256];

        for (int i = 0; i < 256; i++) {
            double normalisation = i / 255.0;                           // Normalisation du niveau de gris entre 0 et 1
            double correction = Math.pow(normalisation, 1.0 / gamma);   // Corection Gama
            courbe[i] = (int) Math.round(correction * 255);             // Reconstruction de la vrai intenssité dans l’échelle [0, 255]
        }

        return courbe;
    }


    /**
     * Cree la courbe tonale correspondant au negatif de l'image.
     *
     * @return courbe tonale de taille 256
     */
    public static int[] creeCourbeTonaleNegatif() {
        int[] courbe = new int[256];
        for (int i = 0; i < 256; i++) {
            courbe[i] = 255 - i;
        }
        return courbe;
    }


    /**
     * Cree la courbe tonale correspondant a l'egalisation
     * de l'histogramme de l'image.
     *
     * @param image matrice d'intensites
     * @return courbe tonale de taille 256
     */
    public static int[] creeCourbeTonaleEgalisation(int[][] image) {
        int[] histogramme = Histogramme256(image);
        int totalPixels = image.length * image[0].length;
        int[] courbe = new int[256];

        int[] cdf = new int[256];
        cdf[0] = histogramme[0];
        for (int i = 1; i < 256; i++) {
            cdf[i] = cdf[i - 1] + histogramme[i];
        }

        for (int i = 0; i < 256; i++) {
            courbe[i] = (int) Math.round((cdf[i] * 255.0) / totalPixels);
        }

        return courbe;
    }
}
