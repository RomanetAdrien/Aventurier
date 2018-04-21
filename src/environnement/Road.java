package environnement;
import Other.HelpfulFunctions;

public class Road {

    protected int number;
    public int getNumber() {
        return number;
    }

    protected int length;
    public int getLength() {
        return length;
    }
    public float GetNormalizedLength(){
        return HelpfulFunctions.Normalize(length,0,750,0,1);
    }

    protected int energyCost;
    public int getEnergyCost() {
        return energyCost;
    }
    public float GetNormalizedEnergyCost(){
        return HelpfulFunctions.Normalize(energyCost,0,6,0,1);
    }

    public int foodCost;
    public int getFoodCost() {
        return foodCost;
    }
    public float GetNormalizedFoodCost(){
        return HelpfulFunctions.Normalize(foodCost,0,5,0,1);
    }

    public City cityA;
    public City getCityA() {
        return cityA;
    }
    public void setCityA(City cityA) {
        this.cityA = cityA;
    }

    public City cityB;
    public City getCityB() {
        return cityB;
    }
    public void setCityB(City cityB) {
        this.cityB = cityB;
    }

    public float risk;
    public float getRisk() {
        return risk;
    }
    public float GetNormalizedRisk(){
        return HelpfulFunctions.Normalize(risk,0,100,0,1);
    }

    public Road(int number, int length, int foodCost, City cityA, City cityB) {
        this.number = number;
        this.length = length;
        this.energyCost = cityA.getEnergyCost() + cityB.getEnergyCost();
        this.foodCost = foodCost;
        this.cityA = cityA;
        this.cityB = cityB;
        this.risk = cityA.getRisk() + cityB.getRisk();
    }

    public void SwitchCities(){
        City dummy = cityA;
        cityA = cityB;
        cityB = dummy;
    }

}