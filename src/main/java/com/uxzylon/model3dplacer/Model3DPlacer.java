package com.uxzylon.model3dplacer;

import com.uxzylon.model3dplacer.Commands.*;
import com.uxzylon.model3dplacer.Events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Model3DPlacer extends JavaPlugin {

    public static Model3DPlacer plugin;

    @Override
    public void onEnable() {
        plugin = this;

        createConfig();

        getCommand("model3d").setExecutor(new model3dCommand());
        getServer().getPluginManager().registerEvents(new moveMenuClick(), plugin);

        plugin.getLogger().info("Enabled!");
    }

    private void createConfig() {
        getConfig().options().copyDefaults(true);

        addText("Title", "§6============ §aModel3DPlacer §6============");

        addText("giveDescription", "Obtenez un CustomModelData dans votre inventaire");
        addText("moveDescription", "Déplacez votre selection");
        addText("placeDescription", "Place un CustomModelData");
        addText("removeDescription", "Supprime votre selection");
        addText("selectDescription", "Sélectionne le CustomModelData le plus proche");
        addText("switchDescription", "Alterne entre Slot Main et Slot Tête");
        addText("unselectDescription", "Désélectionne votre selection");

        addText("noSelection", "§6Vous n'avez pas de selection ! §d/model3d select");
        addText("noPermission", "§cVous n'avez pas la permission !");
        addText("playerOnly", "Player Only!");
        addText("needId", "§cVous devez fournir un Id CustomModelData");
        addText("unselected", "§6Armor Stand avec CustomModelData §a%s §6désélectionné !");
        addText("selected", "§6Armor Stand avec CustomModelData §a%s §6sélectionné !");
        addText("removed", "§6Armor Stand avec CustomModelData §a%s §6supprimé !");
        addText("placed", "§6Armor Stand avec CustomModelData §a%s §6placé !");
        addText("noCustomModelDataFound", "§cAucun Armor Stand avec CustomModelData trouvé !");

        addText("MoveMenu", "§cMenu déplacement §2Par Uxzylon");
        addText("MoveMenuUp", "§aAVANT");
        addText("MoveMenuDown", "§aARRIERE");
        addText("MoveMenuLeft", "§aGAUCHE");
        addText("MoveMenuRight", "§aDROITE");
        addText("MoveMenuWhiteUp", "§6HAUT");
        addText("MoveMenuWhiteDown", "§6BAS");
        addText("MoveMenuRotateLeft", "§3ROTATION GAUCHE");
        addText("MoveMenuRotateRight", "§3ROTATION DROITE");
        addText("MoveMenuRotateRightSide", "§9ROTATION COTE DROIT");
        addText("MoveMenuRotateLeftSide", "§9ROTATION COTE GAUCHE");
        addText("MoveMenuRotateFront", "§dROTATION AVANT");
        addText("MoveMenuRotateBack", "§dROTATION ARRIERE");
        addText("MoveMenuExit", "§cEXIT");

        saveConfig();
        reloadConfig();
    }

    private void addText(String path, String text) {
        getConfig().addDefault("Texts." + path, text);
    }

    public enum Texts {
        Title,
        giveDescription,
        moveDescription,
        placeDescription,
        removeDescription,
        selectDescription,
        switchDescription,
        unselectDescription,
        noSelection,
        noPermission,
        playerOnly,
        needId,
        unselected,
        selected,
        removed,
        placed,
        noCustomModelDataFound,
        MoveMenu,
        MoveMenuUp,
        MoveMenuDown,
        MoveMenuLeft,
        MoveMenuRight,
        MoveMenuWhiteUp,
        MoveMenuWhiteDown,
        MoveMenuRotateLeft,
        MoveMenuRotateRight,
        MoveMenuRotateRightSide,
        MoveMenuRotateLeftSide,
        MoveMenuRotateFront,
        MoveMenuRotateBack,
        MoveMenuExit;

        private final String text;

        Texts() {
            this.text = name();
        }

        public String getText() {
            return plugin.getConfig().getString("Texts." + text);
        }
    }
}
