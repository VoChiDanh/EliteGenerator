package me.kafein.elitegenerator.generator.feature.requirement.impl;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;

public class AutoSmeltRequirement extends Requirement {

    @Override
    public void apply(Object target) {

    }

    @Override
    public boolean has(Object target) {
        return ((Generator) target).isAutoSmeltBuyed();
    }

    @Override
    public RequirementType getRequirementType() {
        return RequirementType.GENERATOR;
    }

}
