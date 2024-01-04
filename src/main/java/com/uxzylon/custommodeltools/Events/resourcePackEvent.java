package com.uxzylon.custommodeltools.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import static com.uxzylon.custommodeltools.CustomModelTools.Texts;
import static com.uxzylon.custommodeltools.CustomModelTools.plugin;
import static com.uxzylon.custommodeltools.CustomModelTools.resourcePack;

public class resourcePackEvent implements Listener {

    @EventHandler
    public void ResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("custommodeltools.bypass.resourcepack")) return;

        String declined = Texts.needToAcceptPack.getText();
        String error =Texts.downloadErrorPack.getText();
        switch (event.getStatus()) {
            case DECLINED: {
                if (plugin.getConfig().getBoolean("ResourcePack.kickOnFail")) player.kickPlayer(declined);
                else player.sendMessage(declined);
                break;
            }

            case FAILED_DOWNLOAD: {
                Bukkit.getScheduler ().runTaskLater (plugin, () -> {
                    if (plugin.getConfig().getBoolean("ResourcePack.kickOnFail")) player.kickPlayer(error);
                    else player.sendMessage(error);
                }, 2);
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje) {
        Player player = pje.getPlayer();
        resourcePack.setResourcePack(player);
    }
}
