package com.gmail.anthony17j.model3dplacer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Utils {

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
        ItemMeta metaHand = null;
        ItemMeta metaHead = null;
        Collection<Entity> near = player.getWorld().getNearbyEntities(player.getLocation(),3d,3d,3d);
        for (Entity entity : near) {
            if (entity instanceof ArmorStand) {
                ArmorStand stand = (ArmorStand) entity;
                EntityEquipment standEquip = stand.getEquipment();
                if (standEquip != null) {
                    ItemStack itemHand = standEquip.getItemInMainHand();
                    ItemStack itemHead = standEquip.getHelmet();
                    metaHand = itemHand.getItemMeta();
                    metaHead = itemHead.getItemMeta();
                    if (metaHand != null) {
                        if (metaHand.hasCustomModelData()) {
                            double distance = entity.getLocation().distance(player.getLocation());
                            if (distance < lowestDistance) {
                                lowestDistance = distance;
                                closestArmorstand = entity;
                            }
                        }
                    } else if (metaHead != null) {
                        if (metaHead.hasCustomModelData()) {
                            double distance = entity.getLocation().distance(player.getLocation());
                            if (distance < lowestDistance) {
                                lowestDistance = distance;
                                closestArmorstand = entity;
                            }
                        }
                    }
                }
            }
        }
        return (ArmorStand) closestArmorstand;
    }

    public static HashMap<UUID, ArmorStand> selectedStand = new HashMap<UUID, ArmorStand>();

    //skull
    public static final ItemStack getCustomSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;
    }

    public static String playerOnly = "Les commandes ne peuvent être éxécutées que par un joueur";
    public static String noSelection = ChatColor.GOLD + "Vous n'avez pas de selection ! " + ChatColor.LIGHT_PURPLE + "/select3dmodel";
    public static String noPermission = ChatColor.RED + "Vous n'avez pas la permission d'exécuter cette commande";
    public static String needId = ChatColor.RED + "Vous devez fournir un Id CustomModelData";

    public static String MoveMenu = ChatColor.RED + "Menu déplacement " + ChatColor.DARK_GREEN + "Par Uxzylon";
    public static String MoveMenuUp = ChatColor.GREEN + "AVANT";
    public static String MoveMenuDown = ChatColor.GREEN + "ARRIERE";
    public static String MoveMenuLeft = ChatColor.GREEN + "GAUCHE";
    public static String MoveMenuRight = ChatColor.GREEN + "DROITE";
    public static String MoveMenuWhiteUp = ChatColor.GOLD + "HAUT";
    public static String MoveMenuWhiteDown = ChatColor.GOLD + "BAS";
    public static String MoveMenuRotateLeft = ChatColor.DARK_AQUA + "ROTATION GAUCHE";
    public static String MoveMenuRotateRight = ChatColor.DARK_AQUA + "ROTATION DROITE";
    public static String MoveMenuRotateRightSide = ChatColor.BLUE + "ROTATION COTE DROIT";
    public static String MoveMenuRotateLeftSide = ChatColor.BLUE + "ROTATION COTE GAUCHE";
    public static String MoveMenuRotateFront = ChatColor.LIGHT_PURPLE + "ROTATION AVANT";
    public static String MoveMenuRotateBack = ChatColor.LIGHT_PURPLE + "ROTATION ARRIERE";
    public static String MoveMenuExit = ChatColor.RED + "EXIT";
}
