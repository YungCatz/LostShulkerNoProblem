package de.yungcat.lostshulkernoproblem.handler;

import de.yungcat.lostshulkernoproblem.LostShulkerNoProblem;
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

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EventProcessor implements Listener {

    private final LostShulkerNoProblem plugin;

    public EventProcessor(LostShulkerNoProblem plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
            
            //DATA Structure
            Map<String, Map<String, List<Coordinates>>> jsonData = new HashMap<>();

            //List for the Coordinates
            List<Coordinates> coordinates = new ArrayList<>();
            coordinates.add(new Coordinates(worldUUID, blockPosX, blockPosY, blockPosZ));

            //Map Entry for the Shulker Colors
            Map<String, List<Coordinates>> shulkerColors = new HashMap<>();
            shulkerColors.put(blockType.toString(), coordinates);

            //Map Entry for the Player UUID
            Map<String, Map<String, List<Coordinates>>> playerData = new HashMap<>();
            playerData.put(playerUUID.toString(), shulkerColors);

            jsonData.putAll(playerData);

            //Saving the Data into the JSON File
            String uuid = LostShulkerNoProblem.getGson().toJson(jsonData);
            try (FileWriter writer = new FileWriter(plugin.dataFile, true)) {
                writer.write(uuid);
                writer.write(System.lineSeparator());
            } catch (IOException except) {
                except.printStackTrace();
            }

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

            //DATA Structure
            Map<String, Map<String, List<Coordinates>>> brokenShulkerData = new HashMap<>();

            //List for the Coordinates
            List<Coordinates> brokenShulkerCoordinates = new ArrayList<>();
            brokenShulkerCoordinates.add(new Coordinates(worldBrokenBlock, brokenPosX, brokenPosY, brokenPosZ));

            //Map Entry for the Shulker Colors
            Map<String, List<Coordinates>> shulkerColors = new HashMap<>();
            shulkerColors.put(brokenBlockType.toString(), brokenShulkerCoordinates);

            //Map Entry for the Player UUID
            Map<String, Map<String, List<Coordinates>>> playerData = new HashMap<>();
            playerData.put(playerBrokeBlock.toString(), shulkerColors);

            brokenShulkerData.putAll(playerData);
        }
    }

    //Coordinates Object for the adding and removing of the data in the json
    static class Coordinates {
        private final UUID world;
        private final int x;
        private final int y;
        private final int z;

        public Coordinates(UUID world, int x, int y, int z) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public UUID getWorld() {return world;}
        public int x() {return x;}
        public int y() {return y;}
        public int z() {return z;}
    }
}
