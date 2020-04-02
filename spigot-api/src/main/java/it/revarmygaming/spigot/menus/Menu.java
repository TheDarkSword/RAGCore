package it.revarmygaming.spigot.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Menu {

    public static final String INVENTORY_META = "RAGCore_Menu";

    private Plugin plugin;

    protected String title;
    protected int rows;
    protected List<Button> buttons;

    public Menu(Plugin plugin) {
        this.plugin = plugin;
        buttons = new ArrayList<>();
    }

    public Menu(Plugin plugin, String title, int rows) {
        this(plugin);

        this.title = title;
        this.rows = rows;
    }

    public Menu(Plugin plugin, String title, int rows, Button... buttons) {
        this(plugin, title, rows);
        this.buttons.addAll(Arrays.asList(buttons));
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public void showToPlayer(Player player) {
        onPreOpen(player);

        Inventory inventory = Bukkit.createInventory(null, rows*9, title);
        buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItem()));

        onOpen(player);

        player.openInventory(inventory);
        player.setMetadata(INVENTORY_META, new FixedMetadataValue(plugin, this));
    }

    public void close(Player player) {
        player.removeMetadata(INVENTORY_META, plugin);
        player.closeInventory();

        onClose(player);
    }

    public Button getButton(int slot) {
        for (Button button : buttons) {
            if(button.getSlot() == slot)
                return button;
        }

        return null;
    }

    public void refresh(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.clear();

        buttons.forEach(button -> inventory.setItem(button.getSlot(), button.getItem()));
    }

    public abstract void onClose(Player player);
    public abstract void onPreOpen(Player player);
    public abstract void onOpen(Player player);

    public Plugin getPlugin() {
        return plugin;
    }
}
