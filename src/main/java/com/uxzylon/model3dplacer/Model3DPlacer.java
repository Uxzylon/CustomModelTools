package com.uxzylon.model3dplacer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uxzylon.model3dplacer.Commands.*;
import com.uxzylon.model3dplacer.Events.*;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

public final class Model3DPlacer extends JavaPlugin {

    public static Model3DPlacer plugin;
    public static HashMap<String, HashMap<String, Pair<Material, Integer>>> customModelDatas = new HashMap<>();
    public static HashMap<UUID, ArmorStand> selectedStand = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        createConfig();
        parseResourcePack();

        getCommand("model3d").setExecutor(new model3dCommand());
        getServer().getPluginManager().registerEvents(new moveMenuClick(), plugin);

        plugin.getLogger().info("Enabled!");
    }

    private void createConfig() {
        getConfig().options().copyDefaults(true);

        getConfig().addDefault("ResourcePack.url", "https://plopsainmc.anthony-jeanney.fr/Plopsacraft-Pack-df25ba3fed7ebc1709ce18db5fc09c96700da254.zip");
        getConfig().addDefault("ResourcePack.hash", "df25ba3fed7ebc1709ce18db5fc09c96700da254");

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
        addText("placed", "§6Modèle §a%s §6de §a%s §6(§a%s§6,§a%d§6) placé !");
        addText("given", "§6Modèle §a%s §6de §a%s §6(§a%s§6,§a%d§6) donné !");
        addText("noCustomModelDataFound", "§cAucun Armor Stand avec CustomModelData trouvé !");
        addText("notFoundModel", "§cModèle non trouvé !");
        addText("wrongSyntax", "§cMauvaise syntaxe ! §d%s");

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
        given,
        noCustomModelDataFound,
        notFoundModel,
        wrongSyntax,
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

    private void parseResourcePack() {
        String url = getConfig().getString("ResourcePack.url");
        if (url == null) {
            return;
        }

        String fileName = "resourcePack.zip";
        boolean needDownload = true;

        plugin.getLogger().info("Initializing the resource pack file...");
        File resourcePackFile = new File(getDataFolder(), fileName);
        if (!resourcePackFile.exists()) {
            resourcePackFile.getParentFile().mkdirs();
            try {
                resourcePackFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to create the resource pack file!");
                return;
            }
        } else {
            // Check the hash
            if (verifyHash(resourcePackFile)) {
                plugin.getLogger().info("Resource pack hash verified!");
                needDownload = false;
            } else {
                plugin.getLogger().warning("Resource pack hash verification failed!");
            }
        }

        if (needDownload) {
            // Download the file
            plugin.getLogger().info("Downloading the resource pack...");
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(resourcePackFile)) {

                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }

                if (!verifyHash(resourcePackFile)) {
                    plugin.getLogger().warning("Failed to verify the resource pack hash!");
                    return;
                }
                plugin.getLogger().info("Resource pack downloaded!");

            } catch (IOException e) {
                plugin.getLogger().warning("Failed to download the resource pack!");
                return;
            }
        }

        String targetDir = "assets/minecraft/models/item/";

        // Unzip the file and parse the JSON files
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(resourcePackFile))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().startsWith(targetDir) && zipEntry.getName().endsWith(".json")) {
                    // Parse the JSON file
                    JsonElement fileElement = JsonParser.parseReader(new InputStreamReader(zis));
                    JsonObject fileObject = fileElement.getAsJsonObject();
                    JsonArray overrides = fileObject.getAsJsonArray("overrides");

                    // plugin.getLogger().info("Parsing " + zipEntry.getName());

                    Material material = Material.getMaterial(zipEntry.getName().substring(targetDir.length(), zipEntry.getName().length() - 5).toUpperCase());
                    if (material == null) {
                        plugin.getLogger().warning("Failed to get the material for " + zipEntry.getName());
                        continue;
                    }

                    for (JsonElement override : overrides) {
                        JsonObject overrideObject = override.getAsJsonObject();
                        JsonObject predicate = overrideObject.getAsJsonObject("predicate");
                        String model = overrideObject.get("model").getAsString();

                        int customModelData = predicate.get("custom_model_data").getAsInt();
                        String[] modelParts = model.split("/");
                        String category = modelParts.length > 1 ? modelParts[0] : "None";
                        String modelName = modelParts.length > 1 ? modelParts[1] : modelParts[0];

                        // If there is a ":" in the category, ignore the string before it
                        if (category.contains(":")) {
                            category = category.split(":")[1];
                        }

                        // Add to the HashMap
                        HashMap<String, Pair<Material, Integer>> categoryMap = customModelDatas.getOrDefault(category, new HashMap<>());
                        categoryMap.put(modelName, Pair.of(material, customModelData));
                        customModelDatas.put(category, categoryMap);
                    }
                }
            }

            plugin.getLogger().info("Resource pack parsed!");

            if (!customModelDatas.isEmpty()) {
                StringBuilder categories = new StringBuilder();
                customModelDatas.forEach((category, modelMap) -> {
                    categories.append(category).append(" (").append(modelMap.size()).append("), ");
                });
                plugin.getLogger().info(
                        "Found " + customModelDatas.size() + " categories with " +
                                customModelDatas.values().stream().mapToInt(HashMap::size).sum() + " models -> " +
                                categories.substring(0, categories.length() - 2)
                );
            }

        } catch (IOException e) {
            plugin.getLogger().warning("Failed to parse the resource pack!");
        }
    }

    private boolean verifyHash(File file) {
        try {
            String hash = sha1Hex(new FileInputStream(file));
            if (hash.equals(getConfig().getString("ResourcePack.hash"))) {
                return true;
            }
        } catch (IOException ignored) {}
        return false;
    }
}
