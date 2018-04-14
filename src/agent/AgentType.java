package agent;

public enum AgentType {
    GREEDY("GREEDY","Always taking the best immediate choice."),
    PLANIFICATION("PLANIFICATION", "Trying to find the best overall choice."),
    INTELLIGENT("INTELLIGENT","Getting better with each try.");

    protected String name;
    protected String description;

    AgentType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Strategy{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
