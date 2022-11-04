package com.omniscient.slash1v1.Battle;

import com.omniscient.slash1v1.Utils.GUI.GUI;
import com.omniscient.slash1v1.Utils.ItemFactory;
import com.omniscient.slash1v1.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BattleCollectGUI extends GUI {
    private final int NEXT = 53;
    private final int PREVIOUS = 45;

    private int page = 0;
    public BattleCollectGUI(Player player) {
        super(player);
    }

    @Override
    protected Inventory getInventory() {
        int maxPage = Battle.collectedItems.containsKey(player.getUniqueId()) ? (int) Math.ceil((float) Battle.collectedItems.get(player.getUniqueId()).length/45) : 0;
        Inventory inventory = Bukkit.createInventory(null, 54, "1v1 Loot ("+(page+1)+" of "+(maxPage == 0 ? 1 : maxPage)+")");
        if(Battle.collectedItems.containsKey(player.getUniqueId())){
            List<ItemStack> loot = Arrays.stream(Battle.collectedItems.get(player.getUniqueId())).collect(Collectors.toList());
            for (int i = 0; i < 45; i++) {
                if(i+page*45 >= loot.size()) break;
                inventory.setItem(i, loot.get(i+page*45));
            }
            if(page > 0) inventory.setItem(PREVIOUS, ItemFactory.makeItem("&ePrevious", "&7Click to see the previous page.", Material.ARROW));
            if(page+1 < maxPage) inventory.setItem(NEXT, ItemFactory.makeItem("&eNext", "&7Click to see the next page.", Material.ARROW));
        }else inventory.setItem(22, ItemFactory.makeItem("&cNo Loot Collected", "&7You don't have any loot\n&7to collect at this time.\n", Material.BARRIER));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(Battle.collectedItems.containsKey(player.getUniqueId())){
            int maxPage = Battle.collectedItems.containsKey(player.getUniqueId()) ? (int) Math.ceil((float) Battle.collectedItems.get(player.getUniqueId()).length/45) : 0;
            if(e.getSlot() < 45){
                if(Arrays.stream(player.getInventory().getContents()).anyMatch(stack -> stack == null || stack.getType() == Material.AIR)) {
                    List<ItemStack> loot = Arrays.stream(Battle.collectedItems.get(player.getUniqueId())).collect(Collectors.toList());
                    ItemStack stack = loot.remove(e.getSlot() + page * 45);
                    player.getInventory().addItem(stack);
                    if(loot.size() > 0) Battle.collectedItems.put(player.getUniqueId(), loot.toArray(new ItemStack[]{}));
                    else Battle.collectedItems.remove(player.getUniqueId());
                }else player.sendMessage(Methods.color("&cYou need more space in you inventory to collect this item."));
            }else if(e.getSlot() == PREVIOUS && page > 0) {
                player.closeInventory();
                page--;
                open();
            }else if(e.getSlot() == NEXT && page+1 < maxPage) {
                player.closeInventory();
                page++;
                open();
            }
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }
    @Override
    public void onClose(InventoryCloseEvent e) {

    }
}
