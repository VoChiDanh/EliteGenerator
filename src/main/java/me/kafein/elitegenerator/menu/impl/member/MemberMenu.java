package me.kafein.elitegenerator.menu.impl.member;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileConfig;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.generator.GeneratorMember;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import me.kafein.elitegenerator.menu.Menu;
import me.kafein.elitegenerator.menu.MenuManager;
import me.kafein.elitegenerator.menu.event.MenuClickEvent;
import me.kafein.elitegenerator.menu.event.MenuCloseEvent;
import me.kafein.elitegenerator.menu.event.MenuOpenEvent;
import me.kafein.elitegenerator.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class MemberMenu extends Menu {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    public MemberMenu(String title, int slot, FileConfig fileConfig) {
        super(title, slot, fileConfig);
    }

    @Override
    public void loadItems() {

    }

    public void openMenu(Player player, Generator generator, UUID memberUUID, String memberName) {

        final Inventory inventory = this.clone();

        final ItemBuilder itemBuilder = getItem("member", generator, memberUUID, memberName);
        inventory.setItem(13, itemBuilder.toItemStack());

        final ItemBuilder openPermItem = getItem("openSettingsPerm", generator, memberUUID, memberName);
        inventory.setItem(30, openPermItem.toItemStack());
        inventory.setItem(39, (generator.containsMemberPermission(memberUUID, MemberPermission.OPEN_SETTINGS)
                ? getEnabledItem(generator, memberUUID, memberName)
                : getDisabledItem(generator, memberUUID, memberName)));


        final ItemBuilder breakPermItem = getItem("breakGeneratorPerm", generator, memberUUID, memberName);
        inventory.setItem(31, breakPermItem.toItemStack());
        inventory.setItem(40, (generator.containsMemberPermission(memberUUID, MemberPermission.BREAK_GENERATOR)
                ? getEnabledItem(generator, memberUUID, memberName)
                : getDisabledItem(generator, memberUUID, memberName)));

        final ItemBuilder changePermItem = getItem("changeSettingsPerm", generator, memberUUID, memberName);
        inventory.setItem(32, changePermItem.toItemStack());
        inventory.setItem(41, (generator.containsMemberPermission(memberUUID, MemberPermission.CHANGE_SETTINGS)
                ? getEnabledItem(generator, memberUUID, memberName)
                : getDisabledItem(generator, memberUUID, memberName)));

        inventory.setItem(45, loadItem("close", memberName, generator)
                .setString("generator", generator.getGeneratorUUID().toString())
                .toItemStack());

        if (getFileConfig().getBoolean("menu.fill")) fill(inventory);

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

        if (!generator.containsMemberPermission(player.getUniqueId(), MemberPermission.CHANGE_SETTINGS)) return;

        if (e.getSlot() == 30 || e.getSlot() == 39) {

            final UUID memberUUID = UUID.fromString(nbtItem.getString("memberUUID"));
            final String memberName = nbtItem.getString("memberName");

            if (memberUUID == generator.getOwnerUUID()) return;

            if (!generator.containsGeneratorMember(memberUUID)){
                player.closeInventory();
                return;
            }

            final GeneratorMember generatorMember = generator.getGeneratorMember(memberUUID);

            if (generatorMember.containsPermission(MemberPermission.OPEN_SETTINGS)) generatorMember.removePermission(MemberPermission.OPEN_SETTINGS);
            else generatorMember.addPermission(MemberPermission.OPEN_SETTINGS);

            openMenu(player, generator, memberUUID, memberName);

        } else if (e.getSlot() == 31 || e.getSlot() == 40) {

            final UUID memberUUID = UUID.fromString(nbtItem.getString("memberUUID"));
            final String memberName = nbtItem.getString("memberName");

            if (memberUUID == generator.getOwnerUUID()) return;

            if (!generator.containsGeneratorMember(memberUUID)){
                player.closeInventory();
                return;
            }

            final GeneratorMember generatorMember = generator.getGeneratorMember(memberUUID);

            if (generatorMember.containsPermission(MemberPermission.BREAK_GENERATOR)) generatorMember.removePermission(MemberPermission.BREAK_GENERATOR);
            else generatorMember.addPermission(MemberPermission.BREAK_GENERATOR);

            openMenu(player, generator, memberUUID, memberName);

        } else if (e.getSlot() == 32 || e.getSlot() == 41) {

            final UUID memberUUID = UUID.fromString(nbtItem.getString("memberUUID"));
            final String memberName = nbtItem.getString("memberName");

            if (memberUUID == generator.getOwnerUUID()) return;

            if (!generator.containsGeneratorMember(memberUUID)){
                player.closeInventory();
                return;
            }

            final GeneratorMember generatorMember = generator.getGeneratorMember(memberUUID);

            if (generatorMember.containsPermission(MemberPermission.CHANGE_SETTINGS)) generatorMember.removePermission(MemberPermission.CHANGE_SETTINGS);
            else generatorMember.addPermission(MemberPermission.CHANGE_SETTINGS);

            openMenu(player, generator, memberUUID, memberName);

        } else if (e.getSlot() == 45) {

            final MembersMenu membersMenu = (MembersMenu) getMenuManager().getMenu(MenuManager.MenuType.MEMBERS);
            membersMenu.openMenu(player, generator, 1);

        }

    }

    @Override
    public void onOpen(MenuOpenEvent e) {

    }

    @Override
    public void onClose(MenuCloseEvent e) {

    }

    private ItemStack getEnabledItem(final Generator generator, final UUID memberUUID, final String memberName) {

        ItemBuilder itemBuilder;

        Material material = Material.getMaterial("GREEN_STAINED_GLASS_PANE");
        if (material == null) itemBuilder = new ItemBuilder(Material.getMaterial("STAINED_GLASS_PANE"), 1, (byte) 5);
        else itemBuilder = new ItemBuilder(material, 1, (byte) 0);

        itemBuilder.setString("generator", generator.getGeneratorUUID().toString());
        itemBuilder.setString("memberUUID", memberUUID.toString());
        itemBuilder.setString("memberName", memberName);

        return itemBuilder.toItemStack();

    }

    private ItemStack getDisabledItem(final Generator generator, final UUID memberUUID, final String memberName) {

        ItemBuilder itemBuilder;

        Material material = Material.getMaterial("RED_STAINED_GLASS_PANE");
        if (material == null) itemBuilder = new ItemBuilder(Material.getMaterial("STAINED_GLASS_PANE"), 1, (byte) 14);
        else itemBuilder = new ItemBuilder(material, 1, (byte) 0);

        itemBuilder.setString("generator", generator.getGeneratorUUID().toString());
        itemBuilder.setString("memberUUID", memberUUID.toString());
        itemBuilder.setString("memberName", memberName);

        return itemBuilder.toItemStack();

    }

    private ItemBuilder getItem(final String property, final Generator generator, final UUID memberUUID, final String memberName) {

        final ItemBuilder itemBuilder = loadItem(property, memberName, generator);
        itemBuilder.setString("generator", generator.getGeneratorUUID().toString());
        itemBuilder.setString("memberUUID", memberUUID.toString());
        itemBuilder.setString("memberName", memberName);
        final List<String> itemBuilderLoreList = itemBuilder.getLore();
        itemBuilderLoreList.replaceAll(lore -> replace(lore, generator, memberUUID));
        itemBuilder.setLore(itemBuilderLoreList);
        itemBuilder.setName(replace(itemBuilder.getName(), generator, memberUUID));

        return itemBuilder;

    }

    private String replace(final String text, final Generator generator, final UUID memberUUID) {

        return text
                .replace("%generator_break_permission%", Boolean.toString(generator.containsMemberPermission(memberUUID, MemberPermission.BREAK_GENERATOR)))
                .replace("%generator_open_permission%", Boolean.toString(generator.containsMemberPermission(memberUUID, MemberPermission.OPEN_SETTINGS)))
                .replace("%generator_change_permission%", Boolean.toString(generator.containsMemberPermission(memberUUID, MemberPermission.CHANGE_SETTINGS)));

    }

}
