import agent.Agent;
import agent.Problem;
import agent.StrategyEnum;
import environnement.City;
import environnement.CitySize;
import environnement.Road;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by adrie on 05/04/2018.
 */
public class Aventurier {

    public static void main(String[] args) {

        // Cities
        ArrayList<City> cities = new ArrayList<>();
        City paris = new City(1, "Paris", CitySize.BIG, 750);
        City orleans = new City(2, "Orleans", CitySize.BIG, 664);
        City troyes = new City(3, "Troyes", CitySize.MEDIUM, 634);
        City bourges = new City(4, "Bourges", CitySize.MEDIUM, 558);
        City auxerre = new City(5, "Auxerre", CitySize.SMALL, 592);
        City clermont = new City(6, "Clermont-Ferrand", CitySize.MEDIUM, 388);
        City nevers = new City(7, "Nevers", CitySize.SMALL, 515);
        City lyon = new City(8, "Lyon", CitySize.BIG, 313);
        City millau = new City(9, "Millau", CitySize.SMALL, 251);
        City saintEtienne = new City(10, "Saint-Etienne", CitySize.MEDIUM, 296);
        City valence = new City(11, "Valence", CitySize.MEDIUM, 213);
        City montpellier = new City(12, "Montpellier", CitySize.BIG, 154);
        City marseille = new City(13, "Marseille", CitySize.BIG, 0);

        cities.add(paris);
        cities.add(orleans);
        cities.add(troyes);
        cities.add(bourges);
        cities.add(auxerre);
        cities.add(clermont);
        cities.add(nevers);
        cities.add(lyon);
        cities.add(millau);
        cities.add(saintEtienne);
        cities.add(valence);
        cities.add(montpellier);
        cities.add(marseille);

        // Roads
        ArrayList<Road> roads = new ArrayList<>();
        Road road1a = new Road(1, 117, 4, paris, orleans);
        Road road2a = new Road(2, 166, 3, paris, auxerre);
        Road road3a = new Road(3, 249, 3, paris, lyon);
        Road road4a = new Road(4, 153, 4, paris, troyes);
        Road road5a = new Road(5, 106, 1, orleans, bourges);
        Road road6a = new Road(6, 150, 3, orleans, nevers);
        Road road7a = new Road(7, 130, 3, auxerre, bourges);
        Road road8a = new Road(8, 106, 2, auxerre, nevers);
        Road road9a = new Road(9, 280, 2, auxerre, lyon);
        Road road10a = new Road(10, 249, 3, auxerre, clermont);
        Road road11a = new Road(11, 63, 4, bourges, nevers);
        Road road12a = new Road(12, 402, 3, bourges, millau);
        Road road13a = new Road(13, 320, 4, troyes, lyon);
        Road road14a = new Road(14, 320, 3, troyes, clermont);
        Road road15a = new Road(15, 377, 2, nevers, millau);
        Road road16a = new Road(16, 219, 4, nevers, saintEtienne);
        Road road17a = new Road(17, 214, 5, nevers, lyon);
        Road road18a = new Road(18, 313, 2, lyon, marseille);
        Road road19a = new Road(19, 100, 2, lyon, valence);
        Road road20a = new Road(20, 211, 1, clermont, valence);
        Road road21a = new Road(21, 102, 3, millau, montpellier);
        Road road22a = new Road(22, 252, 1, saintEtienne, montpellier);
        Road road23a = new Road(23, 296, 2, saintEtienne, marseille);
        Road road24a = new Road(24, 213, 2, valence, marseille);
        Road road25a = new Road(25, 154, 3, montpellier, marseille);

        roads.add(road1a);
        roads.add(road2a);
        roads.add(road3a);
        roads.add(road4a);
        roads.add(road5a);
        roads.add(road6a);
        roads.add(road7a);
        roads.add(road8a);
        roads.add(road9a);
        roads.add(road10a);
        roads.add(road11a);
        roads.add(road12a);
        roads.add(road13a);
        roads.add(road14a);
        roads.add(road15a);
        roads.add(road16a);
        roads.add(road17a);
        roads.add(road18a);
        roads.add(road19a);
        roads.add(road20a);
        roads.add(road21a);
        roads.add(road22a);
        roads.add(road23a);
        roads.add(road24a);
        roads.add(road25a);

        // Adjacency matrix
        Road[][] graphMatrix = new Road[13][13];
        int i, j;
        for (Road road : roads
                ) {
            i = road.getCityA().getId() - 1;
            j = road.getCityB().getId() - 1;
            graphMatrix[i][j] = road;
            graphMatrix[j][i] = road;
        }

        // Debug : print matrix
        //PrintMatrix(graphMatrix);

        System.out.println("Map created.");

        boolean quit = false;
        Scanner keyboard = new Scanner(System.in);
        int selection;
        Agent agent;
        Problem problem;

        while (!quit) {
            // Create the problem
            problem = new Problem(graphMatrix, paris, marseille);
            problem.setStraightlinedistance(750);

            // Choose strategy for the agent
            System.out.println("Enter the integer matching your choice.");
            i = 1;
            for (StrategyEnum strategyEnum : StrategyEnum.values()){
                System.out.println(i + ". " +strategyEnum.toString());
                i++;
            }
            System.out.println("0. to quit the application.");
            selection = keyboard.nextInt();

            switch (selection) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    agent = new Agent(problem, StrategyEnum.FOODFOCUS);
                    agent.run();
                    break;
                case 2:
                    agent = new Agent(problem, StrategyEnum.ENERGYFOCUS);
                    agent.run();
                    break;
                case 3:
                    agent = new Agent(problem, StrategyEnum.RISKFOCUS);
                    agent.run();
                    break;
                case 4:
                    agent = new Agent(problem, StrategyEnum.SURVIVALFOCUS);
                    agent.run();
                    break;
                case 5:
                    agent = new Agent(problem, StrategyEnum.SHORTESTPATHFOCUS);
                    agent.run();
                    break;
                case 6:
                    agent = new Agent(problem, StrategyEnum.MINMAX);
                    agent.run();
                    break;
                case 7:
                    agent = new Agent(problem, StrategyEnum.RANDOM);
                    agent.run();
                    break;
                case 8:
                    agent = new Agent(problem, StrategyEnum.HECATOMBE);
                    agent.run();
                    break;
                default:
                    System.out.println("Please enter a valide number.");
            }

            System.out.println();
            System.out.println("Press Enter to continue.");
            try {
                System.in.read();
            }
            catch (Exception e){

            }
        }
    }

}
