package me.kafein.elitegenerator.generator.feature.requirement.impl;

import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;
import org.bukkit.entity.Player;

public class PermissionRequirement extends Requirement {

    public PermissionRequirement(String requirement) {
        super(requirement);
    }

    @Override
    public void apply(Object target) {

    }

    @Override
    public boolean has(Object target) {
        return ((Player) target).hasPermission(getRequirement());
    }

    @Override
    public RequirementType getRequirementType() {
        return RequirementType.PLAYER;
    }

}
