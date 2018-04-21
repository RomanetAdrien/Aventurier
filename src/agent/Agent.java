package agent;

import Other.HelpfulFunctions;
import environnement.City;
import environnement.Road;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrie on 05/04/2018.
 */
public class Agent {

    protected float energy;
    public float GetNormalizedEnergy(){
        return HelpfulFunctions.Normalize(energy,0,100,0,1);
    }

    protected float food;
    public float GetNormalizedFood(){
        return HelpfulFunctions.Normalize(food,0,100,0,1);
    }

    protected float maxenergy;
    protected float maxfood;
    protected boolean isFine;
    protected Road nextRoad;
    protected Problem problem;
    protected StrategyEnum strategy;
    protected ArrayList<City> visitedCities;

    // TODO Adapt values
    private float consumptionFactor = 2;

    public Agent(Problem problem, StrategyEnum strategy) {
        maxenergy = 100;
        maxfood = 100;
        energy = maxenergy;
        food = maxfood;
        this.problem = problem;
        isFine = true;
        this.strategy = strategy;
        this.visitedCities = new ArrayList<>();
        this.visitedCities.add(problem.getCurrentCity());
    }

    public Agent(Agent agent){
        new Agent(agent.energy,agent.food,agent.maxenergy,agent.maxfood,agent.isFine,agent.nextRoad,agent.problem,agent.strategy);
    }

    public Agent(float energy, float food, float maxenergy, float maxfood, boolean isFine, Road nextRoad, Problem problem, StrategyEnum strategy) {
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

    public void updateStatus() {
        if (food <= 0 || energy <= 0) {
            isFine = false;
        }
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

    public float predictedFood(Road road) {
        float aftermove = 0;
        float afterroad = food - consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedFoodCost() / problem.GetNormalizedStraightLinedDistance()));
        // TODO : Delete if valid
        // float afterroad = food - ((50 * road.getLength() * road.getFoodCost()) / problem.getStraightlinedistance());
        if (afterroad > 0) {
            aftermove = afterroad + road.getCityB().getFood();
        }
        return afterroad;
        //return aftermove;
    }

    public float predictedEnergy(Road road) {
        float aftermove = 0;
        float afterroad = energy - consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedEnergyCost() / problem.GetNormalizedStraightLinedDistance()));
        // TODO : Delete if valid
//        float afterroad = energy - ((50 * road.getLength() * road.getEnergyCost()) / problem.getStraightlinedistance());
        if (afterroad > 0) {
            aftermove = afterroad + road.getCityB().getEnergy();
        }
        return afterroad;
//        return aftermove;
    }


    public void move() {
        food = Float.max(0, food - consumptionFactor*100*((nextRoad.GetNormalizedLength()) * nextRoad.GetNormalizedFoodCost()) / problem.GetNormalizedStraightLinedDistance());
        energy = Float.max(0, energy - consumptionFactor*100*((nextRoad.GetNormalizedLength() * nextRoad.GetNormalizedEnergyCost()) / problem.GetNormalizedStraightLinedDistance()));

        // TODO : Delete if valid
//        food = Float.max(0, food - ((50 * nextRoad.getLength() * nextRoad.getFoodCost()) / problem.getStraightlinedistance()));
//        energy = Float.max(0, energy - ((50 * nextRoad.getLength() * nextRoad.getEnergyCost()) / problem.getStraightlinedistance()));
        updateStatus();
        if (isFine&&(nextRoad.getCityB()!=problem.getGoalCity())) {
            food = Float.min(maxfood, food + nextRoad.getCityB().getFood());
            energy = Float.min(maxenergy, energy + nextRoad.cityB.getEnergy());
        }

        problem.setCurrentCity(nextRoad.getCityB());
        visitedCities.add(nextRoad.getCityA());
        HelpfulFunctions.PrintRoad(nextRoad);
        nextRoad = null;
    }

    public void simulatemove(){
        food = Float.max(0, food-((100*nextRoad.getLength()*nextRoad.getFoodCost())/problem.getStraightlinedistance()));
        energy = Float.max(0, energy-((100*nextRoad.getLength()*nextRoad.getEnergyCost())/problem.getStraightlinedistance()));
        updateStatus();
        if(isFine){
            food=Float.min(maxfood,food+nextRoad.getCityB().getFood());
            energy=Float.min(maxenergy,energy+nextRoad.cityB.getEnergy());
        }

        problem.setCurrentCity(nextRoad.getCityB());
        nextRoad=null;
    }

    public void printStatus() {
        System.out.println("Current city : " + problem.getCurrentCity().getName());
        System.out.println("Food level : " + food + "/" + maxfood + "   Energy level : " + energy + "/" + maxenergy);
    }

    public Road GetBestRoad(List<Road> roadList) {
        Road bestRoad = null;
        float bestDesirability = 0;
        float roadDesirability = 0;

        for (Road road : roadList) {
            if (this.problem.getCurrentCity() == road.getCityB()) {
                road.SwitchCities();
            }

//            // TODO : Delete log
            System.out.println("Road from " + road.getCityA().getName() + " to " + road.getCityB().getName());
//            System.out.print("Predicted food :" + predictedFood(road));
//            System.out.print("Predicted energy :" + predictedEnergy(road));
//            System.out.println();

//            if (!visitedCities.contains(road.getCityB())) {
                roadDesirability = GetDesirability(road);
                if (roadDesirability > bestDesirability) {
                    bestDesirability = roadDesirability;
                    bestRoad = road;
                }
//            }
        }

        // Keeps the agent from looping infinitely
        if (bestRoad == null) {
            bestRoad = roadList.get(roadList.size() - 1);
        }

        // TODO : Delete log
        //System.out.println("Desirability : " + bestDesirability);

        return bestRoad;
    }

    public float GetDesirability(Road road) {
        float desirability = 0;
        float foodBoost = 1, energyBoost = 1, riskBoost = 1, distanceBoost = 1;
        float boost = 2;

        //System.out.println("Road from : " + road.getCityA().getName() + " to " + road.getCityB().getName());
//        System.out.println("Road from " + road.getCityA().getName() + " to " + road.getCityB().getName());

        float energyCost = (float) consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedEnergyCost() / problem.GetNormalizedStraightLinedDistance()));
        float foodCost = (float) consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedFoodCost() / problem.GetNormalizedStraightLinedDistance()));

        // TODO : Delete log
//        System.out.println("Energy cost :" + energyCost);
//        System.out.println("Food cost :" + foodCost);

        // Normalized values
        float foodDesirability = (Float.min(1f, GetNormalizedFood() + road.getCityB().GetNormalizedFood())) / foodCost;

        float energyDesirability = (Float.min(1f, GetNormalizedEnergy()  + road.getCityB().GetNormalizedEnergy())) / energyCost;

        float distanceDesirability;
        if (road.getCityB().GetNormalizedDistanceToGoal() == 0) {
            distanceDesirability = 10;
        } else {
            // Old value
            //distanceDesirability = (float) (road.GetNormalizedLength() + road.getCityB().GetNormalizedDistanceToGoal()) / road.getCityB().GetNormalizedDistanceToGoal();

            // new value
            distanceDesirability = (float) 1/(road.getCityB().GetNormalizedDistanceToGoal());
        }

        float riskDesirability = (float) Math.min(food, energy) / (road.getRisk());

        // TODO : Delete log
//        System.out.println("Food desirability :" + foodDesirability);
//        System.out.println("Energy desirability :" + energyDesirability);
//        System.out.println("Distance desirability :" + distanceDesirability);
//        System.out.println("Risk desirability :" + riskDesirability);

        switch (this.strategy) {
            case FOODFOCUS:
                foodBoost = boost;
                desirability = (foodBoost*foodDesirability + energyBoost*energyDesirability)*riskDesirability*riskBoost*distanceDesirability*distanceBoost;
//                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case ENERGYFOCUS:
                energyBoost = boost;
                desirability = (foodBoost*foodDesirability + energyBoost*energyDesirability)*riskDesirability*riskBoost*distanceDesirability*distanceBoost;

//                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case RISKFOCUS:
                riskBoost = boost;
                desirability = (foodBoost*foodDesirability + energyBoost*energyDesirability)*riskDesirability*riskBoost*distanceDesirability*distanceBoost;

//                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case SURVIVALFOCUS:
                if (predictedFood(road) < 40) {
                    foodBoost = 5;
                    distanceBoost = 5;
                } else if (predictedEnergy(road) < 40) {
                    energyBoost = 5;
                    distanceBoost = 5;
                } else {
                    distanceBoost = 10;
                }
                desirability = (foodBoost*foodDesirability + energyBoost*energyDesirability)*riskDesirability*riskBoost*distanceDesirability*distanceBoost;

//                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case SHORTESTPATHFOCUS:
                distanceBoost = boost;
                desirability = (foodBoost*foodDesirability + energyBoost*energyDesirability)*riskDesirability*riskBoost*distanceDesirability*distanceBoost;
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
        // TODO : Delete log
        System.out.println("Desirability : " + desirability);
        return desirability;
    }

    public void run() {

        while (isFine && !problem.getCurrentCity().equals(problem.getGoalCity())) {
            chooseNextMove();
            move();
            printStatus();
        }
        if (isFine) {
            System.out.println("Congratulations ! You arrived in " + problem.getGoalCity().getName() + " ! (weird choice of destination but whatever)");
        } else {
            System.out.println("Sorry you have died on the roads of France, that's why you stay at home travelling is dangerous");
        }

    }


}
