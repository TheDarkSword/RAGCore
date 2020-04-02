package it.revarmygaming.spigot.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = getOpenedMenu(player);
        if(menu == null)
            return;

        menu.close(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Menu menu = getOpenedMenu(player);
        if(menu == null)
            return;

        event.setCancelled(true);
        Button clicked = menu.getButton(event.getSlot());
        if(clicked == null)
            return;

        clicked.onClick(player, event, menu, clicked);
    }

    private Menu getOpenedMenu(Player player) {
        if(!player.hasMetadata(Menu.INVENTORY_META))
            return null;

        return (Menu) player.getMetadata(Menu.INVENTORY_META).get(0).value();
    }
}
