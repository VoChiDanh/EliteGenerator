package me.kafein.elitegenerator.hook.hologram;

import me.kafein.elitegenerator.generator.Generator;

public interface HologramHook {

    void loadHologram(final Generator generator);

    void reloadHologram(final Generator generator);

    void deleteHologram(final Generator generator);

    void reloadBoostLine(final Generator generator);

}
