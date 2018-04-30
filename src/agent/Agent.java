package agent;

import Other.HelpfulFunctions;
import environnement.City;
import environnement.Road;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    protected Random ran;

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
        this.ran = new Random();
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
        int shortestpath=Integer.MAX_VALUE;
        Road bestroad=null;
        for(Road road : problem.GetAccessibleRoads(problem.getCurrentCity())){
            for(Pair pair : deaths){
                if(pair.getKey().equals(road) && (int)pair.getValue()<minimumdeath){
                    bestroad=road;
                    for(Pair distancepair : distances){
                        if(distancepair.getKey().equals(pair.getKey())){
                            shortestpath=(int)distancepair.getValue();
                        }
                    }
                }
                else{
                    if(pair.getKey().equals(road) && (int)pair.getValue()==minimumdeath){
                        for(Pair distancepair : distances){
                            if(distancepair.getKey().equals(road) && (int)distancepair.getValue()<shortestpath){
                                shortestpath=(int)distancepair.getValue();
                            }
                        }
                    }
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
        return food - consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedFoodCost() / problem.GetNormalizedStraightLinedDistance()));
    }

    public float predictedEnergy(Road road) {
        return energy - consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedEnergyCost() / problem.GetNormalizedStraightLinedDistance()));
    }


    public void move() {
        food = Float.max(0, food - consumptionFactor*100*((nextRoad.GetNormalizedLength()) * nextRoad.GetNormalizedFoodCost()) / problem.GetNormalizedStraightLinedDistance());
        energy = Float.max(0, energy - consumptionFactor*100*((nextRoad.GetNormalizedLength() * nextRoad.GetNormalizedEnergyCost()) / problem.GetNormalizedStraightLinedDistance()));

        // Random bandit attack based on risk
        if(ran.nextInt(100)<nextRoad.getRisk()){
            System.out.println("Bandits attack !" + nextRoad.getRisk());
            food = Float.max(0, food - 50);
            energy = Float.max (0, energy - 50);
        }

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
        float roadDesirability;

        for (Road road : roadList) {
            if (this.problem.getCurrentCity() == road.getCityB()) {
                road.SwitchCities();
            }

            if (!visitedCities.contains(road.getCityB())) {
                roadDesirability = GetDesirability(road);
                if (roadDesirability > bestDesirability) {
                    bestDesirability = roadDesirability;
                    bestRoad = road;
                }
            }
        }

        // Keeps the agent from looping infinitely
        if (bestRoad == null) {
            bestRoad = roadList.get(roadList.size() - 1);
        }

        return bestRoad;
    }

    public float GetDesirability(Road road) {
        float desirability = 0;
        float foodBoost = 1, energyBoost = 1, riskBoost = 1, distanceBoost = 1;
        float boost = 2;

        //System.out.println("Road from : " + road.getCityA().getName() + " to " + road.getCityB().getName());
//        System.out.println("Road from " + road.getCityA().getName() + " to " + road.getCityB().getName());

        float energyCost = consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedEnergyCost() / problem.GetNormalizedStraightLinedDistance()));
        float foodCost = consumptionFactor * 100 * ((road.GetNormalizedLength() * road.GetNormalizedFoodCost() / problem.GetNormalizedStraightLinedDistance()));

        // Normalized values
        float foodDesirability = (Float.min(1f, GetNormalizedFood() + road.getCityB().GetNormalizedFood())) / foodCost;

        float energyDesirability = (Float.min(1f, GetNormalizedEnergy()  + road.getCityB().GetNormalizedEnergy())) / energyCost;

        float distanceDesirability;
        if (road.getCityB().GetNormalizedDistanceToGoal() == 0) {
            distanceDesirability = 10;
        } else {
            distanceDesirability = (float) 1/(road.getCityB().GetNormalizedDistanceToGoal());
        }

        float riskDesirability = Math.min(food, energy) / (road.getRisk());

        switch (this.strategy) {
            case FOODFOCUS:
                // only food matters.
                foodBoost = boost;
                desirability = (foodBoost*foodDesirability);
                break;

            case ENERGYFOCUS:
                energyBoost = boost;
                // only energy matters.
                desirability = energyBoost*energyDesirability;
                break;

            case RISKFOCUS:
                // only risk matters.
                riskBoost = boost;
                desirability = riskDesirability*riskBoost;
                break;

            case SURVIVALFOCUS:
                // everything is taken into account
                if (predictedFood(road) < 50) {
                    foodBoost = boost;
                    riskBoost = boost;
                    distanceBoost = boost;
                } else if (predictedEnergy(road) < 50) {
                    energyBoost = boost;
                    riskBoost = boost;
                    distanceBoost = boost;
                } else {
                    distanceBoost = 3*boost   ;
                }
                desirability = Float.min(foodBoost*foodDesirability,energyBoost*energyDesirability)*riskDesirability*riskBoost*distanceDesirability*distanceBoost;

//                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;

            case SHORTESTPATHFOCUS:
                // only distance matters
                distanceBoost = boost;
                desirability = distanceDesirability*distanceBoost;
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

    public void run() {
        System.out.println("My strategy is " + strategy.toString());
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
