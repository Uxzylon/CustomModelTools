package com.uxzylon.custommodeltools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.uxzylon.custommodeltools.Commands.SubCommand.getCustomSkull;
import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static com.uxzylon.custommodeltools.CustomModelTools.plugin;
import static com.uxzylon.custommodeltools.Utils.*;

public class ResourcePack {
    private String url;
    private String hash;
    public static HashMap<String, HashMap<String, Pair<Material, Integer>>> customModelDatas = new HashMap<>();
    public static List<Inventory> guisCategories = new ArrayList<>();
    public static HashMap<String, List<Inventory>> guisModels = new HashMap<>();

    public enum PlayerFunction {
        PLACE,
        GIVE
    }

    public static HashMap<UUID, PlayerFunction> choosenPlayerFunction = new HashMap<>();

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

    public List<String> getArgsCategoryModel(String[] args) {
        if (args.length == 2) {
            return new ArrayList<>(customModelDatas.keySet());
        } else if (args.length == 3) {
            if (customModelDatas.containsKey(args[1])) {
                return new ArrayList<>(customModelDatas.get(args[1]).keySet());
            }
        }
        return Collections.emptyList();
    }

    public ItemStack getItemFromCategoryModel(String category, String model) {
        if (!customModelDatas.containsKey(category) || !customModelDatas.get(category).containsKey(model)) {
            return null;
        }

        Pair<Material, Integer> triple = customModelDatas.get(category).get(model);
        ItemStack item = new ItemStack(triple.getLeft());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        meta.setCustomModelData(triple.getRight());
        meta.setDisplayName(model);
        item.setItemMeta(meta);

        return item;
    }

    public Triple<String, String, String> getModelInfoFromCustomModelData(int customModelData) {
        for (Map.Entry<String, HashMap<String, Pair<Material, Integer>>> entry : customModelDatas.entrySet()) {
            for (Map.Entry<String, Pair<Material, Integer>> entry2 : entry.getValue().entrySet()) {
                if (entry2.getValue().getRight() == customModelData) {
                    return Triple.of(entry2.getKey(), entry.getKey(), entry2.getValue().getLeft().toString());
                }
            }
        }
        return null;
    }

    private List<Inventory> makeInventory(String title, HashMap<String, Pair<Material, Integer>> models) {
        int guiSize = 54;
        int guiSizeWithoutArrowsLine = 45;
        ItemStack arrowLeft = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=");
        setDisplayName(arrowLeft, "Previous page");
        ItemStack arrowRight = getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19");
        setDisplayName(arrowRight, "Next page");
        ItemStack exit = new ItemStack(Material.BARRIER);
        setDisplayName(exit, "Back");

        List<Inventory> guisModels = new ArrayList<>();
        int nbrGuis = 0;
        int nbrPages = models.keySet().size() % guiSizeWithoutArrowsLine == 0 ? models.keySet().size() / guiSizeWithoutArrowsLine : models.keySet().size() / guiSizeWithoutArrowsLine + 1;
        Inventory currentGui = Bukkit.createInventory(null, guiSize, title + " " + nbrGuis + "/" + nbrPages);
        for (int i = 0; i < models.keySet().size(); i++) {
            if (i % guiSizeWithoutArrowsLine == 0) {
                nbrGuis++;
                currentGui = Bukkit.createInventory(null, guiSize, title + " " + nbrGuis + "/" + nbrPages);

                currentGui.setItem(49, exit);
                if (nbrPages > 1) {
                    currentGui.setItem(45, arrowLeft);
                    currentGui.setItem(53, arrowRight);
                }

                guisModels.add(currentGui);
            }

            String modelName = (String) models.keySet().toArray()[i];

            ItemStack item = new ItemStack(models.get(modelName).getLeft());
            if (models.get(modelName).getLeft() == Material.PLAYER_HEAD) {
                item = getCustomSkull(Alphabet.valueOf(String.valueOf(modelName.toUpperCase().charAt(0))).getValue());
            }
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return null;
            }
            meta.setDisplayName(modelName);
            meta.setCustomModelData(models.get(modelName).getRight());
            item.setItemMeta(meta);
            currentGui.addItem(item);
        }

        return guisModels;
    }

    private void makeGuis() {
        guisCategories.clear();
        guisModels.clear();

        HashMap<String, Pair<Material, Integer>> categoryModels = new HashMap<>();
        HashMap<String, Pair<Material, Integer>> finalCategoryModels = categoryModels;
        customModelDatas.forEach((category, modelMap) -> finalCategoryModels.put(category, Pair.of(Material.PLAYER_HEAD, 0)));

        // sort the categories
        categoryModels = categoryModels.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);

        // sort the models
        customModelDatas.forEach((category, modelMap) -> {
            modelMap = modelMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);
            customModelDatas.put(category, modelMap);
        });

        guisCategories = makeInventory("Categories", categoryModels);

        customModelDatas.forEach((category, modelMap) -> guisModels.put(category, makeInventory("Models: " + category, modelMap)));
    }

    public void setPlayerFunction(Player player, PlayerFunction playerFunction) {
        choosenPlayerFunction.put(player.getUniqueId(), playerFunction);
    }
}
