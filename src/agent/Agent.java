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
    protected int maxenergy;
    protected int maxfood;
    protected boolean isFine;
    // protected String strategy;
    protected Road nextRoad;
    protected Problem problem;
    protected StrategyEnum strategy;

    public Agent(Problem prob, StrategyEnum strategy){
        maxenergy=100;
        maxfood=100;
        energy=maxenergy;
        food=maxfood;
        problem=prob;
        isFine=true;
        this.strategy=strategy;
    }


    public void chooseNextMove(){
        nextRoad=this.GetBestRoad(problem.GetAccessibleRoads(problem.getCurrentCity()));
    }

    public void updateStatus(){
        if(food<=0||energy<=0){
            isFine=false;
        }
    }

    public int predictedFood(){
        int aftermove=0;
        int afterroad = food-((100*nextRoad.getLength()*nextRoad.getFood())/problem.getStraightlinedistance());
        if(afterroad>0){
            aftermove=afterroad+nextRoad.getCityB().getFood();
        }
        return aftermove;
    }
    public int predictedEnergy(){
        int aftermove=0;
        int afterroad = energy-((100*nextRoad.getLength()*nextRoad.getEnergyCost())/problem.getStraightlinedistance());
        if(afterroad>0){
            aftermove=afterroad+nextRoad.getCityB().getEnergy();
        }
        return aftermove;
    }



    public void move(){
        food = Integer.max(0, food-((100*nextRoad.getLength()*nextRoad.getFood())/problem.getStraightlinedistance()));
        energy = Integer.max(0, energy-((100*nextRoad.getLength()*nextRoad.getEnergyCost())/problem.getStraightlinedistance()));
        updateStatus();
        if(isFine){
            food=Integer.min(maxfood,food+nextRoad.getCityB().getFood());
            energy=Integer.min(maxenergy,energy+nextRoad.cityB.getEnergy());
        }

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

    public void printStatus(){
        System.out.println("Current city : "+problem.getCurrentCity().getName());
        System.out.println("Food level : "+food+"/"+maxfood+ "   Energy level : "+energy+"/"+maxenergy);
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

        while(isFine&&!problem.getCurrentCity().equals(problem.getGoalCity())){
            chooseNextMove();
            move();
            printStatus();
        }
        if(isFine){
            System.out.println("Congratulations ! You arrived in " +problem.getGoalCity().getName()+ " ! (weird choice of destination but whatever)");
        }
        else{
            System.out.println("Sorry you have died on the roads of France, that's why you stay at home travelling is dangerous");
        }

    }



}
