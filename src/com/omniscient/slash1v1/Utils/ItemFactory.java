package com.omniscient.slash1v1.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemFactory {
    public static final ItemStack PLACEHOLDER = makeItem("", null, Material.STAINED_GLASS_PANE, 7);
    public static final ItemStack BACK = makeItem("&eBack", "&7Click to go back.", Material.ARROW);

    public static ItemStack makeItem(String name, String lore, Material material, int b){
        ItemStack stack = new ItemStack(material, 1, (byte) b);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(Methods.color(name));
        if(lore != null) meta.setLore(Arrays.stream(lore.split("\n")).map(Methods::color).collect(Collectors.toList()));
        stack.setItemMeta(meta);
        return stack;
    }
    public static ItemStack makeItem(String name, String lore, Material material){
        return makeItem(name, lore, material, 0);
    }
    public static ItemStack makeSkull(String name, String lore, String texture){
        ItemStack stack = makeItem(name, lore, Material.SKULL_ITEM, 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
            e.printStackTrace();
        }
        stack.setItemMeta(meta);
        return stack;
    }
    public static ItemStack makeSkull(String name, String lore, Player texture){
        ItemStack stack = makeItem(name, lore, Material.SKULL_ITEM, 3);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(texture.getDisplayName());
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack addToLore(ItemStack stack, String lore){
        ItemStack clone = stack.clone();
        ItemMeta meta = stack.getItemMeta();
        List<String> list = stack.getItemMeta().getLore() == null ? new ArrayList<>() : stack.getItemMeta().getLore();
        list.addAll(Arrays.stream(lore.split("\n")).map(Methods::color).collect(Collectors.toList()));
        meta.setLore(list);
        clone.setItemMeta(meta);
        return clone;
    }
}
