package environnement;

public enum CitySize {
    SMALL("SMALL"),
    MEDIUM("MEDIUM"),
    BIG("BIG");

    protected String name;

    protected int risk;

    public int getRisk(){
        return this.risk;
    }

    CitySize(String name) {
        this.name = name;
        switch (name){
            case "SMALL":
                this.risk = 30;
                break;
            case "MEDIUM":
                this.risk = 20;
                break;
            case "BIG":
                this.risk = 10;
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
