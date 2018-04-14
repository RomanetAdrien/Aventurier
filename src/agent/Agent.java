package agent;

import environnement.City;
import environnement.Road;

import java.util.List;

/**
 * Created by adrie on 05/04/2018.
 */
public class Agent {

    protected int energy;
    protected int food;
    protected boolean isFine;
    // protected String strategy;
    protected Road nextRoad;
    protected Problem problem;
    protected StrategyEnum strategy;

    public Agent(Problem prob, StrategyEnum strategy){
        energy=100;
        food=100;
        problem=prob;
        this.strategy=strategy;
    }


    public void chooseNextMove(){
        switch (strategy){
            case NONE:chooseNextMoveBrutal();break;
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

    public Road GetBestRoad(List<Road> roadList){
        Road bestRoad = null;
        float bestDesirability = 0;
        float roadDesirability = 0;

        for (Road road : roadList) {
            roadDesirability = GetDesirability(road);
            if( roadDesirability > bestDesirability) {
                bestDesirability = roadDesirability;
                bestRoad = road;
            }
        }

        return bestRoad;
    }

    public float GetDesirability(Road road){
        float desirability = 0;
        int foodBoost = 1, energyBoost = 1, riskBoost = 1, distanceBoost = 1;

        switch(this.strategy){
            case FOODFOCUS:
                foodBoost = 2;

                break;
            case ENERGYFOCUS:
                energyBoost = 2;
                break;
            case RISKFOCUS:
                riskBoost = 2;
                break;
            case SURVIVALFOCUS:

                break;
            case SHORTESTPATHFOCUS:
                distanceBoost = 2;
                break;
            default:
                break;
        }

        return desirability;
    }

    public void run(){

        while(!problem.getCurrentCity().equals(problem.getGoalCity())){
            chooseNextMove();
            move();
        }

    }



}
