package environnement;

/**
 * Created by adrie on 05/04/2018.
 */
public class Ville {

    protected String name;
    public String getName() {
        return name;
    }

    protected CitySize size;
    public CitySize getSize() {
        return size;
    }

    protected int energyCost;
    public int getEnergyCost() {
        return energyCost;
    }

    protected float food;
    public float getFood() {
        return food;
    }

    protected float energy;
    public float getEnergy() {
        return energy;
    }

    protected float risk;
    public float getRisk() {
        return risk;
    }

    public Ville(String name, CitySize size, float food, float energy, float risk) {
        this.name = name;
        this.size = size;
        this.energyCost = size.ordinal() +1;
        this.food = food;
        this.energy = energy;
        this.risk = size.getRisk();
    }
}
