package com.uxzylon.model3dplacer.Commands.subcommands;

import com.uxzylon.model3dplacer.Commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

import static com.uxzylon.model3dplacer.Model3DPlacer.Texts;
import static com.uxzylon.model3dplacer.Model3DPlacer.plugin;
import static com.uxzylon.model3dplacer.Model3DPlacer.resourcePack;

public class reload extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return Texts.reloadDescription.getText();
    }

    @Override
    public String getSyntax() {
        return "/model3d reload";
    }

    @Override
    public String permission() {
        return "model3dplacer.command.reload";
    }

    @Override
    public boolean canRunConsole() {
        return true;
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void perform(Player player, String[] args) {
        plugin.reloadConfig();

        resourcePack.updatePack();
        for (Player p : Bukkit.getOnlinePlayers()) {
            resourcePack.setResourcePack(p);
            p.sendMessage(Texts.reloadedPack.getText());
        }
        plugin.getLogger().info(Texts.reloadedPack.getText());
    }
}
