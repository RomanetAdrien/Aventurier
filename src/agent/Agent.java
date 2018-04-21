package agent;

import environnement.City;
import environnement.Road;
import javafx.util.Pair;

import java.util.ArrayList;
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

    public Agent(Agent agent){
        new Agent(agent.energy,agent.food,agent.maxenergy,agent.maxfood,agent.isFine,agent.nextRoad,agent.problem,agent.strategy);
    }

    public Agent(int energy, int food, int maxenergy, int maxfood, boolean isFine, Road nextRoad, Problem problem, StrategyEnum strategy) {
        this.energy = energy;
        this.food = food;
        this.maxenergy = maxenergy;
        this.maxfood = maxfood;
        this.isFine = isFine;
        this.nextRoad = nextRoad;
        this.problem = problem;
        this.strategy = strategy;
    }

    public void chooseNextMove(){
        if(this.strategy.equals(StrategyEnum.MINMAX)){
            nextRoad=minimaxDecision(this);
            return;
        }
        if(this.strategy.equals(StrategyEnum.HECATOMBE)){
            nextRoad = sendScouts();
        }
        nextRoad=this.GetBestRoad(problem.GetAccessibleRoads(problem.getCurrentCity()));
    }

    private Road sendScouts() {
        int scoutnumbers=10000000;
        List<Pair<Road,Integer>> distances = new ArrayList<>();
        List<Pair<Road,Integer>> deaths = new ArrayList<>();
        for(Road road : problem.GetAccessibleRoads(problem.getCurrentCity())){
            distances.add(new Pair(road,0));
            deaths.add(new Pair(road,0));
        }
        for(int i=0;i<scoutnumbers;i++){
            AgentScout as = new AgentScout(this);
            as.run();
            if(as.isFine)System.out.println("ARRRIVEEEEEEEEEEEEEEEE");
            for(Pair pair : distances){
                if(pair.getKey().equals(as.firstroad)){
                    pair = new Pair(as.firstroad,(int)pair.getValue()+as.traveledDistance);
                }
            }
            for(Pair pair : deaths){
                if(pair.getKey().equals(as.firstroad)&&!as.isFine){
                    pair = new Pair(as.firstroad,(int)pair.getValue()+1);
                }
            }
        }
        int minimumdeath=Integer.MAX_VALUE;
        Road bestroad=null;
        for(Road road : problem.GetAccessibleRoads(problem.getCurrentCity())){
            for(Pair pair : deaths){
                if(pair.getKey().equals(road) && (int)pair.getValue()<minimumdeath){
                    bestroad=road;
                }
            }
        }
        return bestroad;
    }

    private Road minimaxDecision(Agent agent){
        List<Road> possibilities = agent.problem.GetAccessibleRoads(agent.problem.getCurrentCity());
        if(possibilities.isEmpty()){
            return null;
        }
        float maxdesirability = Integer.MIN_VALUE;
        Road bestroad = possibilities.get(0);

        for(Road road : possibilities){
            Agent tmp = new Agent(energy,food,maxenergy,maxfood,isFine,nextRoad,problem,strategy);
            tmp.nextRoad=road;
            tmp.simulatemove();
            float mindesirability = Integer.MAX_VALUE;
            for(Road furtherroad : tmp.problem.GetAccessibleRoads(tmp.problem.getCurrentCity())){
                if(tmp.GetDesirability(furtherroad)<mindesirability){
                    mindesirability=tmp.GetDesirability(furtherroad);
                }
            }
            if(mindesirability>maxdesirability){
                bestroad=road;
                maxdesirability=mindesirability;
            }
        }
        return bestroad;
    }

    public void updateStatus(){
        if(food<=0||energy<=0){
            isFine=false;
        }
    }

    public int predictedFood(){
        int aftermove=0;
        int afterroad = food-((100*nextRoad.getLength()*nextRoad.getFoodCost())/problem.getStraightlinedistance());
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
        food = Integer.max(0, food-((100*nextRoad.getLength()*nextRoad.getFoodCost())/problem.getStraightlinedistance()));
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

    public void simulatemove(){
        food = Integer.max(0, food-((100*nextRoad.getLength()*nextRoad.getFoodCost())/problem.getStraightlinedistance()));
        energy = Integer.max(0, energy-((100*nextRoad.getLength()*nextRoad.getEnergyCost())/problem.getStraightlinedistance()));
        updateStatus();
        if(isFine){
            food=Integer.min(maxfood,food+nextRoad.getCityB().getFood());
            energy=Integer.min(maxenergy,energy+nextRoad.cityB.getEnergy());
        }

        problem.setCurrentCity(nextRoad.getCityB());
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
        float foodBoost = 1, energyBoost = 1, riskBoost = 1, distanceBoost = 1;
        float boost = 10;

        //System.out.println("Road from : " + road.getCityA().getName() + " to " + road.getCityB().getName());

        float energyCost = (float) road.getEnergyCost()*50*road.getLength()/problem.getStraightlinedistance();
        float foodCost = (float) road.getFoodCost()*50*road.getLength()/problem.getStraightlinedistance();

        // TODO : comment log
//        System.out.println("Energy cost :" + energyCost);
//        System.out.println("Food cost :" + foodCost);

        float foodDesirability = (float) (food+road.getCityB().getFood())/foodCost;
        float energyDesirability = (float) (energy+road.getCityB().getEnergy())/energyCost;
        float distanceDesirability = (float) (road.getLength()+road.getCityB().getDistanceToGoal())/road.getCityB().getDistanceToGoal();
        float riskDesirability = (float) Math.min(food,energy)/(10*road.getRisk());

//        System.out.println("Food desirability :" + foodDesirability);
//        System.out.println("Energy desirability :" + energyDesirability);
//        System.out.println("Distance desirability :" + distanceDesirability);
//        System.out.println("Risk desirability :" + riskDesirability);

        switch(this.strategy){
            case FOODFOCUS:
                foodBoost = boost;
                desirability = foodBoost*foodDesirability + energyBoost*energyDesirability + distanceBoost*distanceDesirability + riskBoost/riskDesirability;
                break;
            case ENERGYFOCUS:
                energyBoost = boost;
                desirability = foodBoost*foodDesirability + energyBoost*energyDesirability + distanceBoost*distanceDesirability + riskBoost/riskDesirability;
                break;
            case RISKFOCUS:
                riskBoost = boost;
                desirability = foodBoost*foodDesirability + energyBoost*energyDesirability + distanceBoost*distanceDesirability + riskBoost/riskDesirability;
                break;
            case SURVIVALFOCUS:
                desirability = foodBoost*foodDesirability + energyBoost*energyDesirability + distanceBoost*distanceDesirability + riskBoost/riskDesirability;
                break;
            case SHORTESTPATHFOCUS:
                distanceBoost = boost;
                desirability = foodBoost*foodDesirability + energyBoost*energyDesirability + distanceBoost*distanceDesirability + riskBoost/riskDesirability;
                break;
            case MINMAX:
                desirability = foodBoost*foodDesirability + energyBoost*energyDesirability + distanceBoost*distanceDesirability + riskBoost/riskDesirability;
                break;
            case HECATOMBE:
                desirability = foodBoost*foodDesirability + energyBoost*energyDesirability + distanceBoost*distanceDesirability + riskBoost/riskDesirability;
                break;
            case RANDOM:
                desirability= (float) Math.random()*100;
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
