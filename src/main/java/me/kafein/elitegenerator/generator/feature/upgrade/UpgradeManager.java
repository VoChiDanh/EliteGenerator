package me.kafein.elitegenerator.generator.feature.upgrade;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.feature.requirement.Requirement;
import me.kafein.elitegenerator.generator.feature.requirement.impl.*;
import me.kafein.elitegenerator.generator.feature.upgrade.impl.*;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class UpgradeManager {

    final private FileConfig fileConfig = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.settings);

    final private Map<UpgradeType, Upgrade> upgrades = new HashMap<>();
    final private Map<Integer, Upgrade> levelUpgrades = new HashMap<>();
    final private Map<Integer, BoostUpgrade> boostUpgrades = new HashMap<>();

    public UpgradeManager() {
        loadUpgrades();
    }

    private void loadUpgrades() {

        final String prefix = "settings.generator.upgrade.";

        for (String level : fileConfig.getConfigurationSection(prefix + "level-upgrade").getKeys(false)) {

            final Upgrade upgrade = new LevelUpgrade();
            addRequirements(upgrade, fileConfig, prefix + "level-upgrade." + level);
            levelUpgrades.put(Integer.parseInt(level), upgrade);

        }

        for (String boost : fileConfig.getConfigurationSection(prefix + "boost-upgrade").getKeys(false)) {

            final BoostUpgrade upgrade = new BoostUpgrade();
            addRequirements(upgrade, fileConfig, prefix + "boost-upgrade." + boost);
            boostUpgrades.put(Integer.parseInt(boost), upgrade);

        }

        for (UpgradeType upgradeType : UpgradeType.values()) {

            Upgrade upgrade = null;

            switch (upgradeType) {

                case AUTOBREAK:
                    upgrade = new AutoBreakUpgrade();
                    break;
                case AUTOPICKUP:
                    upgrade = new AutoPickupUpgrade();
                    break;
                case AUTOSMELT:
                    upgrade = new AutoSmeltUpgrade();
                    break;
                case AUTOCHEST:
                    upgrade = new AutoChestUpgrade();
                    break;

            }

            addRequirements(upgrade, fileConfig, prefix + upgradeType.name().toLowerCase(Locale.ENGLISH));
            upgrades.put(upgradeType, upgrade);

        }

    }

    private void addRequirements(final Upgrade upgrade, final FileConfig fileConfig, final String prefix) {

        for (String requirements : fileConfig.getStringList(prefix + ".requirement")) {

            Requirement requirement = null;

            final String[] splitter = requirements.split(": ");

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

            upgrade.addRequirement(requirement);

        }

    }

    @Nullable
    public Upgrade getUpgrade(final UpgradeType upgradeType) {
        return upgrades.get(upgradeType);
    }

    @Nullable
    public Upgrade getLevelUpgrade(final int level) {
        return levelUpgrades.get(level);
    }

    @Nullable
    public BoostUpgrade getBoostUpgrade(final int level) {
        return boostUpgrades.get(level);
    }

    public boolean containsLevelUpgrade(final int level) {
        return levelUpgrades.containsKey(level);
    }

    public boolean containsBoostUpgrade(final int level) {
        return boostUpgrades.containsKey(level);
    }

    public Set<Integer> getLevelList() {
        return levelUpgrades.keySet();
    }

    public Set<Integer> getBoostList() {
        return boostUpgrades.keySet();
    }

}
