package me.kafein.elitegenerator.generator.feature.regen;

import dev.lone.itemsadder.api.CustomBlock;
import me.kafein.elitegenerator.EliteGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegenRunnable implements Runnable {

    final private RegenManager regenManager = EliteGenerator.getInstance().getGeneratorManager().getFeatureManager().getRegenManager();

    final private Plugin plugin;

    public static List<String> list = new ArrayList<>();

    public RegenRunnable(final Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 1, 1);
    }

    @Override
    public void run() {

        if (regenManager.isRegenGeneratorsIsEmpty()) return;

        final Iterator<Regen> regenIterator = regenManager.getRegenGenerators();

        while (regenIterator.hasNext()) {

            final Regen regen = regenIterator.next();

            if (regen.getDelay() <= 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (getRandom(1,4) == 3) {
                        CustomBlock.place(list.get(getRandom(0 , list.size() - 1)), regen.getLocation());
                    } else {
                        regen.getLocation().getBlock().setType(regen.getMaterial());
                    }
                });
                regenManager.removeRegenGenerator(regen.getLocation());
                regenIterator.remove();
            } else regen.setDelay(regen.getDelay() - 1);

        }

    }

    public static void load() {
        list.add("iasurvival:aqua_aura_ore");
        list.add("iasurvival:blaze_powder_ore");
        list.add("iasurvival:cassiterite_ore");
        list.add("iasurvival:coal_dirt_ore");
        list.add("iasurvival:dark_amethyst_ore");
        list.add("iasurvival:end_ore");
        list.add("iasurvival:gold_dirt_ore");
        list.add("iasurvival:iron_dirt_ore");
        list.add("iasurvival:ruby_ore");
        list.add("iasurvival:spinel_ore");
        list.add("iasurvival:turquoise_ore");
        list.add("iaalchemy:nether_alchemy_ore");
        list.add("iaalchemy:mysterious_ore");
    }

    public static Integer getRandom(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

}
