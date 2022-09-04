package me.kafein.elitegenerator.listener;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public class PlayerListener implements Listener {

    final private UserManager userManager = EliteGenerator.getInstance().getUserManager();
    final private BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

    final private Plugin plugin;

    public PlayerListener(final Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPreLogin(final AsyncPlayerPreLoginEvent e) {
        userManager.loadUser(e.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPreLoginMonitor(final AsyncPlayerPreLoginEvent e) {
        if (userManager.containsUser(e.getUniqueId())) return;
        userManager.loadUser(e.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(final PlayerLoginEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        if (userManager.containsUser(uuid)) return;
        userManager.loadUser(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLoginMonitor(final PlayerLoginEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        if (userManager.containsUser(uuid)) return;
        userManager.loadUser(uuid);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(final PlayerQuitEvent e) {
        bukkitScheduler.runTaskAsynchronously(plugin, () -> userManager.saveUser(e.getPlayer().getUniqueId()));
    }

}
