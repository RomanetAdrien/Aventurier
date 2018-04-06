package environnement;

/**
 * Created by adrie on 05/04/2018.
 */
public class Ville {

    protected String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String size;
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    protected float food;
    public float getFood() {
        return food;
    }

    public void setFood(float food) {
        this.food = food;
    }

    protected float energy;
    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    protected float risk;
    public float getRisk() {
        return risk;
    }

    public void setRisk(float risk) {
        this.risk = risk;
    }

    public Ville(String name, String size, float food, float energy, float risk) {
        this.name = name;
        this.size = size;
        this.food = food;
        this.energy = energy;
        this.risk = risk;
    }
}
