package de.yungcat.lostshulkernoproblem.handler;

import com.google.common.base.Objects;
import com.google.common.reflect.TypeToken;
import de.yungcat.lostshulkernoproblem.LostShulkerNoProblem;
import de.yungcat.lostshulkernoproblem.util.Coordinates;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class EventProcessor implements Listener {

    private final LostShulkerNoProblem plugin;
    //DATA Structure
    private final Map<String, Map<String, List<Coordinates>>> jsonData;

    public EventProcessor(LostShulkerNoProblem plugin) {
        this.plugin = plugin;
        Map<String, Map<String, List<Coordinates>>> json;
        Type type = new TypeToken<Map<String, Map<String, List<Coordinates>>>>() {
        }.getType();
        try {
            json = LostShulkerNoProblem.getGson().fromJson(new FileReader(plugin.dataFile), type);
        } catch (FileNotFoundException e) {
            json = new HashMap<>();
        }
        this.jsonData = json;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void saveJSONFile() {
        //Saving the Data into the JSON File
        String json = LostShulkerNoProblem.getGson().toJson(jsonData);
        try (FileWriter writer = new FileWriter(plugin.dataFile)) {
            writer.write(json);
        } catch (IOException except) {
            except.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        Block blockPlaced = e.getBlockPlaced();
        BlockState blockState = e.getBlockPlaced().getState();
        Player playerPlaced = e.getPlayer();
        UUID playerUUID = playerPlaced.getUniqueId();
        UUID worldUUID = blockPlaced.getWorld().getUID();

        if (blockState instanceof ShulkerBox) {
            //Set all Variables
            Material blockType = blockPlaced.getType();
            Location blockPos = blockPlaced.getLocation();
            int blockPosX = blockPlaced.getLocation().getBlockX();
            int blockPosY = blockPlaced.getLocation().getBlockY();
            int blockPosZ = blockPlaced.getLocation().getBlockZ();

            Map<String, List<Coordinates>> playerData = jsonData.getOrDefault(playerUUID.toString(), new HashMap<>());
            playerData.compute(blockType.name(), (s, coordinates) -> {
                if (coordinates == null) coordinates = new ArrayList<>();
                coordinates.add(new Coordinates(worldUUID, blockPosX, blockPosY, blockPosZ));
                return coordinates;
            });

            jsonData.put(playerUUID.toString(), playerData);

            //Saving the Data into the JSON File
            saveJSONFile();

            //DEBUG PLAYER MESSAGE
            //playerPlaced.sendMessage(playerName + "\n" + blockTypeString + "\n" + blockPosXYZ + "\n" + blockPosWorld);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block brokenBlock = e.getBlock();
        BlockState brokenBlockState = e.getBlock().getState();
        UUID playerBrokeBlock = e.getPlayer().getUniqueId();
        Material brokenBlockType = brokenBlock.getType();
        UUID worldBrokenBlock = brokenBlock.getWorld().getUID();
        int brokenPosX = brokenBlock.getX();
        int brokenPosY = brokenBlock.getY();
        int brokenPosZ = brokenBlock.getZ();

        if (brokenBlockState instanceof ShulkerBox) {
            Coordinates coordinates = new Coordinates(worldBrokenBlock, brokenPosX, brokenPosY, brokenPosZ);

            for (Map.Entry<String, Map<String, List<Coordinates>>> data : jsonData.entrySet()) {
                for (Map.Entry<String, List<Coordinates>> colors : data.getValue().entrySet()) {
                    for (Coordinates cord : colors.getValue()) {
                        if (cord.equals(coordinates)) {
                            colors.getValue().remove(cord);
                            saveJSONFile();
                            return;
                        }
                    }
                }
            }

        }
    }
}
