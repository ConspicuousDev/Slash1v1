package com.omniscient.slash1v1.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Methods {
    public static String color(Object o){
        return ChatColor.translateAlternateColorCodes('&', o.toString());
    }
    public static void consoleLog(Object o){
        if(Bukkit.getServer() != null)
            Bukkit.getServer().getConsoleSender().sendMessage(Methods.color("&5[&fSlash&c1v1&5] &f"+o));
        else
            System.out.println("[LockedBox] "+ChatColor.stripColor(o.toString()));
    }
    public static String capitalize(String string) {
        return Arrays.stream(string.split(" ")).map(word -> word.substring(0, 1).toUpperCase()+word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
    }
    public static String stripColor(String string) {
        return ChatColor.stripColor(string).replaceAll("§", "&").replaceAll("&[0-9A-FKLRa-fklr]", "");
    }
}
