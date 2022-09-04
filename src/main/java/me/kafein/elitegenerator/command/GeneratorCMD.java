package me.kafein.elitegenerator.command;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.config.FileManager;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.util.placeholder.PlaceHolder;
import org.bukkit.Bukkit;
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

        if (!sender.isOp()) {
            sender.sendMessage(fileManager.getMessage("noPerm"));
            return true;
        }

        if (args.length == 0) {
            fileManager.getMessageList("help").forEach(sender::sendMessage);
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {

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

            fileManager.loadFiles();
            PlaceHolder.reload();
            generatorManager.reloadGeneratorItem();
            generatorManager.reloadFirstBlockMaterial();

            sender.sendMessage(fileManager.getMessage("reload"));

            return true;

        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return Arrays.asList("give", "reload");
        return null;
    }

}
