package com.omniscient.slash1v1.Commands;

import com.omniscient.slash1v1.Battle.Battle;
import com.omniscient.slash1v1.Battle.BattleCollectGUI;
import com.omniscient.slash1v1.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Command1v1Abort implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(Battle.currentBattle == null) commandSender.sendMessage(Methods.color("&cThere is no 1v1 happening at the moment."));
        else Battle.currentBattle.cancel("&cThe 1v1 has been aborted.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return new ArrayList<>();
    }
}
