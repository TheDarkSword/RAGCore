package it.revarmygaming.spigot.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    private final ItemStack item;
    private final int slot;

    public Button(ItemStack item, int slot) {
        this.item = item;
        this.slot = slot;
    }

    public abstract void onClick(Player player, InventoryClickEvent inventoryClickEvent, Menu menu, Button button);

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }
}
