package environnement;

import Other.HelpfulFunctions;

import java.util.ArrayList;

/**
 * Created by adrie on 05/04/2018.
 */
public class City {

    protected static ArrayList<Road> roadMap;

    public  ArrayList<Road> getRoadMap() { return roadMap; }
    public  void setRoadMap(ArrayList<Road> roads) { City.roadMap=roads; }

    protected int id;
    public int getId() {
        return id;
    }

    protected String name;
    public String getName() {
        return name;
    }

    protected CitySize size;
    public CitySize getSize() {
        return size;
    }

    protected int distanceToGoal;
    public int getDistanceToGoal() {
        return distanceToGoal;
    }
    public float GetNormalizedDistanceToGoal(){
        return HelpfulFunctions.Normalize(distanceToGoal,0,750,0,1);
    }

    protected int energyCost;
    public int getEnergyCost() {
        return energyCost;
    }

    protected int food;
    public int getFood() {
        return food;
    }
    public float GetNormalizedFood(){
        return HelpfulFunctions.Normalize(food,0,100,0,1);
    }
    // Increase food based on the max food (food + % of max food)
    // TODO : Adjust value
    public void setFood() {
        switch (this.size) {
            case BIG:
                this.food = 60;
                break;
            case MEDIUM:
                this.food = 50;
                break;
            case SMALL:
                this.food = 40;
                break;
            default:
                break;
        }
    }

    protected int energy;
    public int getEnergy() {
        return energy;
    }
    public float GetNormalizedEnergy(){
        return HelpfulFunctions.Normalize(energy,0,100,0,1);
    }
    //Increase energy based on the max energy (energy + % of max energy)
    // TODO : Adjust value
    public void setEnergy() {
        switch (this.size) {
            case BIG:
                this.energy = 60;
                break;
            case MEDIUM:
                this.energy = 50;
                break;
            case SMALL:
                this.energy = 40;
                break;
            default:
                break;
        }    }

    protected float risk;
    public float getRisk() {
        return risk;
    }

    public City(int id, String name, CitySize size, int distanceToGoal) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.distanceToGoal = distanceToGoal;
        this.energyCost = size.ordinal() + 1;
        this.setFood();
        this.setEnergy();
        this.risk = size.getRisk();
    }

    public ArrayList<City> getNeighbors(){
        ArrayList<City> neighbors = new ArrayList<>();
        for(Road road : City.roadMap){
            if(road.cityA.equals(this)&&neighbors.contains(road.cityB)){
                neighbors.add(road.cityB);
            }
            if(road.cityB.equals(this)&&neighbors.contains(road.cityA)){
                neighbors.add(road.cityA);
            }
        }
        return neighbors;
    }
}
