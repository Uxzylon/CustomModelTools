package com.uxzylon.custommodeltools;

import com.uxzylon.custommodeltools.Commands.*;
import com.uxzylon.custommodeltools.Events.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class CustomModelTools extends JavaPlugin {

    public static CustomModelTools plugin;
    public static HashMap<UUID, ArmorStand> selectedStand = new HashMap<>();
    public static ResourcePack resourcePack;

    @Override
    public void onEnable() {
        plugin = this;

        createConfig();

        resourcePack = new ResourcePack();

        getCommand("model3d").setExecutor(new model3dCommand());

        getServer().getPluginManager().registerEvents(new moveMenuClickEvent(), plugin);
        getServer().getPluginManager().registerEvents(new resourcePackEvent(), plugin);

        plugin.getLogger().info("Enabled!");
    }

    private void createConfig() {
        getConfig().options().copyDefaults(true);

        getConfig().addDefault("ResourcePack.url", "https://plopsainmc.anthony-jeanney.fr/Plopsacraft-Pack-0adac8c94ad1db17cf01f85fbfee78c2b4517e01.zip");
        getConfig().addDefault("ResourcePack.hash", "0adac8c94ad1db17cf01f85fbfee78c2b4517e01");
        getConfig().addDefault("ResourcePack.kickOnFail", true);

        addText("Title", "§6============ §aCustomModelTools §6============");

        addText("giveDescription", "Obtenez un CustomModelData dans votre inventaire");
        addText("moveDescription", "Déplacez votre selection");
        addText("placeDescription", "Place un CustomModelData");
        addText("removeDescription", "Supprime votre selection");
        addText("selectDescription", "Sélectionne le CustomModelData le plus proche");
        addText("switchDescription", "Alterne entre Slot Main et Slot Tête");
        addText("unselectDescription", "Désélectionne votre selection");
        addText("reloadDescription", "Recharge le plugin");

        addText("modelMessage", "§6Modèle §a%s §6de §a%s §6(§a%s§6,§a%d§6) ");
        addText("noSelection", "§6Vous n'avez pas de selection ! §d/model3d select");
        addText("noPermission", "§cVous n'avez pas la permission !");
        addText("playerOnly", "Player Only!");
        addText("unselected", "§6désélectionné !");
        addText("selected", "§6sélectionné !");
        addText("removed", "§6supprimé !");
        addText("placed", "§6placé !");
        addText("given", "§6donné !");
        addText("switched", "§6switched to §a%s§6 slot !");
        addText("noCustomModelDataFound", "§cAucun Armor Stand avec CustomModelData trouvé !");
        addText("notFoundModel", "§cModèle non trouvé !");
        addText("wrongSyntax", "§cMauvaise syntaxe ! §d%s");

        addText("reloadedPack", "§6Resource Pack rechargé !");
        addText("needToAcceptPack", "§cVous devez accepter le Resource Pack!");
        addText("downloadErrorPack", "§cErreur lors du téléchargement du Resource Pack!");

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
        Title, giveDescription, moveDescription, placeDescription, removeDescription, selectDescription,
        switchDescription, unselectDescription, reloadDescription, modelMessage, noSelection, noPermission, playerOnly, unselected,
        selected, removed, placed, given, switched, noCustomModelDataFound, notFoundModel, wrongSyntax, reloadedPack,
        needToAcceptPack, downloadErrorPack, MoveMenu, MoveMenuUp, MoveMenuDown, MoveMenuLeft, MoveMenuRight,
        MoveMenuWhiteUp, MoveMenuWhiteDown, MoveMenuRotateLeft, MoveMenuRotateRight, MoveMenuRotateRightSide,
        MoveMenuRotateLeftSide, MoveMenuRotateFront, MoveMenuRotateBack, MoveMenuExit;

        private final String text;

        Texts() {
            this.text = name();
        }

        public String getText() {
            return plugin.getConfig().getString("Texts." + text);
        }
    }
}
