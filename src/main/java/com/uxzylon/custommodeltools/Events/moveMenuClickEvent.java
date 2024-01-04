package com.uxzylon.custommodeltools.Events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import static com.uxzylon.custommodeltools.Commands.SubCommand.*;
import static com.uxzylon.custommodeltools.CustomModelTools.*;
import static com.uxzylon.custommodeltools.ResourcePack.*;

public class moveMenuClickEvent implements Listener {
    @EventHandler
    public void onEvent(InventoryClickEvent e) {
        String title = e.getView().getTitle();

        if (title.equalsIgnoreCase(Texts.MoveMenu.getText())) {

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

        } else if (title.startsWith("Categories") || title.startsWith("Models")) {

            if (e.getCurrentItem() == null) {
                return;
            }

            Player player = (Player) e.getWhoClicked();
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            if (itemMeta == null) {
                return;
            }

            boolean isCategory = title.startsWith("Categories");

            // get page number (after space and before slash)
            int indexLocation = isCategory ? 1 : 2;
            int pageIndex = Integer.parseInt(title.split(" ")[indexLocation].split("/")[0]) - 1;
            int nbPages = Integer.parseInt(title.split(" ")[indexLocation].split("/")[1]) - 1;
            int previousPage = pageIndex - 1;
            int nextPage = pageIndex + 1;
            if (previousPage < 0) {
                previousPage = nbPages;
            }
            if (nextPage > nbPages) {
                nextPage = 0;
            }

            String category = isCategory ? itemMeta.getDisplayName() : title.split(" ")[1];

            if (e.getSlot() == 49) { // exit
                player.closeInventory();
                if (!isCategory) {
                    player.openInventory(guisCategories.get(0));
                }
                return;
            } else if (e.getSlot() == 45) {  // previous page
                if (isCategory) {
                    player.openInventory(guisCategories.get(previousPage));
                } else {
                    player.openInventory(guisModels.get(category).get(previousPage));
                }
                return;
            } else if (e.getSlot() == 53) {  // next page
                if (isCategory) {
                    player.openInventory(guisCategories.get(nextPage));
                } else {
                    player.openInventory(guisModels.get(category).get(nextPage));
                }
                return;
            }

            String itemName = itemMeta.getDisplayName();

            if (isCategory) {
                player.openInventory(guisModels.get(itemName).get(0));
            } else {
                // do something with the model
                ItemStack item = resourcePack.getItemFromCategoryModel(category, itemName);
                if (choosenPlayerFunction.get(player.getUniqueId()) == PlayerFunction.PLACE) {
                    placeModel(player, category, itemName, item);
                } else {
                    giveModel(player, category, itemName, item);
                }

                player.closeInventory();
            }
        }
    }
}
