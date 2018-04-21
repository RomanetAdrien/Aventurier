package agent;

public enum StrategyEnum {
    FOODFOCUS("FOOD FOCUS", "Trying to get as much food as possible"),
    ENERGYFOCUS("ENERGY FOCUS", "Trying to get as much energy as possible"),
    RISKFOCUS("RISK FOCUS", "Trying to avoid risk as much as possible"),
    SURVIVALFOCUS("SURVIVAL FOCUS", "Trying to reach the end safely, monitoring resources and risk"),
    SHORTESTPATHFOCUS("SHORTEST PATH FOCUS", "Taking the shortest overall path"),
    MINMAX("MINMAX ALGORITHM", "We follow the minmax algorithm"),
    RANDOM("RANDOM", "Let's just spin a pen and let the gods decide where we go"),
    HECATOMBE("HECATOMBE", "Send many other people to try different roads so we can choose the shortest among the safest paths"),
    NONE("NONE", "No strategy, used by the intelligent agent");

    protected String name;
    protected String description;

    StrategyEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "StrategyEnum{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
