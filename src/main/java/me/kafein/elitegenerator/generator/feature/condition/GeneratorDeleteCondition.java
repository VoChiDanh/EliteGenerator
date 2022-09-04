package me.kafein.elitegenerator.generator.feature.condition;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.RequirementType;
import me.kafein.elitegenerator.generator.feature.requirement.impl.*;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeneratorDeleteCondition {

    private final static Set<Requirement> requirements = new HashSet<>();
    private static boolean enabled = false;

    static {
        FileConfig config = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.settings);
        if (config.getBoolean("settings.generator.generator-delete-conditions.enabled")) {
            enabled = true;
            List<String> list = config.getStringList("settings.generator.generator-delete-conditions.conditions");
            list.forEach(requirementNotParsed -> {
                final String[] splitter = requirementNotParsed.split(": ");
                Requirement requirement = null;
                switch (splitter[0].toLowerCase()) {
                    case "level":
                        requirement = new LevelRequirement(Integer.parseInt(splitter[1]));
                        break;
                    case "money":
                        requirement = new MoneyRequirement(Double.parseDouble(splitter[1]));
                        break;
                    case "perm":
                        requirement = new PermissionRequirement(splitter[1]);
                        break;
                    case "exp":
                        requirement = new ExpRequirement(Integer.parseInt(splitter[1]));
                        break;
                    case "is_level":
                        requirement = new IslandLevelRequirement(Long.parseLong(splitter[1]));
                        break;
                    case "upgrade":
                        switch (splitter[1]) {
                            case "auto_break":
                                requirement = new AutoBreakRequirement();
                                break;
                            case "auto_pickup":
                                requirement = new AutoPickupRequirement();
                                break;
                            case "auto_smelt":
                                requirement = new AutoSmeltRequirement();
                                break;
                            case "auto_chest":
                                requirement = new AutoChestRequirement();
                                break;
                        }
                        break;
                }
                if (requirement != null) requirements.add(requirement);
            });
        }
    }

    public static boolean check(Player player, Generator generator) {
        if (!enabled) return true;
        for (Requirement requirement : requirements) {
            if (requirement.getRequirementType() == RequirementType.GENERATOR ? requirement.has(generator) : requirement.has(player))
                continue;
            return false;
        }
        return true;
    }

}
