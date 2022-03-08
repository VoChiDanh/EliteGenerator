package me.kafein.elitegenerator.generator.feature.requirement.impl;

import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;
import me.kafein.elitegenerator.hook.VaultHook;
import org.bukkit.entity.Player;

public class MoneyRequirement extends Requirement {

    public MoneyRequirement(final Number requirement) {
        super(requirement);
    }

    @Override
    public void apply(Object target) {
        VaultHook.getEconomy().withdrawPlayer(((Player) target), getNumberRequirement().doubleValue());
    }

    @Override
    public boolean has(Object target) {
        return VaultHook.getEconomy().has(((Player) target), getNumberRequirement().doubleValue());
    }

    @Override
    public RequirementType getRequirementType() {
        return RequirementType.PLAYER;
    }

}
