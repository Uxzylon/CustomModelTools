package com.uxzylon.model3dplacer;

import com.uxzylon.model3dplacer.Commands.*;
import com.uxzylon.model3dplacer.Events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Model3DPlacer extends JavaPlugin {

    public static Model3DPlacer plugin;

    @Override
    public void onEnable() {
        plugin = this;
        this.getLogger().info("Enabled!");

        getCommand("model3d").setExecutor(new model3dCommand());
        getServer().getPluginManager().registerEvents(new moveMenuClick(), plugin);
    }
}
