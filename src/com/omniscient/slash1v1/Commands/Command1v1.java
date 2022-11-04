package com.omniscient.slash1v1.Commands;

import com.omniscient.slash1v1.Battle.Battle;
import com.omniscient.slash1v1.Battle.BattleCollectGUI;
import com.omniscient.slash1v1.Utils.GUI.GUI;
import com.omniscient.slash1v1.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Command1v1 implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(Methods.color("&cYou must be a player to execute this command."));
            return true;
        }
        if(strings.length < 1){
            commandSender.sendMessage(Methods.color("&cPlease use: "+command.getUsage()));
            return true;
        }
        if(Battle.currentBattle != null){
            commandSender.sendMessage(Methods.color("&cWait for the current 1v1 to end."));
            return true;
        }
        Player player = (Player) commandSender;
        if(strings[0].equalsIgnoreCase("collect")){
            new BattleCollectGUI(player).open();
        }else {
            Player target = Bukkit.getPlayer(strings[0]);
            if (target == null) {
                commandSender.sendMessage(Methods.color("&cThe player &7" + strings[0] + " &cwas not found."));
                return true;
            }
            if (player == target) {
                commandSender.sendMessage(Methods.color("&cYou cannot challenge yourself."));
                return true;
            }
            new Battle(player, target);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();
        if(strings.length == 1) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            completions.add("collect");
        }
        return completions;
    }
}
