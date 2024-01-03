package com.uxzylon.custommodeltools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.uxzylon.custommodeltools.Commands.SubCommand.getCustomSkull;
import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static com.uxzylon.custommodeltools.CustomModelTools.plugin;

public class ResourcePack {
    private String url;
    private String hash;
    public static HashMap<String, HashMap<String, Pair<Material, Integer>>> customModelDatas = new HashMap<>();
    public static List<Inventory> guisCategories = new ArrayList<>();
    public static HashMap<String, List<Inventory>> guisModels = new HashMap<>();

    public ResourcePack() {
        updatePack();
    }

    public void updatePack() {
        this.url = plugin.getConfig().getString("ResourcePack.url");
        this.hash = plugin.getConfig().getString("ResourcePack.hash");

        parseResourcePack();

        makeGuis();
    }

    public void setResourcePack(Player player) {
        player.setResourcePack(url, getHashHex());
    }

    private void parseResourcePack() {
        if (this.url == null) {
            return;
        }

        customModelDatas.clear();

        String fileName = "resourcePack.zip";
        boolean needDownload = true;

        plugin.getLogger().info("Initializing the resource pack file...");
        File resourcePackFile = new File(plugin.getDataFolder(), fileName);
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
                customModelDatas.forEach((category, modelMap) -> categories.append(category).append(" (").append(modelMap.size()).append("), "));
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
            if (hash.equals(this.hash)) {
                return true;
            }
        } catch (IOException ignored) {}
        return false;
    }

    public byte[] getHashHex() {
        return DatatypeConverter.parseHexBinary(hash);
    }

    private List<Inventory> makeInventory(Material material, List<String> models) {
        int guiSize = 54;
        int guiSizeWithoutArrowsLine = 45;
        ItemStack arrowLeft = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=");
        ItemStack arrowRight = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19");

        List<Inventory> guisModels = new ArrayList<>();
        int nbrGuis = 0;
        int nbrPages = models.size() % guiSizeWithoutArrowsLine == 0 ? models.size() / guiSizeWithoutArrowsLine : models.size() / guiSizeWithoutArrowsLine + 1;
        Inventory currentGui = Bukkit.createInventory(null, guiSize, "Categories " + nbrGuis + "/" + nbrPages);
        for (int i = 0; i < models.size(); i++) {
            if (i % guiSizeWithoutArrowsLine == 0) {
                nbrGuis++;
                currentGui = Bukkit.createInventory(null, guiSize, "Categories " + nbrGuis + "/" + nbrPages);
                if (nbrGuis > 1 || nbrGuis < nbrPages) {
                    currentGui.setItem(45, arrowLeft);
                }
                if (nbrGuis < nbrPages) {
                    currentGui.setItem(53, arrowRight);
                }
                guisModels.add(currentGui);
            }
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return null;
            }
            meta.setDisplayName(models.get(i));
            item.setItemMeta(meta);
            currentGui.addItem(item);
        }

        return guisModels;
    }

    private void makeGuis() {
        guisCategories.clear();
        guisModels.clear();
        guisCategories = makeInventory(Material.PAPER, new ArrayList<>(customModelDatas.keySet()));
        customModelDatas.forEach((category, modelMap) -> guisModels.put(
                category, makeInventory(customModelDatas.get(category).values().iterator().next().getLeft(), new ArrayList<>(modelMap.keySet()))));
    }
}
