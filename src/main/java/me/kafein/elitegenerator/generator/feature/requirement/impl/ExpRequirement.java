package me.kafein.elitegenerator.generator.feature.requirement.impl;

import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;
import me.kafein.elitegenerator.util.Experience;
import org.bukkit.entity.Player;

public class ExpRequirement extends Requirement {

    public ExpRequirement(final Number requirement) {
        super(requirement);
    }

    @Override
    public void apply(Object target) {
        final Player player = (Player) target;
        Experience.changeExp(player, -getNumberRequirement().intValue());
    }

    @Override
    public boolean has(Object target) {
        return Experience.getExp((Player) target) >= getNumberRequirement().intValue();
    }

    @Override
    public RequirementType getRequirementType() {
        return RequirementType.PLAYER;
    }

}
