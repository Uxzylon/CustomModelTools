package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import java.util.Collections;
import java.util.List;

import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;

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
        return "/model3d place <id>";
    }

    @Override
    public String permission() {
        return "model3dplacer.command.place";
    }

    @Override
    public boolean canRunConsole() {
        return false;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2) {
            return Collections.singletonList("<id>");
        }
        return Collections.emptyList();
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1 && isInt(args[1])) {
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
            assert standEquip != null;

            ItemStack item = new ItemStack(Material.CLOCK);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setCustomModelData(Integer.valueOf(args[1]));
            item.setItemMeta(meta);

            standEquip.setItemInMainHand(item);


            setSlotsDisabled(stand, true);


            player.sendMessage(String.format(Texts.placed.getText(), args[1]));
        } else {
            player.sendMessage(Texts.needId.getText());
        }
    }
}
