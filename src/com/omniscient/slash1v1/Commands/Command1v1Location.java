package com.omniscient.slash1v1.Commands;

import com.omniscient.slash1v1.Battle.Battle;
import com.omniscient.slash1v1.Slash1v1;
import com.omniscient.slash1v1.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Command1v1Location implements TabExecutor {
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
        if(!Arrays.asList("challenger", "challenged").contains(strings[0])){
            commandSender.sendMessage(Methods.color("&cThe location &7"+strings[0]+"&c wasn't found."));
            return true;
        }
        if(strings[0].equals("challenger"))
            Battle.challengerLocation = ((Player) commandSender).getLocation();
        else if(strings[0].equals("challenged"))
            Battle.challengedLocation = ((Player) commandSender).getLocation();
        commandSender.sendMessage(Methods.color("&eLocation &7"+strings[0]+"&e has been set to your current position."));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();
        if(strings.length == 1)
            completions.addAll(Arrays.asList("challenger", "challenged"));
        return completions;
    }
}
