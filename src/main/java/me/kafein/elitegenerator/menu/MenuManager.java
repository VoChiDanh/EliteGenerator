package me.kafein.elitegenerator.menu;

import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.menu.impl.BoostMenu;
import me.kafein.elitegenerator.menu.impl.GeneratorMenu;
import me.kafein.elitegenerator.menu.impl.SettingsMenu;
import me.kafein.elitegenerator.menu.impl.UpgradeMenu;
import me.kafein.elitegenerator.menu.impl.member.MemberMenu;
import me.kafein.elitegenerator.menu.impl.member.MembersMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

    final private Map<String, Menu> menus = new HashMap<>();
    final private Map<String, String> menuTitles = new HashMap<>();

    final private Map<UUID, String> menuPlayers = new HashMap<>();

    final private Plugin plugin;

    public MenuManager(final Plugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {

        for (MenuType menuType : MenuType.values()) {

            final String menuName = menuType.name().toLowerCase(Locale.ENGLISH);

            Menu menu = null;

            final File file = new File(plugin.getDataFolder(), "menu/" + menuName + "-menu.yml");
            if (!file.exists()) plugin.saveResource("menu/" + menuName + "-menu.yml", true);
            final FileConfig fileConfig = new FileConfig(file);

            switch (menuType) {
                case GENERATOR:
                    menu = new GeneratorMenu(fileConfig.getString("menu.title"), fileConfig.getInt("menu.size"), fileConfig);
                    break;
                case UPGRADE:
                    menu = new UpgradeMenu(fileConfig.getString("menu.title"), fileConfig.getInt("menu.size"), fileConfig);
                    break;
                case SETTINGS:
                    menu = new SettingsMenu(fileConfig.getString("menu.title"), fileConfig.getInt("menu.size"), fileConfig);
                    break;
                case BOOST:
                    menu = new BoostMenu(fileConfig.getString("menu.title"), fileConfig.getInt("menu.size"), fileConfig);
                    break;
                case MEMBERS:
                    menu = new MembersMenu(fileConfig.getString("menu.title"), fileConfig.getInt("menu.size"), fileConfig);
                    break;
                case MEMBER:
                    menu = new MemberMenu(fileConfig.getString("menu.title"), fileConfig.getInt("menu.size"), fileConfig);
                    break;
            }

            menu.register(plugin);

            menus.put(menuName, menu);
            menuTitles.put(menu.getTitle(), menuName);

        }

    }

    public void reload() {
        menuPlayersCloseMenu();
        load();
    }

    @Nullable
    public Menu getMenu(final String name) {
        return menus.get(name);
    }

    @Nullable
    public Menu getMenu(final MenuType menuType) {
        return menus.get(menuType.name().toLowerCase(Locale.ENGLISH));
    }

    @Nullable
    public String getMenuName(final String title) {
        return menuTitles.get(title);
    }

    @Nullable
    public String getMenuTitleWithPlayer(final UUID playerUUID) {
        return menuPlayers.get(playerUUID);
    }

    public boolean contains(final String name) {
        return menus.containsKey(name);
    }

    public boolean containsMenuPlayer(final UUID playerUUID) {
        return menuPlayers.containsKey(playerUUID);
    }

    public boolean containsMenuTitle(final String title) {
        return menuTitles.containsKey(title);
    }

    public void add(final String name, final Menu menu) {
        menus.put(name, menu);
        menuTitles.put(menu.getTitle(), name);
    }

    public void remove(final String name) {
        menuTitles.remove(menus.get(name).getTitle());
        menus.remove(name);
    }

    public void addMenuPlayer(final UUID playerUUID, final String title) {
        menuPlayers.put(playerUUID, title);
    }

    public void removeMenuPlayer(final UUID playerUUID) {
        menuPlayers.remove(playerUUID);
    }

    public void menuPlayersCloseMenu() {
        for (UUID uuid : menuPlayers.keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            player.closeInventory();
        }
    }

    public enum MenuType {
        GENERATOR, UPGRADE, SETTINGS, BOOST, MEMBERS, MEMBER
    }

}
