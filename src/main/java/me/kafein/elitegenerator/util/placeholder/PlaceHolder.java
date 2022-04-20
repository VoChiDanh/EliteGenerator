package me.kafein.elitegenerator.util.placeholder;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.util.location.LocationSerializer;
import org.bukkit.ChatColor;

public class PlaceHolder {

    public static String BUYED;
    public static String NOTBUYED;
    public static String ENABLED;
    public static String DISABLED;

    public static void reload() {
        FileConfig fileConfig = EliteGenerator.getInstance().getFileManager().getFile(FileManager.ConfigFile.language);
        BUYED = fileConfig.getString("language.generator.placeHolder.buyed");
        NOTBUYED = fileConfig.getString("language.generator.placeHolder.notbuyed");
        ENABLED = fileConfig.getString("language.generator.placeHolder.enabled");
        DISABLED = fileConfig.getString("language.generator.placeHolder.disabled");
    }

    public static String replace(final String text, final Generator generator) {
        return ChatColor.translateAlternateColorCodes('&', text
                .replace("%generator_uuid%", generator.getGeneratorUUID().toString())
                .replace("%generator_name%", generator.getGeneratorName())
                .replace("%generator_location%", LocationSerializer.serialize(generator.getGeneratorLocation()))
                .replace("%generator_level%", Integer.toString(generator.getLevel()))
                .replace("%generator_member_amount%", Integer.toString(generator.getGeneratorMemberSize()))
                .replace("%generator_has_boost%", (generator.hasBoost() ? ENABLED : DISABLED))
                .replace("%generator_boost_level%", (generator.hasBoost() ? Integer.toString(generator.getBoost().getLevel()) : DISABLED))
                .replace("%generator_boost_time%", (generator.hasBoost() ? generator.getBoost().getTimeParsed() : "00.00"))
                .replace("%generator_auto_break_enabled%", (generator.isAutoBreakEnabled() ? ENABLED : DISABLED))
                .replace("%generator_auto_break_buyed%", (generator.isAutoBreakBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_pickup_enabled%", (generator.isAutoPickupEnabled() ? ENABLED : DISABLED))
                .replace("%generator_auto_pickup_buyed%", (generator.isAutoPickupBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_smelt_enabled%", (generator.isAutoSmeltEnabled() ? ENABLED : DISABLED))
                .replace("%generator_auto_smelt_buyed%", (generator.isAutoSmeltBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_chest_buyed%", (generator.isAutoChestBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_chest_enabled%",
                        (generator.isAutoChestEnabled() ? LocationSerializer.serialize(generator.getAutoChest().getChestLocation()) : DISABLED)));
    }


    public static String replace(final String text, final Generator generator, final String playerName) {
        return ChatColor.translateAlternateColorCodes('&', text
                .replace("%player%", playerName)
                .replace("%generator_uuid%", generator.getGeneratorUUID().toString())
                .replace("%generator_name%", generator.getGeneratorName())
                .replace("%generator_location%", LocationSerializer.serialize(generator.getGeneratorLocation()))
                .replace("%generator_level%", Integer.toString(generator.getLevel()))
                .replace("%generator_member_amount%", Integer.toString(generator.getGeneratorMemberSize()))
                .replace("%generator_has_boost%", (generator.hasBoost() ? ENABLED : DISABLED))
                .replace("%generator_boost_level%", (generator.hasBoost() ? Integer.toString(generator.getBoost().getLevel()) : DISABLED))
                .replace("%generator_boost_time%", (generator.hasBoost() ? generator.getBoost().getTimeParsed() : "00.00"))
                .replace("%generator_auto_break_enabled%", (generator.isAutoBreakEnabled() ? ENABLED : DISABLED))
                .replace("%generator_auto_break_buyed%", (generator.isAutoBreakBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_pickup_enabled%", (generator.isAutoPickupEnabled() ? ENABLED : DISABLED))
                .replace("%generator_auto_pickup_buyed%", (generator.isAutoPickupBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_smelt_enabled%", (generator.isAutoSmeltEnabled() ? ENABLED : DISABLED))
                .replace("%generator_auto_smelt_buyed%", (generator.isAutoSmeltBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_chest_buyed%", (generator.isAutoChestBuyed() ? BUYED : NOTBUYED))
                .replace("%generator_auto_chest_enabled%",
                        (generator.isAutoChestEnabled() ? LocationSerializer.serialize(generator.getAutoChest().getChestLocation()) : DISABLED)));
    }

}
