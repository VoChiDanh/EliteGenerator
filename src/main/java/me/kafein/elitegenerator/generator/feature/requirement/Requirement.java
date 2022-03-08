package me.kafein.elitegenerator.generator.feature.requirement;

public abstract class Requirement {

    private String requirement;
    private Number numberRequirement;

    public Requirement() {}

    public Requirement(final Number numberRequirement) {
        this.numberRequirement = numberRequirement;
    }

    public Requirement(final String requirement) {
        this.requirement = requirement;
    }

    public abstract void apply(final Object target);
    public abstract boolean has(final Object target);

    public abstract RequirementType getRequirementType();

    public Number getNumberRequirement() {
        return numberRequirement;
    }

    public String getRequirement() {
        return requirement;
    }

}
