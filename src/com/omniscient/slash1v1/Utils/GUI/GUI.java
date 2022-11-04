package com.omniscient.slash1v1.Utils.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public abstract class GUI {
    protected final Player player;
    protected GUI(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    protected abstract Inventory getInventory();
    public abstract void onInteract(InventoryClickEvent e);
    public abstract void onOpen(InventoryOpenEvent e);
    public abstract void onClose(InventoryCloseEvent e);

    public void open(){
        player.getOpenInventory().close();
        player.openInventory(getInventory());
        InventoryListener.openedGUIs.add(this);
    }

    public void update(InventoryClickEvent e){
        if(e != null && !e.isCancelled()) return;
        if(e != null && !e.getInventory().equals(player.getOpenInventory().getTopInventory())) return;
        Inventory inventory = getInventory();
        if(player.getOpenInventory() == null) return;
        if(player.getOpenInventory().getTopInventory() == null) return;
        if(player.getOpenInventory().getTopInventory().getSize() != inventory.getSize()) return;
        for (int i = 0; i < inventory.getSize(); i++) {
            player.getOpenInventory().getTopInventory().setItem(i, inventory.getItem(i));
        }
    }
}
