package com.uxzylon.custommodeltools.Commands.subcommands;

import com.uxzylon.custommodeltools.Commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static com.uxzylon.custommodeltools.CustomModelTools.Texts;
import static com.uxzylon.custommodeltools.CustomModelTools.selectedStand;
import static com.uxzylon.custommodeltools.Utils.*;

public class move3dModel extends SubCommand {

    @Override
    public String getName() {
        return "move";
    }

    @Override
    public String getDescription() {
        return Texts.moveDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d move";
    }

    @Override
    public String permission() {
        return "custommodeltools.command.move";
    }

    @Override
    public boolean canRunConsole() {
        return false;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void perform(Player player, String[] args) {
        ArmorStand stand = selectedStand.get(player.getUniqueId());
        if (stand != null) {
            Inventory gui = Bukkit.createInventory(player,27, Texts.MoveMenu.getText());
            ItemStack arrowUp = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=");
            ItemStack arrowDown = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=");
            ItemStack arrowLeft = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=");
            ItemStack arrowRight = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19");
            ItemStack arrowWhiteUp = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWFkNmM4MWY4OTlhNzg1ZWNmMjZiZTFkYzQ4ZWFlMmJjZmU3NzdhODYyMzkwZjU3ODVlOTViZDgzYmQxNGQifX19");
            ItemStack arrowWhiteDown = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODgyZmFmOWE1ODRjNGQ2NzZkNzMwYjIzZjg5NDJiYjk5N2ZhM2RhZDQ2ZDRmNjVlMjg4YzM5ZWI0NzFjZTcifX19");
            ItemStack rotateLeft = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFkMDgwN2FjNzk1MmM0MjUwYWI1OWZiYTgyNjQ2MjIyYTFhMmNiNmQ2NDNlNGE1NjZhZTc1ZDg2MzI1NGRiIn19fQ==");
            ItemStack rotateRight = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNjNmZkNTczMjhmM2ExMWUyYjliYzg5Y2MzMmJhZDcxY2FmZGUxOGZkMjcxYzE3NTA0YjI3ZjZlNTFjIn19fQ==");
            ItemStack rotateLeftSide = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmU2ZTgyZDEyYTVmN2JiZTM0ZDliN2JmNjMyZTg4YTUwMzcxYTJhYzM5YzI2YmVmMDVmNzJjZGQ3NWIxODViIn19fQ==");
            ItemStack rotateRightSide = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg0MjVkMzU0MjM1YzEzMzRkN2QwOWJiYmYxOGRkOTY1ZTFjMGMyMzg2OTViMGIyOTAyM2U3ZGNiNWIzIn19fQ==");
            ItemStack rotateFront = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA1NDNiNmNhMTdkMmU0ZDEzMmU0M2U3N2M5Mjc2NmNmYmM2MWY4OWI3MmQ4ZmI4NTNmOTAyYWUxZGQ3YjAifX19");
            ItemStack rotateBack = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdiMTc5MWJkYzQ2ZDhhNWM1MTcyOWU4OTgyZmQ0MzliYjQwNTEzZjY0YjViYWJlZTkzMjk0ZWZjMWM3In19fQ==");
            ItemStack exit = new ItemStack(Material.BARRIER);

            setDisplayName(arrowUp, Texts.MoveMenuUp.getText());
            setDisplayName(arrowDown, Texts.MoveMenuDown.getText());
            setDisplayName(arrowLeft, Texts.MoveMenuLeft.getText());
            setDisplayName(arrowRight, Texts.MoveMenuRight.getText());
            setDisplayName(arrowWhiteUp, Texts.MoveMenuWhiteUp.getText());
            setDisplayName(arrowWhiteDown, Texts.MoveMenuWhiteDown.getText());
            setDisplayName(rotateRight, Texts.MoveMenuRotateRight.getText());
            setDisplayName(rotateLeft, Texts.MoveMenuRotateLeft.getText());
            setDisplayName(rotateRightSide, Texts.MoveMenuRotateRightSide.getText());
            setDisplayName(rotateLeftSide, Texts.MoveMenuRotateLeftSide.getText());
            setDisplayName(rotateFront, Texts.MoveMenuRotateFront.getText());
            setDisplayName(rotateBack, Texts.MoveMenuRotateBack.getText());
            setDisplayName(exit, Texts.MoveMenuExit.getText());

            gui.setItem(1, arrowUp);
            gui.setItem(9, arrowLeft);
            gui.setItem(11, arrowRight);
            gui.setItem(19, arrowDown);
            gui.setItem(3, arrowWhiteUp);
            gui.setItem(21, arrowWhiteDown);
            gui.setItem(13, rotateLeft);
            gui.setItem(14, rotateRight);
            gui.setItem(7, rotateLeftSide);
            gui.setItem(8, rotateRightSide);
            gui.setItem(16, rotateFront);
            gui.setItem(25, rotateBack);
            gui.setItem(26, exit);
            player.openInventory(gui);
        } else {
            player.sendMessage(Texts.noSelection.getText());
        }
    }
}
