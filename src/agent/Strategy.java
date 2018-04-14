package agent;

import environnement.Road;

import java.util.List;

public class Strategy {

//    protected StrategyEnum strategy;
//
//    public StrategyEnum getStrategy() {
//        return strategy;
//    }
//
//    public Strategy(StrategyEnum strategy) {
//        this.strategy = strategy;
//    }
//
//    public Road GetBestRoad(List<Road> roadList){
//        Road bestRoad = null;
//        float bestDesirability = 0;
//        float roadDesirability = 0;
//
//        for (Road road : roadList) {
//            roadDesirability = GetDesirability(road);
//            if( roadDesirability > bestDesirability) {
//                bestDesirability = roadDesirability;
//                bestRoad = road;
//            }
//        }
//
//        return bestRoad;
//    }
//
//    public float GetDesirability(Road road){
//        float desirability = 0;
//        int foodBoost = 1, energyBoost = 1, riskBoost = 1, distanceBoost = 1;
//
//        switch(this.strategy){
//            case FOODFOCUS:
//                foodBoost = 2;
//
//                break;
//            case ENERGYFOCUS:
//                energyBoost = 2;
//                break;
//            case RISKFOCUS:
//                riskBoost = 2;
//                break;
//            case SURVIVALFOCUS:
//
//                break;
//            case SHORTESTPATHFOCUS:
//                distanceBoost = 2;
//                break;
//            default:
//                break;
//        }
//
//        return desirability;
//    }
}
