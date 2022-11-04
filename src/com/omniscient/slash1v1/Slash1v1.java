package com.omniscient.slash1v1;

import com.omniscient.slash1v1.Battle.Battle;
import com.omniscient.slash1v1.Battle.BattleListener;
import com.omniscient.slash1v1.Commands.Command1v1;
import com.omniscient.slash1v1.Commands.Command1v1Abort;
import com.omniscient.slash1v1.Commands.Command1v1Leave;
import com.omniscient.slash1v1.Commands.Command1v1Location;
import com.omniscient.slash1v1.Utils.GUI.InventoryListener;
import com.omniscient.slash1v1.Utils.ItemStackSerialization;
import com.omniscient.slash1v1.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class Slash1v1 extends JavaPlugin {
    public static Slash1v1 plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getDataFolder().mkdir();

        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new BattleListener(), plugin);

        Bukkit.getServer().getPluginCommand("1v1").setExecutor(new Command1v1());
        Bukkit.getServer().getPluginCommand("1v1location").setExecutor(new Command1v1Location());
        Bukkit.getServer().getPluginCommand("1v1abort").setExecutor(new Command1v1Abort());
        Bukkit.getServer().getPluginCommand("1v1leave").setExecutor(new Command1v1Leave());

        try(FileReader file = new FileReader(new File(getDataFolder(), "locationStore.json"))){
            JSONObject locationStore = (JSONObject) new JSONParser().parse(file);
            JSONObject challengerLocation = (JSONObject) locationStore.get("challenger");
            if(challengerLocation != null) {
                Battle.challengerLocation = new Location(
                        Bukkit.getWorld((String) challengerLocation.get("world")),
                        (double) challengerLocation.get("x"),
                        (double) challengerLocation.get("y"),
                        (double) challengerLocation.get("z")
                );
                Battle.challengerLocation.setPitch((float) (double) challengerLocation.get("pitch"));
                Battle.challengerLocation.setYaw((float) (double) challengerLocation.get("yaw"));
            }
            JSONObject challengedLocation = (JSONObject) locationStore.get("challenged");
            if(challengedLocation != null) {
                Battle.challengedLocation = new Location(
                        Bukkit.getWorld((String) challengedLocation.get("world")),
                        (double) challengedLocation.get("x"),
                        (double) challengedLocation.get("y"),
                        (double) challengedLocation.get("z")
                );
                Battle.challengedLocation.setPitch((float) (double) challengedLocation.get("pitch"));
                Battle.challengedLocation.setYaw((float) (double) challengedLocation.get("yaw"));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try(FileReader file = new FileReader(new File(getDataFolder(), "collectStore.json"))){
            JSONObject collectStore = (JSONObject) new JSONParser().parse(file);
            collectStore.keySet().forEach(key -> {
                UUID uuid = UUID.fromString((String) key);
                ItemStack[] collected;
                try {
                    collected = ItemStackSerialization.itemStackArrayFromBase64((String) collectStore.get(key));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                Battle.collectedItems.put(uuid, collected);
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        Methods.consoleLog("&aPlugin enabled.");
    }

    @Override
    public void onDisable() {
        JSONObject challengerObject;
        if(Battle.challengerLocation != null) {
            challengerObject = new JSONObject();
            challengerObject.put("world", Battle.challengerLocation.getWorld().getName());
            challengerObject.put("x", Battle.challengerLocation.getX());
            challengerObject.put("y", Battle.challengerLocation.getY());
            challengerObject.put("z", Battle.challengerLocation.getZ());
            challengerObject.put("pitch", Battle.challengerLocation.getPitch());
            challengerObject.put("yaw", Battle.challengerLocation.getYaw());
        }else challengerObject = null;
        JSONObject challengedObject;
        if(Battle.challengedLocation != null) {
            challengedObject = new JSONObject();
            challengedObject.put("world", Battle.challengedLocation.getWorld().getName());
            challengedObject.put("x", Battle.challengedLocation.getX());
            challengedObject.put("y", Battle.challengedLocation.getY());
            challengedObject.put("z", Battle.challengedLocation.getZ());
            challengedObject.put("pitch", Battle.challengedLocation.getPitch());
            challengedObject.put("yaw", Battle.challengedLocation.getYaw());
        }else challengedObject = null;
        JSONObject locationStore = new JSONObject();
        locationStore.put("challenger", challengerObject);
        locationStore.put("challenged", challengedObject);
        try (FileWriter file = new FileWriter(new File(Slash1v1.plugin.getDataFolder(), "locationStore.json"))) {
            file.write(locationStore.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject collectStore = new JSONObject();
        Battle.collectedItems.keySet().forEach(uuid -> {
            collectStore.put(uuid.toString(), ItemStackSerialization.itemStackArrayToBase64(Battle.collectedItems.get(uuid)));
        });
        try (FileWriter file = new FileWriter(new File(Slash1v1.plugin.getDataFolder(), "collectStore.json"))) {
            file.write(collectStore.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Methods.consoleLog("&cPlugin disabled.");
    }
}
