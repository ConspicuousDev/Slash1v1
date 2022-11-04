package com.omniscient.slash1v1.Battle;

import com.omniscient.slash1v1.Slash1v1;
import com.omniscient.slash1v1.Utils.Methods;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Battle {
    public static Battle currentBattle = null;
    public static final Map<UUID, ItemStack[]> collectedItems = new HashMap<>();

    public static Location challengerLocation = null;
    public static Location challengedLocation = null;

    private boolean started;
    private final Player challenger;
    private final Player challenged;
    private boolean challengerReady = false;
    private boolean challengedReady = false;
    private BattleConfirmationGUI challengerGUI;
    private BattleConfirmationGUI challengedGUI;
    public Battle(Player challenger, Player challenged){
        this.challenger = challenger;
        this.challenged = challenged;
        this.challengerGUI = new BattleConfirmationGUI(challenger, this);
        this.challengedGUI = new BattleConfirmationGUI(challenged, this);
        currentBattle = this;
        challengerGUI.open();
        challengedGUI.open();
        Battle battle = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(currentBattle == battle && !battle.started) battle.cancel();
            }
        }.runTaskLater(Slash1v1.plugin, 30*20);
    }

    public boolean isStarted() {
        return started;
    }
    public Player getChallenger() {
        return challenger;
    }
    public Player getChallenged() {
        return challenged;
    }
    public boolean isChallengerReady() {
        return challengerReady;
    }
    public boolean isChallengedReady() {
        return challengedReady;
    }
    public BattleConfirmationGUI getChallengerGUI() {
        return challengerGUI;
    }
    public BattleConfirmationGUI getChallengedGUI() {
        return challengedGUI;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
    public void setChallengerReady(boolean challengerReady) {
        this.challengerReady = challengerReady;
    }
    public void setChallengedReady(boolean challengedReady) {
        this.challengedReady = challengedReady;
    }

    public void declareLoss(Player loser, List<ItemStack> loot){
        Player winner = challenger;
        if(loser == challenger) winner = challenged;
        List<ItemStack> items = new ArrayList<>(loot);
        if(Battle.collectedItems.containsKey(winner.getUniqueId()))
            items.addAll(Arrays.stream(Battle.collectedItems.get(winner.getUniqueId())).collect(Collectors.toList()));
        ItemStack[] collected = items.toArray(new ItemStack[]{});
        Battle.collectedItems.put(winner.getUniqueId(), collected);
        started = false;
        currentBattle = null;
        challenger.teleport(challenger.getWorld().getSpawnLocation());
        challenged.teleport(challenged.getWorld().getSpawnLocation());
        winner.sendMessage(Methods.color("&aYou won the 1v1! Use &7/1v1 collect &ato collect your loot."));
        loser.sendMessage(Methods.color("&cYou lost the 1v1. Better luck next time."));
    }
    public void start(){
        if(currentBattle != this) return;
        started = true;
        if(!challengerReady || !challengedReady){
            cancel();
            return;
        }
        if(challengerLocation == null || challengedLocation == null){
            cancel("&cThe arena locations have not been set. Contact a staff member if you think this is an error.");
            return;
        }
        challenger.getOpenInventory().close();
        challenged.getOpenInventory().close();
        challenger.teleport(challengerLocation);
        challenged.teleport(challengedLocation);
        final Battle battle = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(currentBattle == battle){
                    battle.cancel("&cThe battle has been ended since it exceeded the maximum time.");
                    battle.getChallenger().teleport(battle.getChallenger().getWorld().getSpawnLocation());
                    battle.getChallenged().teleport(battle.getChallenged().getWorld().getSpawnLocation());
                }
            }
        }.runTaskLater(Slash1v1.plugin, 10*60*20);
    }
    public void cancel(String error){
        if(currentBattle != this) return;
        if(started){
            challenger.teleport(challenger.getWorld().getSpawnLocation());
            challenged.teleport(challenged.getWorld().getSpawnLocation());
        }
        started = false;
        currentBattle = null;
        challenger.getOpenInventory().close();
        challenged.getOpenInventory().close();
        challenger.sendMessage(Methods.color(error));
        challenged.sendMessage(Methods.color(error));
    }
    public void cancel(){
        cancel("&cThe 1v1 has been canceled.");
    }
}
