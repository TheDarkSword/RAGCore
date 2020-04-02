package it.revarmygaming.spigot.holograms;

import it.revarmygaming.spigot.common.Chat;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class HologramLine {

    private String text;
    private ArmorStand armorStand;

    public HologramLine(String text){
        this.text = Chat.getTranslated(text);
    }

    public void spawn(Location location){
        if(armorStand != null) despawn();

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setCustomName(text);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCanPickupItems(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(false);
    }

    public void despawn() {
        if(armorStand == null) return;

        armorStand.remove();
        armorStand = null;
    }

    public void setText(String text) {
        this.text = Chat.getTranslated(text);
        if(armorStand != null)
            armorStand.setCustomName(this.text);
    }
}
