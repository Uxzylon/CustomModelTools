package com.uxzylon.model3dplacer.Events;

import com.uxzylon.model3dplacer.ResourcePack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import static com.uxzylon.model3dplacer.Model3DPlacer.plugin;
import static com.uxzylon.model3dplacer.Model3DPlacer.resourcePack;

public class resourcePackEvent implements Listener {

    @EventHandler
    public void ResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        String declined = ChatColor.RED + "Vous devez accepter le resource pack!";
        String error = ChatColor.RED + "Erreur de t" + '\u00E9' + "l" + '\u00E9' + "chargement!";
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
