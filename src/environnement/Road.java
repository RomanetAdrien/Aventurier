package environnement;

public class Road {

    protected int number;
    public int getNumber() {
        return number;
    }

    protected int length;
    public int getLength() {
        return length;
    }

    protected int energyCost;
    public int getEnergyCost() {
        return energyCost;
    }

    public int foodCost;
    public int getFood() {
        return foodCost;
    }

    public City cityA;
    public City getCityA() {
        return cityA;
    }

    public City cityB;
    public City getCityB() {
        return cityB;
    }

    public float risk;
    public float getRisk() {
        return risk;
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
}