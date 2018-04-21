package agent;

import Other.HelpfulFunctions;
import environnement.City;
import environnement.Road;

import java.util.ArrayList;
import java.util.List;

public class Problem {

    // map graph matrix
    protected Road[][] graphMatrix;
    public Road[][] getGraphMatrix() {
        return graphMatrix;
    }

    // current city
    private City currentCity;
    public City getCurrentCity() {
        return currentCity;
    }
    public void setCurrentCity(City city) {currentCity=city;}

    // straight distance between goal city and depart city
    public int straightlinedistance;
    public int getStraightlinedistance(){ return straightlinedistance;}
    public float GetNormalizedStraightLinedDistance(){
        return HelpfulFunctions.Normalize(straightlinedistance,0,straightlinedistance,0,1);
    }
    public void setStraightlinedistance(int distance){straightlinedistance=distance; }


    // goal city
    private City goalCity;
    public City getGoalCity() {
        return goalCity;
    }

    public boolean TestGoalReached(){
        return this.currentCity==this.goalCity;
    }


    public Problem(Road[][] graphMatrix, City currentCity, City goalCity) {
        this.graphMatrix = graphMatrix;
        this.currentCity = currentCity;
        this.goalCity = goalCity;
    }

    // succesion functions
    public List<Road> GetAccessibleRoads(City city){
        List<Road> roads = new ArrayList<>();
        for(int i = 0; i<graphMatrix.length; i++){
            if(graphMatrix[city.getId()-1][i]!=null){
                roads.add(graphMatrix[city.getId()-1][i]);
            }
        }
        return roads;
    }
}
