package me.kafein.elitegenerator.command;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.util.ItemChecker;
import me.kafein.elitegenerator.util.material.XMaterial;
import me.kafein.elitegenerator.util.placeholder.PlaceHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GeneratorCMD implements TabExecutor {

    final private FileManager fileManager = EliteGenerator.getInstance().getFileManager();
    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (args.length == 0) {
            if (!sender.isOp()) {
                sender.sendMessage(fileManager.getMessage("noPerm"));
                return true;
            }
            fileManager.getMessageList("help").forEach(sender::sendMessage);
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.isOp()) {
                sender.sendMessage(fileManager.getMessage("noPerm"));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("/generator give <target>");
                return true;
            }

            final Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(fileManager.getMessage("generator.targetIsNotOnline"));
                return true;
            }

            sender.sendMessage(fileManager.getMessage("generator.generatorItemGivenToTarget"));

            final ItemStack generatorItem = generatorManager.getGeneratorItem().create(1, false, false, false, false);

            final Map<Integer, ItemStack> itemStackMap = target.getInventory().addItem(generatorItem);
            if (!itemStackMap.isEmpty()) target.getWorld().dropItemNaturally(target.getLocation(), generatorItem);
            target.sendMessage(fileManager.getMessage("generator.givenGeneratorItem"));

            return true;

        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.isOp()) {
                sender.sendMessage(fileManager.getMessage("noPerm"));
                return true;
            }

            fileManager.loadFiles();
            PlaceHolder.reload();
            generatorManager.reloadGeneratorItem();
            generatorManager.reloadFirstBlockMaterial();

            sender.sendMessage(fileManager.getMessage("reload"));

            return true;

        } else if (args[0].equalsIgnoreCase("get")) {
            Player p = (Player) sender;
            if (args.length >= 2 && args[1].equalsIgnoreCase("confirm")) {
                if (ItemChecker.getMaterialAmount(p.getInventory(), XMaterial.HOPPER) >= 4 &&
                        ItemChecker.getMaterialAmount(p.getInventory(), XMaterial.REDSTONE) >= 16) {
                    ItemChecker.removeAmount(p.getInventory(), XMaterial.REDSTONE, 16);
                    ItemChecker.removeAmount(p.getInventory(), XMaterial.HOPPER, 4);
                    final ItemStack generatorItem = generatorManager.getGeneratorItem().create(1, false, false, false, false);

                    final Map<Integer, ItemStack> itemStackMap = p.getInventory().addItem(generatorItem);
                    if (!itemStackMap.isEmpty()) p.getWorld().dropItemNaturally(p.getLocation(), generatorItem);
                    p.sendMessage(fileManager.getMessage("generator.givenGeneratorItem"));
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&8[&x&9&a&9&6&e&c矿物&8] &c你没有足够的物品来兑换"));
                }
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&x&9&a&9&6&e&c矿物&8] &c你需要 &74* 漏斗 &c和 &716* 红石粉 &c来兑换一个生成器!"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&x&9&a&9&6&e&c矿物&8] &c输入 &7/generator get confirm &c来确认兑换"));
            }
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return Arrays.asList("give", "reload", "get");
        return null;
    }

}
