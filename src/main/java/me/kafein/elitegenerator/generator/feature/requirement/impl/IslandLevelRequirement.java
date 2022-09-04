package me.kafein.elitegenerator.generator.feature.requirement.impl;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;
import org.bukkit.entity.Player;

public class IslandLevelRequirement extends Requirement {

    public IslandLevelRequirement(final Number requirement) {
        super(requirement);
    }

    @Override
    public void apply(Object target) {

    }

    @Override
    public boolean has(Object target) {
        return EliteGenerator.getInstance().getHookManager().getSkyBlockHook().getIslandLevel(((Player) target).getUniqueId()) >= getNumberRequirement().longValue();
    }

    @Override
    public RequirementType getRequirementType() {
        return RequirementType.PLAYER;
    }

}
