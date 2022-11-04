package com.omniscient.slash1v1.Battle;

import com.omniscient.slash1v1.Utils.Methods;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class BattleListener implements Listener {
    @EventHandler
    public void onBattleCommand(PlayerCommandPreprocessEvent e){
        if(Battle.currentBattle == null) return;
        if(Battle.currentBattle.getChallenger()  != e.getPlayer() && Battle.currentBattle.getChallenged() != e.getPlayer()) return;
        if(e.getMessage().toLowerCase().startsWith("/1v1leave")) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(Methods.color("&cYou cannot use commands during a 1v1."));
    }

    @EventHandler
    public void onBattleDeath(PlayerDeathEvent e){
        if(Battle.currentBattle == null) return;
        if(Battle.currentBattle.getChallenger()  != e.getEntity() && Battle.currentBattle.getChallenged() != e.getEntity()) return;
        List<ItemStack> loot = new ArrayList<>();
        loot.addAll(Arrays.stream(e.getEntity().getInventory().getContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).collect(Collectors.toList()));
        loot.addAll(Arrays.stream(e.getEntity().getInventory().getArmorContents()).filter(stack -> stack != null && stack.getType() != Material.AIR).collect(Collectors.toList()));
        e.setKeepInventory(true);
        e.getEntity().getInventory().clear();
        e.getEntity().getInventory().setArmorContents(null);
        Battle.currentBattle.declareLoss(e.getEntity(), loot);
    }
}
