package me.kafein.elitegenerator.generator.feature.requirement.impl;

import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;
import org.bukkit.entity.Player;

public class ExpRequirement extends Requirement {

    public ExpRequirement(final Number requirement) {
        super(requirement);
    }

    @Override
    public void apply(Object target) {
        final Player player = (Player) target;
        player.setLevel(player.getLevel() - getNumberRequirement().intValue());
    }

    @Override
    public boolean has(Object target) {
        return ((Player) target).getLevel() >= getNumberRequirement().intValue();
    }

    @Override
    public RequirementType getRequirementType() {
        return RequirementType.PLAYER;
    }

}
