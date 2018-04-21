package agent;

import environnement.Road;

/**
 * Created by adrie on 21/04/2018.
 */
public class AgentScout extends Agent{

    public int traveledDistance;
    public int traveledRoads;
    public Road firstroad;

    public AgentScout(Agent agent) {
        super(agent);
        traveledDistance=0;
        traveledRoads=0;
        strategy=StrategyEnum.SURVIVALFOCUS;
        firstroad=null;
    }

    public void move(){
        if(nextRoad==null){
            firstroad=nextRoad;
        }
        traveledRoads++;
        traveledDistance+=nextRoad.getLength();
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

    public void run(){
        while(isFine&&!problem.getCurrentCity().equals(problem.getGoalCity())){
            chooseNextMove();
            move();
            printStatus();
        }
    }
}
