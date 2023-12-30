package com.uxzylon.model3dplacer.Events;

import com.uxzylon.model3dplacer.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

public class moveMenuClick implements Listener {
    @EventHandler
    public void onEvent(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(Utils.MoveMenu)) {

            if (e.getCurrentItem() == null) {
                return;
            }

            Player player = (Player) e.getWhoClicked();
            ArmorStand stand = Utils.selectedStand.get(player.getUniqueId());

            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            if (itemMeta == null) {
                return;
            }

            String displayName = itemMeta.getDisplayName();

            EntityEquipment equipment = stand.getEquipment();
            if (equipment == null) {
                return;
            }

            boolean isMainHand = equipment.getItemInMainHand().getItemMeta() != null;

            Location standLocation = stand.getLocation();
            EulerAngle armPose = stand.getRightArmPose();
            EulerAngle headPose = stand.getHeadPose();
            EulerAngle pose = isMainHand ? armPose : headPose;

            if (displayName.equalsIgnoreCase(Utils.MoveMenuUp)) {
                standLocation.setX(standLocation.getX() + 0.1);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuDown)) {
                standLocation.setX(standLocation.getX() - 0.1);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuLeft)) {
                standLocation.setZ(standLocation.getZ() - 0.1);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuRight)) {
                standLocation.setZ(standLocation.getZ() + 0.1);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuWhiteUp)) {
                standLocation.setY(standLocation.getY() + 0.1);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuWhiteDown)) {
                standLocation.setY(standLocation.getY() - 0.1);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuRotateLeft)) {
                standLocation.setYaw(standLocation.getYaw() - 10);
                player.sendMessage(ChatColor.YELLOW + String.valueOf(stand.getLocation().getYaw()) + "°");
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuRotateRight)) {
                standLocation.setYaw(standLocation.getYaw() + 10);
                player.sendMessage(ChatColor.YELLOW + String.valueOf(stand.getLocation().getYaw()) + "°");
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuRotateRightSide)) {
                pose = pose.subtract(0, 0, 0.02);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuRotateLeftSide)) {
                pose = pose.add(0, 0, 0.02);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuRotateFront)) {
                pose = pose.add(0.02, 0, 0);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuRotateBack)) {
                pose = pose.subtract(0.02, 0, 0);
            } else if (displayName.equalsIgnoreCase(Utils.MoveMenuExit)) {
                player.closeInventory();
                return;
            }

            player.sendMessage(
                ChatColor.YELLOW + "X: " +
                    ChatColor.RED + String.format("%.2f", standLocation.getX()) +
                    ChatColor.YELLOW + " Y: " +
                    ChatColor.RED + String.format("%.2f", standLocation.getY()) +
                    ChatColor.YELLOW + " Z: " +
                    ChatColor.RED + String.format("%.2f", standLocation.getZ()) +
                    ChatColor.YELLOW + " Yaw: " +
                    ChatColor.RED + String.format("%.2f", standLocation.getYaw()) +
                    ChatColor.YELLOW + " Angle X: " +
                    ChatColor.RED + String.format("%.2f", Math.toDegrees(pose.getX())) +
                    ChatColor.YELLOW + " Angle Z: " +
                    ChatColor.RED + String.format("%.2f", Math.toDegrees(pose.getZ()))
            );

            stand.teleport(standLocation);

            if (isMainHand) {
                stand.setRightArmPose(pose);
            } else {
                stand.setHeadPose(pose);
            }

            e.setCancelled(true);
        }
    }
}
