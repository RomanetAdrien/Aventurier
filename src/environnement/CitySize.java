package environnement;

public enum CitySize {
    SMALL("SMALL"),
    MEDIUM("MEDIUM"),
    BIG("BIG");

    protected String name;

    protected float risk;

    public float getRisk(){
        return this.risk;
    }

    CitySize(String name) {
        this.name = name;
        switch (name){
            case "SMALL":
                this.risk = 2.5f;
                break;
            case "MEDIUM":
                this.risk = 5f;
                break;
            case "BIG":
                this.risk = 7.5f;
                break;
                default:
                    break;
        }
    }

    @Override
    public String toString() {
        return "CitySize{" + name + '}';
    }
}
