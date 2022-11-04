package com.omniscient.slash1v1.Battle;

import com.omniscient.slash1v1.Slash1v1;
import com.omniscient.slash1v1.Utils.GUI.GUI;
import com.omniscient.slash1v1.Utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class BattleConfirmationGUI extends GUI {
    private final int CHALLENGER = 11;
    private final int CHALLENGED = 15;

    private final Battle battle;
    public BattleConfirmationGUI(Player player, Battle battle) {
        super(player);
        this.battle = battle;
    }

    @Override
    protected Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, battle.getChallenger().getDisplayName()+" vs "+battle.getChallenged().getDisplayName());
        for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, ItemFactory.PLACEHOLDER);
        inventory.setItem(CHALLENGER, ItemFactory.makeSkull((battle.isChallengerReady() ? "&a" : "&c")+"Challenger: "+battle.getChallenger().getDisplayName(), null, battle.getChallenger()));
        inventory.setItem(CHALLENGED, ItemFactory.makeSkull((battle.isChallengedReady() ? "&a" : "&c")+"Challenged: "+battle.getChallenged().getDisplayName(), null, battle.getChallenged()));
        inventory.setItem(CHALLENGER+9, ItemFactory.makeItem((battle.isChallengerReady() ? "&aReady" : "&cNot Ready"), null, Material.STAINED_GLASS_PANE, (battle.isChallengerReady() ? 5 : 14)));
        inventory.setItem(CHALLENGED+9, ItemFactory.makeItem((battle.isChallengedReady() ? "&aReady" : "&cNot Ready"), null, Material.STAINED_GLASS_PANE, (battle.isChallengedReady() ? 5 : 14)));
        return inventory;
    }

    @Override
    public void onInteract(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getSlot() == (player == battle.getChallenger() ? CHALLENGER+9 : CHALLENGED+9)) {
            if (player == battle.getChallenger()) {
                battle.setChallengerReady(!battle.isChallengerReady());
                battle.getChallengedGUI().update(null);
            }else {
                battle.setChallengedReady(!battle.isChallengedReady());
                battle.getChallengerGUI().update(null);
            }
        }
        if(battle.isChallengerReady() && battle.isChallengedReady()) battle.start();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        if(battle.isStarted()) return;
        battle.cancel();
    }
}
