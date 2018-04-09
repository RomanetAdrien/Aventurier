package environnement;

public class Route {

    protected int number;

    public int getNumber() {
        return number;
    }

    protected int length;

    public int getLength() {
        return length;
    }

    protected int distanceToGoal;

    public int getDistanceToGoal() {
        return distanceToGoal;
    }

    protected int energyCost;

    public int getExhaustion() {
        return energyCost;
    }

    public int food;

    public int getFood() {
        return food;
    }

    public Ville cityA;

    public Ville getCityA() {
        return cityA;
    }

    public Ville cityB;

    public Ville getCityB() {
        return cityB;
    }

    public Route(int number, int length, int distanceToGoal, int food, Ville cityA, Ville cityB) {
        this.number = number;
        this.length = length;
        this.distanceToGoal = distanceToGoal;
        this.energyCost = cityA.getEnergyCost() + cityB.getEnergyCost();
        this.food = food;
        this.cityA = cityA;
        this.cityB = cityB;
    }
}