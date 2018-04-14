package agent;

import environnement.City;
import environnement.Road;

/**
 * Created by adrie on 05/04/2018.
 */
public class Agent {

    protected int energy;
    protected int food;
    protected boolean isFine;
    protected String strategy;
    protected Road nextRoad;
    protected Problem problem;

    public Agent(Problem prob){
        energy=100;
        food=100;
        problem=prob;
        strategy="bite";
    }


    public void chooseNextMove(){
        switch (strategy){
            case "Brutal":chooseNextMoveBrutal();break;
            default:chooseNextMoveBrutal();break;
        }
    }

    public void updateStatus(){
        if(food<=0||energy<=0){
            isFine=false;
        }
    }




    public void move(){
        food = Integer.max(0, food-nextRoad.getFood());
        energy = Integer.max(0, energy-nextRoad.getEnergyCost());
        updateStatus();

        problem.setCurrentCity(nextRoad.getCityB());
        nextRoad.printRoad();
        nextRoad=null;
    }

    public void chooseNextMoveBrutal(){
        Road next = problem.GetAccessibleRoads(problem.getCurrentCity()).get(0);
        for(Road road: problem.GetAccessibleRoads(problem.getCurrentCity())){
            if(road.cityB.getDistanceToGoal()<=next.cityB.getDistanceToGoal()){
                next = road;
            }
        }
        nextRoad=next;
    }

    public void run(){

        while(!problem.getCurrentCity().equals(problem.getGoalCity())){
            chooseNextMove();
            move();
        }

    }



}
