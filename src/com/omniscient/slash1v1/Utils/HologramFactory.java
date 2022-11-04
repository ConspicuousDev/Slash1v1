package com.omniscient.slash1v1.Utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class HologramFactory {
    public static ArmorStand makeHologram(Location location, String string){
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(Methods.color(string));
        armorStand.getNearbyEntities(.1, .1 ,.1).forEach(entity -> {
            if(!(entity instanceof ArmorStand)) return;
            if(!(entity.getCustomName().equals(armorStand.getCustomName()))) return;
            entity.remove();
        });
        return armorStand;
    }
}
