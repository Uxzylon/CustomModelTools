package com.uxzylon.model3dplacer.Events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import static com.uxzylon.model3dplacer.Commands.SubCommand.selectedStand;
import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;

public class moveMenuClick implements Listener {
    @EventHandler
    public void onEvent(InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase(Texts.MoveMenu.getText())) {

            if (e.getCurrentItem() == null) {
                return;
            }

            Player player = (Player) e.getWhoClicked();
            ArmorStand stand = selectedStand.get(player.getUniqueId());

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

            if (displayName.equalsIgnoreCase(Texts.MoveMenuUp.getText())) {
                standLocation.setX(standLocation.getX() + 0.1);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuDown.getText())) {
                standLocation.setX(standLocation.getX() - 0.1);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuLeft.getText())) {
                standLocation.setZ(standLocation.getZ() - 0.1);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuRight.getText())) {
                standLocation.setZ(standLocation.getZ() + 0.1);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuWhiteUp.getText())) {
                standLocation.setY(standLocation.getY() + 0.1);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuWhiteDown.getText())) {
                standLocation.setY(standLocation.getY() - 0.1);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuRotateLeft.getText())) {
                standLocation.setYaw(standLocation.getYaw() - 10);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuRotateRight.getText())) {
                standLocation.setYaw(standLocation.getYaw() + 10);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuRotateRightSide.getText())) {
                pose = pose.subtract(0, 0, 0.02);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuRotateLeftSide.getText())) {
                pose = pose.add(0, 0, 0.02);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuRotateFront.getText())) {
                pose = pose.add(0.02, 0, 0);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuRotateBack.getText())) {
                pose = pose.subtract(0.02, 0, 0);
            } else if (displayName.equalsIgnoreCase(Texts.MoveMenuExit.getText())) {
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
