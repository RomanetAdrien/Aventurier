package Other;

import environnement.Road;

public class HelpfulFunctions {

    public static void PrintMatrix(Road[][] graphMatrix) {
        int i, j;
        for (i = 0; i < 13; i++) {
            for (j = 0; j < 13; j++) {
                if (graphMatrix[i][j] != null) {
                    System.out.print(graphMatrix[i][j].getNumber());
                } else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }
    }

    public static float Normalize(float value, float mini, float maxi, float minf, float maxf){
        return ((value-mini)*(maxf-minf)/(maxi-mini) + minf);
    }

    public static void PrintRoad(Road road){
        System.out.println("From : " + road.cityA.getName() + "   To : " + road.cityB.getName());
    }

}
