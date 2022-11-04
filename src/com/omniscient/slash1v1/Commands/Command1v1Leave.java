package com.omniscient.slash1v1.Commands;

import com.omniscient.slash1v1.Battle.Battle;
import com.omniscient.slash1v1.Slash1v1;
import com.omniscient.slash1v1.Utils.Methods;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Command1v1Leave implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(Methods.color("&cYou must be a player to execute this command."));
            return true;
        }
        if(Battle.currentBattle == null || !Battle.currentBattle.isStarted()){
            commandSender.sendMessage(Methods.color("&cThere is no 1v1 happening at the moment."));
            return true;
        }
        final Player player = (Player) commandSender;
        if(Battle.currentBattle.getChallenger()  != player && Battle.currentBattle.getChallenged() != player){
            commandSender.sendMessage(Methods.color("&cYou are not part of the current 1v1."));
            return true;
        }
        final Battle battle = Battle.currentBattle;
        AtomicInteger time = new AtomicInteger(10);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(Battle.currentBattle != battle) {
                    cancel();
                    return;
                }
                PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(Methods.color("&c"+time.get()+" second(s) until tie.")), (byte)2);
                ((CraftPlayer) battle.getChallenger()).getHandle().playerConnection.sendPacket(packet);
                ((CraftPlayer) battle.getChallenged()).getHandle().playerConnection.sendPacket(packet);
                if(time.addAndGet(-1) <= 0){
                    if(Battle.currentBattle != battle) {
                        cancel();
                        return;
                    }
                    battle.cancel("&e"+player.getName()+" left the battle.");
                }
            }
        }.runTaskTimer(Slash1v1.plugin, 0, 20);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
