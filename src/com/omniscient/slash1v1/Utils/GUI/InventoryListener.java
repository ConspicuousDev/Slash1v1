package com.omniscient.slash1v1.Utils.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {
    public static final List<GUI> openedGUIs = new ArrayList<>();

    public static GUI getOpenedGUI(Player player){
        return openedGUIs.stream()
                .filter(gui -> gui.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e){
        GUI gui = getOpenedGUI((Player) e.getWhoClicked());
        if(gui == null) return;
        if(e.getClickedInventory() == null) return;
        gui.onInteract(e);
        gui.update(e);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        GUI gui = getOpenedGUI((Player) e.getPlayer());
        if(gui == null) return;
        gui.onOpen(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        GUI gui = getOpenedGUI((Player) e.getPlayer());
        if(gui == null) return;
        openedGUIs.remove(gui);
        gui.onClose(e);
    }
}
