package com.gmail.anthony17j.model3dplacer.Events;

import com.gmail.anthony17j.model3dplacer.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.util.EulerAngle;

public class moveMenuClick implements Listener {
    @EventHandler
    public void onEvent(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(Utils.MoveMenu)) {
            if (e.getCurrentItem() != null) {
                Player player = (Player) e.getWhoClicked();
                ArmorStand stand = Utils.selectedStand.get(player.getUniqueId());
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuUp)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX() + 0.1,stand.getLocation().getY(),stand.getLocation().getZ(),stand.getLocation().getYaw(),stand.getLocation().getPitch()));
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuDown)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX() - 0.1,stand.getLocation().getY(),stand.getLocation().getZ(),stand.getLocation().getYaw(),stand.getLocation().getPitch()));
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuLeft)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX(),stand.getLocation().getY(),stand.getLocation().getZ() - 0.1,stand.getLocation().getYaw(),stand.getLocation().getPitch()));
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuRight)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX(),stand.getLocation().getY(),stand.getLocation().getZ() + 0.1,stand.getLocation().getYaw(),stand.getLocation().getPitch()));
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuWhiteUp)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX(),stand.getLocation().getY() + 0.1,stand.getLocation().getZ(),stand.getLocation().getYaw(),stand.getLocation().getPitch()));
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuWhiteDown)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX(),stand.getLocation().getY() - 0.1,stand.getLocation().getZ(),stand.getLocation().getYaw(),stand.getLocation().getPitch()));
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuRotateLeft)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX(),stand.getLocation().getY(),stand.getLocation().getZ(),stand.getLocation().getYaw() - 10,stand.getLocation().getPitch()));
                    player.sendMessage(ChatColor.YELLOW + String.valueOf(stand.getLocation().getYaw()) + "°");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuRotateRight)) {
                    stand.teleport(new Location(stand.getWorld(),stand.getLocation().getX(),stand.getLocation().getY(),stand.getLocation().getZ(),stand.getLocation().getYaw() + 10,stand.getLocation().getPitch()));
                    player.sendMessage(ChatColor.YELLOW + String.valueOf(stand.getLocation().getYaw()) + "°");
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuRotateRightSide)) {
                    if (stand.getEquipment().getItemInMainHand().getItemMeta() != null) {
                        stand.setRightArmPose(new EulerAngle(stand.getRightArmPose().getX(),stand.getRightArmPose().getY(),Math.toRadians(Math.toDegrees(stand.getRightArmPose().getZ()) - 4)));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getRightArmPose().getZ())) + "°");
                    } else {
                        stand.setHeadPose(new EulerAngle(stand.getHeadPose().getX(),stand.getHeadPose().getY(),Math.toRadians(Math.toDegrees(stand.getHeadPose().getZ()) - 4)));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getHeadPose().getZ())) + "°");
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuRotateLeftSide)) {
                    if (stand.getEquipment().getItemInMainHand().getItemMeta() != null) {
                        stand.setRightArmPose(new EulerAngle(stand.getRightArmPose().getX(),stand.getRightArmPose().getY(),Math.toRadians(Math.toDegrees(stand.getRightArmPose().getZ()) + 4)));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getRightArmPose().getZ())) + "°");
                    } else {
                        stand.setHeadPose(new EulerAngle(stand.getHeadPose().getX(),stand.getHeadPose().getY(),Math.toRadians(Math.toDegrees(stand.getHeadPose().getZ()) + 4)));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getHeadPose().getZ())) + "°");
                    }

                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuRotateFront)) {
                    if (stand.getEquipment().getItemInMainHand().getItemMeta() != null) {
                        stand.setRightArmPose(new EulerAngle(Math.toRadians(Math.toDegrees(stand.getRightArmPose().getX()) + 4),stand.getRightArmPose().getY(),stand.getRightArmPose().getZ()));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getRightArmPose().getX())) + "°");
                    } else {
                        stand.setHeadPose(new EulerAngle(Math.toRadians(Math.toDegrees(stand.getHeadPose().getX()) + 4),stand.getHeadPose().getY(),stand.getHeadPose().getZ()));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getHeadPose().getX())) + "°");
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuRotateBack)) {
                    if (stand.getEquipment().getItemInMainHand().getItemMeta() != null) {
                        stand.setRightArmPose(new EulerAngle(Math.toRadians(Math.toDegrees(stand.getRightArmPose().getX()) - 4),stand.getRightArmPose().getY(),stand.getRightArmPose().getZ()));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getRightArmPose().getX())) + "°");
                    } else {
                        stand.setHeadPose(new EulerAngle(Math.toRadians(Math.toDegrees(stand.getHeadPose().getX()) - 4),stand.getHeadPose().getY(),stand.getHeadPose().getZ()));
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(Math.toDegrees(stand.getHeadPose().getX())) + "°");
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.MoveMenuExit)) {
                    player.closeInventory();
                }
            }
            e.setCancelled(true);
        }
    }
}
