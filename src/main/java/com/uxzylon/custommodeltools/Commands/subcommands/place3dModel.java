package com.uxzylon.custommodeltools.Commands.subcommands;

import com.uxzylon.custommodeltools.Commands.SubCommand;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.List;

import static com.uxzylon.custommodeltools.CustomModelTools.Texts;
import static com.uxzylon.custommodeltools.ResourcePack.customModelDatas;

public class place3dModel extends SubCommand {

    @Override
    public String getName() {
        return "place";
    }

    @Override
    public String getDescription() {
        return Texts.placeDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d place <category> <model>";
    }

    @Override
    public String permission() {
        return "custommodeltools.command.place";
    }

    @Override
    public boolean canRunConsole() {
        return false;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return getArgsCategoryModel(args);
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 2 && args[1] != null && args[2] != null) {

            ItemStack item = getItemFromCategoryModel(args[1], args[2]);
            if (item == null) {
                player.sendMessage(String.format(Texts.notFoundModel.getText(), getSyntax()));
                return;
            }

            double locationX = player.getLocation().getBlockX();
            int locationY = player.getLocation().getBlockY();
            double locationZ = player.getLocation().getBlockZ();
            int yaw;
            switch (yawToFace(player.getLocation().getYaw())) {
                case NORTH:
                    locationX = locationX + 0.125;
                    locationY = locationY - 1;
                    locationZ = locationZ + 1.125;
                    yaw = 180;
                    break;
                case EAST:
                    locationX = locationX - 0.125;
                    locationY = locationY - 1;
                    locationZ = locationZ + 0.125;
                    yaw = -90;
                    break;
                case WEST:
                    locationX = locationX + 1.125;
                    locationY = locationY - 1;
                    locationZ = locationZ + 0.875;
                    yaw = 90;
                    break;
                default:
                    locationX = locationX + 0.875;
                    locationY = locationY - 1;
                    locationZ = locationZ - 0.125;
                    yaw = 0;
                    break;
            }
            Location location = new Location(player.getWorld(),locationX,locationY,locationZ,yaw,0);

            ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            stand.setArms(true);
            stand.setBasePlate(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setPersistent(true);
            stand.setVisible(false);
            stand.setRightArmPose(new EulerAngle(Math.toRadians(270),0,0));

            EntityEquipment standEquip = stand.getEquipment();
            if (standEquip == null) {
                return;
            }

            standEquip.setItemInMainHand(item);

            setSlotsDisabled(stand, true);

            Pair<Material, Integer> material = customModelDatas.get(args[1]).get(args[2]);
            player.sendMessage(String.format(Texts.modelMessage.getText(), args[2], args[1], material.getLeft(), material.getRight()) + Texts.placed.getText());
        } else {
            player.sendMessage(String.format(Texts.wrongSyntax.getText(), getSyntax()));
        }
    }
}
