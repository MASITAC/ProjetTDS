package ImageProcessing.Lineaire;

import ImageProcessing.Complexe.Complexe;
import ImageProcessing.Complexe.MatriceComplexe;
import ImageProcessing.Fourier.Fourier;

public class FiltrageLineaireGlobal {


    public static int[][] filtrePasseBasIdeal(int[][] image, int frequenceCoupure) {
        int hauteur = image.length;
        int largeur = image[0].length;

        // besoin de double pour fct de fourrier
        double[][] imageDouble = new double[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                imageDouble[i][j] = image[i][j];
                System.out.println(imageDouble[i][j]);
            }
        }
        //  Fourier 2D
        MatriceComplexe spectre = Fourier.Fourier2D(imageDouble);
        spectre = Fourier.decroise(spectre);
        //  masque passe-bas
        int centreX = largeur / 2;
        int centreY = hauteur / 2;

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                double distance = Math.sqrt(Math.pow(i - centreY, 2) + Math.pow(j - centreX, 2));
                if (distance > frequenceCoupure) {
                    spectre.set(i, j, new Complexe(0.0, 0.0));
                }
            }
        }
        spectre = Fourier.decroise(spectre);
        // Fourier inverse
        MatriceComplexe imageFiltree = Fourier.InverseFourier2D(spectre);

        // recuperation partie reelle
        int[][] resultat = new int[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                double val = imageFiltree.get(i, j).getPartieReelle();
                val = Math.round(val);
                val = Math.max(0, Math.min(255, val)); // clamp 0-255
                resultat[i][j] = (int) val;
            }
        }

        return resultat;
    }

    public static int[][] filtrePasseHautIdeal(int[][] image, int frequenceCoupure) {
        int hauteur = image.length;
        int largeur = image[0].length;

        // besoin de double pour fct de fourrier
        double[][] imageDouble = new double[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                imageDouble[i][j] = image[i][j];
            }
        }
        //  Fourier 2D
        MatriceComplexe spectre = Fourier.Fourier2D(imageDouble);
        spectre = Fourier.decroise(spectre);
        //  masque passe-bas
        int centreX = largeur / 2;
        int centreY = hauteur / 2;

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                double distance = Math.sqrt(Math.pow(i - centreY, 2) + Math.pow(j - centreX, 2));
                if (distance < frequenceCoupure) {
                    spectre.set(i, j, new Complexe(0.0, 0.0));
                }
            }
        }
        spectre = Fourier.decroise(spectre);
        // Fourier inverse
        MatriceComplexe imageFiltree = Fourier.InverseFourier2D(spectre);

        // recuperation partie reelle
        int[][] resultat = new int[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                double val = imageFiltree.get(i, j).getPartieReelle();
                val = Math.round(val);
                val = Math.max(0, Math.min(255, val)); // clamp 0-255
                resultat[i][j] = (int) val;
            }
        }

        return resultat;
    }

    public static int[][] filtrePasseBasButterworth(int[][] image, int frequenceCoupure, int ordre) {
        int hauteur = image.length;
        int largeur = image[0].length;

        double[][] imageDouble = new double[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                imageDouble[i][j] = image[i][j];
            }
        }

        MatriceComplexe spectre = Fourier.Fourier2D(imageDouble);
        spectre = Fourier.decroise(spectre);

        int centreX = largeur / 2;
        int centreY = hauteur / 2;
        MatriceComplexe tfFiltree = new MatriceComplexe(hauteur, largeur);


        for (int u = 0; u < hauteur; u++) {
            for (int v = 0; v < largeur; v++) {
                // Calcul de la distance au centre de la fréquence
                double du = u - centreY;
                double dv = v - centreX;
                double D = Math.sqrt(du * du + dv * dv);

                // Fonction de transfert Butterworth
                double H = 1.0 / (1.0 + Math.pow(D / frequenceCoupure, 2.0 * ordre));

                // Multiplier la valeur complexe par H (filtrage)
                Complexe val = spectre.get(u, v);
                double re = val.getPartieReelle() * H;
                double im = val.getPartieImaginaire() * H;
                tfFiltree.set(u, v, new Complexe(re, im));
            }
        }
        // Inverse FFT
        tfFiltree = Fourier.decroise(tfFiltree);
        MatriceComplexe imageFiltree = Fourier.InverseFourier2D(tfFiltree);

        // Décentrage et conversion en int
        int[][] resultat = new int[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                double val = imageFiltree.get(i, j).getPartieReelle();
                val = Math.round(val);
                val = Math.max(0, Math.min(255, val)); // clamp 0-255
                resultat[i][j] = (int) val;
            }
        }
        return resultat;
    }

    public static int[][] filtrePasseHautButterworth(int [][] image, int frequenceCoupure, int ordre) {
        int hauteur = image.length;
        int largeur = image[0].length;

        double[][] imageDouble = new double[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                imageDouble[i][j] = image[i][j];
            }
        }

        MatriceComplexe spectre = Fourier.Fourier2D(imageDouble);
        spectre = Fourier.decroise(spectre);

        int centreX = largeur / 2;
        int centreY = hauteur / 2;
        MatriceComplexe tfFiltree = new MatriceComplexe(hauteur, largeur);


        for (int u = 0; u < hauteur; u++) {
            for (int v = 0; v < largeur; v++) {
                // Calcul de la distance au centre de la fréquence
                double du = u - centreY;
                double dv = v - centreX;
                double D = Math.sqrt(du * du + dv * dv);

                // Fonction de transfert Butterworth
                double H = 1.0 / (1.0 + Math.pow(frequenceCoupure/D, 2.0 * ordre));

                // Multiplier la valeur complexe par H (filtrage)
                Complexe val = spectre.get(u, v);
                double re = val.getPartieReelle() * H;
                double im = val.getPartieImaginaire() * H;
                tfFiltree.set(u, v, new Complexe(re, im));
            }
        }
        // Inverse FFT
        tfFiltree = Fourier.decroise(tfFiltree);
        MatriceComplexe imageFiltree = Fourier.InverseFourier2D(tfFiltree);

        // Décentrage et conversion en int
        int[][] resultat = new int[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                double val = imageFiltree.get(i, j).getPartieReelle();
                val = Math.round(val);
                val = Math.max(0, Math.min(255, val)); // clamp 0-255
                resultat[i][j] = (int) val;
            }
        }
        return resultat;
    }


}
