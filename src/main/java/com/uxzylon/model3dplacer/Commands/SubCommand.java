package com.uxzylon.model3dplacer.Commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.uxzylon.model3dplacer.Model3DPlacer.customModelDatas;

public abstract class SubCommand {
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract String permission();
    public abstract boolean canRunConsole();
    public abstract List<String> getSubcommandArguments(Player player, String[] args);
    public abstract void perform(Player player, String[] args);

    public List<String> getArgsCategoryModel(String[] args) {
        if (args.length == 2) {
            return new ArrayList<>(customModelDatas.keySet());
        } else if (args.length == 3) {
            if (customModelDatas.containsKey(args[1])) {
                return new ArrayList<>(customModelDatas.get(args[1]).keySet());
            }
        }
        return Collections.emptyList();
    }

    public ItemStack getItemFromCategoryModel(String category, String model) {
        if (!customModelDatas.containsKey(category) || !customModelDatas.get(category).containsKey(model)) {
            return null;
        }

        Pair<Material, Integer> pair = customModelDatas.get(category).get(model);
        ItemStack item = new ItemStack(pair.getLeft());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        meta.setCustomModelData(pair.getRight());
        meta.setDisplayName(model);
        item.setItemMeta(meta);

        return item;
    }

    public int getArmorStandCustomModelData(ArmorStand stand) {
        EntityEquipment equipment = stand.getEquipment();
        if (equipment == null) {
            return -1;
        }
        ItemMeta handMeta = equipment.getItemInMainHand().getItemMeta();
        if (handMeta != null) {
            return handMeta.getCustomModelData();
        } else {
            ItemStack helmet = stand.getEquipment().getHelmet();
            if (helmet == null) {
                return -1;
            }
            ItemMeta helmetMeta = helmet.getItemMeta();
            if (helmetMeta == null) {
                return -1;
            }
            return helmetMeta.getCustomModelData();
        }
    }

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void setSlotsDisabled(ArmorStand as, boolean slotsDisabled) {
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            for(ArmorStand.LockType lockType : ArmorStand.LockType.values()) {
                if(slotsDisabled) {
                    as.addEquipmentLock(slot, lockType);
                } else {
                    as.removeEquipmentLock(slot, lockType);
                }
            }
        }
    }

    public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    public static BlockFace yawToFace(float yaw) {
        return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    public static ArmorStand getClosestModel(Player player) {
        Entity closestArmorstand = null;
        double lowestDistance = Double.MAX_VALUE;
        ItemMeta metaHand;
        ItemMeta metaHead = null;
        Collection<Entity> near = player.getWorld().getNearbyEntities(player.getLocation(),3d,3d,3d);
        for (Entity entity : near) {
            if (!(entity instanceof ArmorStand stand)) {
                continue;
            }

            EntityEquipment standEquip = stand.getEquipment();
            if (standEquip == null) {
                continue;
            }

            ItemStack itemHand = standEquip.getItemInMainHand();
            ItemStack itemHead = standEquip.getHelmet();
            metaHand = itemHand.getItemMeta();
            if (itemHead != null) {
                metaHead = itemHead.getItemMeta();
            }

            ItemMeta meta = metaHand != null ? metaHand : metaHead;

            if (meta != null && meta.hasCustomModelData()) {
                double distance = entity.getLocation().distance(player.getLocation());
                if (distance < lowestDistance) {
                    if (stand.getCustomName() != null) {
                        if (!stand.getCustomName().contains("CAR_")) {
                            lowestDistance = distance;
                            closestArmorstand = entity;
                        }
                    } else {
                        lowestDistance = distance;
                        closestArmorstand = entity;
                    }
                }
            }
        }
        return (ArmorStand) closestArmorstand;
    }

    //skull
    public static ItemStack getCustomSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta == null) return head;

        GameProfile profile = new GameProfile(UUID.randomUUID(), "random");

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(SubCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

        head.setItemMeta(skullMeta);
        return head;
    }
}
