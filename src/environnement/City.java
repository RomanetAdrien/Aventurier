package environnement;

/**
 * Created by adrie on 05/04/2018.
 */
public class City {
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

    protected int energyCost;

    public int getEnergyCost() {
        return energyCost;
    }

    protected float food;

    public float getFood() {
        return food;
    }

    // Increase food based on the max food (food + % of max food)
    public void setFood() {
        switch (this.size) {
            case BIG:
                this.food = 75;
                break;
            case MEDIUM:
                this.food = 60;
                break;
            case SMALL:
                this.food = 50;
                break;
            default:
                break;
        }
    }

    protected float energy;

    public float getEnergy() {
        return energy;
    }

    //Increase energy based on the max energy (energy + % of max energy)
    public void setEnergy() {
        switch (this.size) {
            case BIG:
                this.energy = 75;
                break;
            case MEDIUM:
                this.energy = 60;
                break;
            case SMALL:
                this.energy = 50;
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
}
