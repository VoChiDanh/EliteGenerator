package me.kafein.elitegenerator.util;

import me.kafein.elitegenerator.util.material.XMaterial;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemChecker {

    public static void removeAmount(Inventory inventory, XMaterial material, int amount) {
        int removed = 0;
        int index = 0;
        ItemStack[] var5 = inventory.getContents();
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            ItemStack itemStack = var5[var7];
            if (itemStack == null) {
                ++index;
            } else {
                if (removed >= amount) {
                    break;
                }

                if (material.isSimilar(itemStack)) {
                    if (removed + itemStack.getAmount() <= amount) {
                        removed += itemStack.getAmount();
                        inventory.setItem(index, (ItemStack) null);
                    } else {
                        itemStack.setAmount(itemStack.getAmount() - (amount - removed));
                        removed += amount;
                    }
                }

                ++index;
            }
        }

    }

    public static int getMaterialAmount(Inventory inventory, XMaterial material) {
        int total = 0;
        ItemStack[] var3 = inventory.getContents();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            ItemStack item = var3[var5];
            if (item != null && material.isSimilar(item) && !item.hasItemMeta()) {
                total += item.getAmount();
            }
        }

        return total;
    }

}
