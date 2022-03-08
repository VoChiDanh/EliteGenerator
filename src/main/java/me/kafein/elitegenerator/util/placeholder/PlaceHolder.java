package me.kafein.elitegenerator.util.placeholder;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.util.location.LocationSerializer;
import org.bukkit.ChatColor;

public class PlaceHolder {

    public static String replace(final String text, final Generator generator) {
        return ChatColor.translateAlternateColorCodes('&', text
                .replace("%generator_uuid%", generator.getGeneratorUUID().toString())
                .replace("%generator_name%", generator.getGeneratorName())
                .replace("%generator_location%", LocationSerializer.serialize(generator.getGeneratorLocation()))
                .replace("%generator_level%", Integer.toString(generator.getLevel()))
                .replace("%generator_member_amount%", Integer.toString(generator.getGeneratorMemberSize()))
                .replace("%generator_has_boost%", (generator.hasBoost() ? "enabled" : "disabled"))
                .replace("%generator_boost_level%", (generator.hasBoost() ? Integer.toString(generator.getBoost().getLevel()) : "disabled"))
                .replace("%generator_boost_time%", (generator.hasBoost() ? generator.getBoost().getTimeParsed() : "00.00"))
                .replace("%generator_auto_break_enabled%", (generator.isAutoBreakEnabled() ? "enabled" : "disabled"))
                .replace("%generator_auto_break_buyed%", (generator.isAutoBreakBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_pickup_enabled%", (generator.isAutoPickupEnabled() ? "enabled" : "disabled"))
                .replace("%generator_auto_pickup_buyed%", (generator.isAutoPickupBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_smelt_enabled%", (generator.isAutoSmeltEnabled() ? "enabled" : "disabled"))
                .replace("%generator_auto_smelt_buyed%", (generator.isAutoSmeltBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_chest_buyed%", (generator.isAutoChestBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_chest_enabled%",
                        (generator.isAutoChestEnabled() ? LocationSerializer.serialize(generator.getAutoChest().getChestLocation()) : "disabled")));
    }


    public static String replace(final String text, final Generator generator, final String playerName) {
        return ChatColor.translateAlternateColorCodes('&', text
                .replace("%player%", playerName)
                .replace("%generator_uuid%", generator.getGeneratorUUID().toString())
                .replace("%generator_name%", generator.getGeneratorName())
                .replace("%generator_location%", LocationSerializer.serialize(generator.getGeneratorLocation()))
                .replace("%generator_level%", Integer.toString(generator.getLevel()))
                .replace("%generator_member_amount%", Integer.toString(generator.getGeneratorMemberSize()))
                .replace("%generator_has_boost%", (generator.hasBoost() ? "enabled" : "disabled"))
                .replace("%generator_boost_level%", (generator.hasBoost() ? Integer.toString(generator.getBoost().getLevel()) : "disabled"))
                .replace("%generator_boost_time%", (generator.hasBoost() ? generator.getBoost().getTimeParsed() : "00.00"))
                .replace("%generator_auto_break_enabled%", (generator.isAutoBreakEnabled() ? "enabled" : "disabled"))
                .replace("%generator_auto_break_buyed%", (generator.isAutoBreakBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_pickup_enabled%", (generator.isAutoPickupEnabled() ? "enabled" : "disabled"))
                .replace("%generator_auto_pickup_buyed%", (generator.isAutoPickupBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_smelt_enabled%", (generator.isAutoSmeltEnabled() ? "enabled" : "disabled"))
                .replace("%generator_auto_smelt_buyed%", (generator.isAutoSmeltBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_chest_buyed%", (generator.isAutoChestBuyed() ? "buyed" : "not buyed"))
                .replace("%generator_auto_chest_enabled%",
                        (generator.isAutoChestEnabled() ? LocationSerializer.serialize(generator.getAutoChest().getChestLocation()) : "disabled")));
    }

}
