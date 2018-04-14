package agent;

import environnement.City;
import environnement.Road;

/**
 * Created by adrie on 05/04/2018.
 */
public class Agent {

    protected int energy;
    protected int food;
    protected int maxenergy;
    protected int maxfood;
    protected boolean isFine;
    protected String strategy;
    protected Road nextRoad;
    protected Problem problem;

    public Agent(Problem prob){
        maxenergy=100;
        maxfood=100;
        energy=maxenergy;
        food=maxfood;
        problem=prob;
        strategy="bite";
        isFine=true;
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
            food+=nextRoad.getCityB().getFood();
            energy+=nextRoad.cityB.getEnergy();
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
