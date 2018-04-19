package agent;

import environnement.City;
import environnement.Road;

import java.util.List;

/**
 * Created by adrie on 05/04/2018.
 */
public class Agent {

    protected float energy;
    protected float food;
    protected float maxenergy;
    protected float maxfood;
    protected boolean isFine;
    protected Road nextRoad;
    protected Problem problem;
    protected StrategyEnum strategy;

    public Agent(Problem prob, StrategyEnum strategy) {
        maxenergy = 100;
        maxfood = 100;
        energy = maxenergy;
        food = maxfood;
        problem = prob;
        isFine = true;
        this.strategy = strategy;
    }


    public void chooseNextMove() {
        nextRoad = this.GetBestRoad(problem.GetAccessibleRoads(problem.getCurrentCity()));
    }

    public void updateStatus() {
        if (food <= 0 || energy <= 0) {
            isFine = false;
        }
    }

    public float predictedFood() {
        float aftermove = 0;
        float afterroad = food - ((50 * nextRoad.getLength() * nextRoad.getFoodCost()) / problem.getStraightlinedistance());
        if (afterroad > 0) {
            aftermove = afterroad + nextRoad.getCityB().getFood();
        }
        return aftermove;
    }

    public float predictedEnergy() {
        float aftermove = 0;
        float afterroad = energy - ((50 * nextRoad.getLength() * nextRoad.getEnergyCost()) / problem.getStraightlinedistance());
        if (afterroad > 0) {
            aftermove = afterroad + nextRoad.getCityB().getEnergy();
        }
        return aftermove;
    }


    public void move() {
        food = Float.max(0, food - ((50 * nextRoad.getLength() * nextRoad.getFoodCost()) / problem.getStraightlinedistance()));
        energy = Float.max(0, energy - ((50 * nextRoad.getLength() * nextRoad.getEnergyCost()) / problem.getStraightlinedistance()));
        updateStatus();
        if (isFine) {
            food = Float.min(maxfood, food + nextRoad.getCityB().getFood());
            energy = Float.min(maxenergy, energy + nextRoad.cityB.getEnergy());
        }

        problem.setCurrentCity(nextRoad.getCityB());
        nextRoad.printRoad();
        nextRoad = null;
    }

    public void chooseNextMoveBrutal() {
        Road next = problem.GetAccessibleRoads(problem.getCurrentCity()).get(0);
        for (Road road : problem.GetAccessibleRoads(problem.getCurrentCity())) {
            if (road.cityB.getDistanceToGoal() <= next.cityB.getDistanceToGoal()) {
                next = road;
            }
        }
        nextRoad = next;
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
            if(this.problem.getCurrentCity()==road.getCityB()){
                road.SwitchCities();
            }
            roadDesirability = GetDesirability(road);
            if (roadDesirability > bestDesirability) {
                bestDesirability = roadDesirability;
                bestRoad = road;
            }
        }

        // TODO : delete log
        System.out.println("Desirability : " + bestDesirability);
        return bestRoad;
    }

    public float GetDesirability(Road road) {
        float desirability = 0;
        float foodBoost = 1, energyBoost = 1, riskBoost = 1, distanceBoost = 1;
        float boost = 10;

//        System.out.println("Road from " + road.getCityA().getName() + " to " + road.getCityB().getName());

        float energyCost = (float) road.getEnergyCost() * 50 * road.getLength() / problem.getStraightlinedistance();
        float foodCost = (float) road.getFoodCost() * 50 * road.getLength() / problem.getStraightlinedistance();

        // TODO : comment log
//        System.out.println("Energy cost :" + energyCost);
//        System.out.println("Food cost :" + foodCost);

        float foodDesirability = (float) (food + road.getCityB().getFood()) / foodCost;
        float energyDesirability = (float) (energy + road.getCityB().getEnergy()) / energyCost;
        float distanceDesirability;
        if (road.getCityB().getDistanceToGoal() == 0) {
            distanceDesirability = 3;
        }
        else {
            distanceDesirability = (float) (road.getLength() + road.getCityB().getDistanceToGoal()) / road.getCityB().getDistanceToGoal();
        }
        float riskDesirability = (float) Math.min(food, energy) / (10 * road.getRisk());

//        System.out.println("Food desirability :" + foodDesirability);
//        System.out.println("Energy desirability :" + energyDesirability);
//        System.out.println("Distance desirability :" + distanceDesirability);
//        System.out.println("Risk desirability :" + riskDesirability);

        switch (this.strategy) {
            case FOODFOCUS:
                foodBoost = boost;
                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case ENERGYFOCUS:
                energyBoost = boost;
                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case RISKFOCUS:
                riskBoost = boost;
                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case SURVIVALFOCUS:

                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            case SHORTESTPATHFOCUS:
                distanceBoost = boost;
                desirability = foodBoost * foodDesirability + energyBoost * energyDesirability + distanceBoost * distanceDesirability + riskBoost / riskDesirability;
                break;
            default:
                break;
        }

//        System.out.println("Desirability : " + desirability);
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
