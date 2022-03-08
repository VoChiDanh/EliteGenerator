package me.kafein.elitegenerator.generator.feature.upgrade;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Upgrade {

    final private List<Requirement> requirementList = new ArrayList<>();

    public abstract void apply(final Generator generator);

    public void addRequirement(final Requirement requirement) {
        requirementList.add(requirement);
    }

    public void removeRequirement(final Requirement requirement) {
        requirementList.remove(requirement);
    }

    public boolean containsRequirement(final Requirement requirement) {
        return requirementList.contains(requirement);
    }

    public boolean hasRequirements(final Player player, final Generator generator) {
        for (Requirement requirement : requirementList) {
            if (requirement.getRequirementType() == RequirementType.GENERATOR ? requirement.has(generator) : requirement.has(player)) continue;
            return false;
        }
        return true;
    }

    public void applyRequirements(final Player player, final Generator generator) {
        requirementList.forEach(req -> {
            if (req.getRequirementType() == RequirementType.GENERATOR) req.apply(generator);
            else req.apply(player);
        });
    }

}
