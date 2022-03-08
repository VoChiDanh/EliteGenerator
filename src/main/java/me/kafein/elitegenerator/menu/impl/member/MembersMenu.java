package me.kafein.elitegenerator.menu.impl.member;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.menu.Menu;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import me.kafein.elitegenerator.menu.impl.SettingsMenu;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class MembersMenu extends Menu {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    public MembersMenu(String title, int slot, FileConfig fileConfig) {
        super(title, slot, fileConfig);
    }

    @Override
    public void loadItems() {

    }

    public void openMenu(Player player, Generator generator, int page) {

        final Inventory inventory = this.clone();

        final List<UUID> generatorMembers = generator.getGeneratorMembers();

        if (generatorMembers.isEmpty()) return;

        int paginated = (page * 36);

        int minPageEntry = paginated - 36;

        if (minPageEntry > generatorMembers.size()) return;

        for (int i = 0; i < 36; i++) {

            final int pageEntry = minPageEntry + i;

            if (pageEntry >= generatorMembers.size()) break;

            final UUID memberUUID = generatorMembers.get(pageEntry);
            final String memberName = Bukkit.getOfflinePlayer(memberUUID).getName();

            final ItemBuilder itemBuilder = loadItem("member", memberName, generator);
            itemBuilder.setString("generator", generator.getGeneratorUUID().toString());
            itemBuilder.setString("memberUUID", memberUUID.toString());
            itemBuilder.setString("memberName", memberName);
            inventory.setItem(i + 9, itemBuilder.toItemStack());

        }

        if (inventory.getItem(9) == null) {
            if (page - 1 <= 0) player.closeInventory();
            else openMenu(player, generator, page - 1);
        }

        inventory.setItem(48, loadItem("previousPage", player.getName(), generator)
                .setString("generator", generator.getGeneratorUUID().toString())
                .setInteger("page", page).toItemStack());

        inventory.setItem(49, loadItem("close", player.getName(), generator)
                .setString("generator", generator.getGeneratorUUID().toString()).toItemStack());

        inventory.setItem(50, loadItem("nextPage", player.getName(), generator)
                .setString("generator", generator.getGeneratorUUID().toString())
                .setInteger("page", page).toItemStack());

        final ItemStack fillItem = getFillItem();
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, fillItem);
            if (i >= 3 && i <= 5) continue;
            inventory.setItem(45 + i, fillItem);
        }

        Bukkit.getScheduler().runTask(getPlugin(), () -> player.openInventory(inventory));

    }

    @EventHandler
    public void onClick(MenuClickEvent e) {

        if (!e.getMenu().getTitle().equals(getTitle())) return;

        final Player player = e.getPlayer();

        final NBTItem nbtItem = new NBTItem(e.getItem());

        if (!nbtItem.hasKey("generator")) return;

        final Generator generator = generatorManager.getGenerator(UUID.fromString(nbtItem.getString("generator")));
        if (generator == null) {
            player.closeInventory();
            return;
        }

        if (e.getSlot() >= 9 && e.getSlot() <= 45) {

            final UUID memberUUID = UUID.fromString(nbtItem.getString("memberUUID"));
            final String memberName = nbtItem.getString("memberName");

            if (memberUUID.equals(generator.getOwnerUUID())) return;

            final MemberMenu memberMenu = (MemberMenu) getMenuManager().getMenu(MenuManager.MenuType.MEMBER);
            memberMenu.openMenu(player, generator, memberUUID, memberName);

        } else if (e.getSlot() == 48) {

            final int page = nbtItem.getInteger("page");

            if ((page - 1) <= 0) return;

            openMenu(player, generator, page - 1);

        } else if (e.getSlot() == 49) {

            final SettingsMenu settingsMenu = (SettingsMenu) getMenuManager().getMenu(MenuManager.MenuType.SETTINGS);
            settingsMenu.openMenu(player, generator);

        } else if (e.getSlot() == 50) {

            final int page = nbtItem.getInteger("page");

            openMenu(player, generator, page + 1);

        }

    }

    @Override
    public void onOpen(MenuOpenEvent e) {

    }

    @Override
    public void onClose(MenuCloseEvent e) {

    }

}
