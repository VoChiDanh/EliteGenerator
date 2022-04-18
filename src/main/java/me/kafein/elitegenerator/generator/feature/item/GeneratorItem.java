package me.kafein.elitegenerator.generator.feature.item;

import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneratorItem {

    final private FileConfig fileConfig;

    public GeneratorItem(final FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    public ItemStack create(final int level, final boolean autoBreak, final boolean autoPickup, final boolean autoSmelt, final boolean autoChest) {

        final String prefix = "settings.generator.generator-item.";

        final ItemBuilder itemBuilder = new ItemBuilder(fileConfig.getString(prefix + "material"));
        itemBuilder.setString("generatorItem", "generator");
        itemBuilder.setInteger("level", level);
        itemBuilder.setBoolean("autoBreak", autoBreak);
        itemBuilder.setBoolean("autoPickup", autoPickup);
        itemBuilder.setBoolean("autoSmelt", autoSmelt);
        itemBuilder.setBoolean("autoChest", autoChest);

        final List<String> lore = fileConfig.getStringList(prefix + "lore");
        lore.replaceAll(e -> replacer(e, level, autoBreak, autoPickup, autoSmelt, autoChest));
        itemBuilder.setLore(lore);

        final String name = fileConfig.getString(prefix + "name");
        itemBuilder.setName(replacer(name, level, autoBreak, autoPickup, autoSmelt, autoChest));

        itemBuilder.setEnchant(fileConfig.getStringList(prefix + "enchant"));
        itemBuilder.setGlow(fileConfig.getBoolean(prefix + "glow"));

        return itemBuilder.toItemStack();

    }

    private String replacer(final String var, final int level, final boolean autoBreak, final boolean autoPickup, final boolean autoSmelt, final boolean autoChest) {
        return var
                .replace("%level%", Integer.toString(level))
                .replace("%autoBreak%", Boolean.toString(autoBreak))
                .replace("%autoPickup%", Boolean.toString(autoPickup))
                .replace("%autoSmelt%", Boolean.toString(autoSmelt))
                .replace("%autoChest%", Boolean.toString(autoChest));
    }

}
