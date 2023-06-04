package de.yungcat.lostshulkernoproblem.handler;

import com.google.gson.JsonObject;
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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EventProcessor implements Listener {

    LostShulkerNoProblem plugin;

    public EventProcessor(LostShulkerNoProblem plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Block Data
    Block blockPlaced;
    BlockState blockState;
    Material blockType;

    //Player Data
    Player playerPlaced;
    String playerName;
    UUID playerUUID;
    UUID worldUUID;

    //Shulker Position
    Location blockPos;
    int blockPosX;
    int blockPosY;
    int blockPosZ;
    String blockPosWorld;
    String blockPosXYZ;
    String blockTypeString;

    //List with all the necessary data to save
    String[] shulkerData;

    //Saving

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        blockPlaced = e.getBlockPlaced();
        blockState = e.getBlockPlaced().getState();
        playerPlaced = e.getPlayer();
        playerName = e.getPlayer().getName();
        playerUUID = playerPlaced.getUniqueId();
        worldUUID = blockPlaced.getWorld().getUID();

        String shulkerData;

        if (blockState instanceof ShulkerBox) {
            //Set all Variables
            blockType = blockPlaced.getType();
            blockPos = blockPlaced.getLocation();
            blockPosX = blockPlaced.getLocation().getBlockX();
            blockPosY = blockPlaced.getLocation().getBlockY();
            blockPosZ = blockPlaced.getLocation().getBlockZ();
            blockPosXYZ = "x: " + blockPosX + ", y: " + blockPosY + ", z: " + blockPosZ;

            //Changing World Name to make it more readable for user
            /*if(blockPos.getWorld().getName().equals("world")) {
                blockPosWorld = "Overworld";
            }
            else if (blockPos.getWorld().getName().equals("world_nether")) {
                blockPosWorld = "Nether";
            }
            else blockPosWorld = "The End";
            */
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
            shulkerData = jsonData.toString();
            Bukkit.getConsoleSender().sendMessage(shulkerData);

            //Changing blockType Name to make it more readable
            blockTypeString = blockType.toString().replace("_", " ");
            blockTypeString = blockTypeString.toLowerCase();

            //Setting all variables to the String[]
            //shulkerData = new String[]{playerName, blockTypeString, blockPosXYZ, blockPosWorld};

            //DEBUG PLAYER MESSAGE
            //playerPlaced.sendMessage(playerName + "\n" + blockTypeString + "\n" + blockPosXYZ + "\n" + blockPosWorld);

            //Saving the Data into the JSON File
            String uuid = plugin.gson.toJson(jsonData);
            try (FileWriter writer = new FileWriter(plugin.dataFile, true)) {
                writer.write(uuid);
                writer.write(System.lineSeparator());
            } catch (IOException excep) {
                excep.printStackTrace();
            }
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
            brokenShulkerCoordinates.add(new Coordinates(worldUUID, blockPosX, blockPosY, blockPosZ));

            //Map Entry for the Shulker Colors
            Map<String, List<Coordinates>> shulkerColors = new HashMap<>();
            shulkerColors.put(blockType.toString(), brokenShulkerCoordinates);

            //Map Entry for the Player UUID
            Map<String, Map<String, List<Coordinates>>> playerData = new HashMap<>();
            playerData.put(playerUUID.toString(), shulkerColors);

            brokenShulkerData.putAll(playerData);
            //shulkerData = brokenShulkerData.toString();
            Bukkit.getConsoleSender().sendMessage(shulkerData);
        }
    }


    public void removeFieldsIfSame() {
        JsonObject jsonObject = readShulkerData();

    }

    private JsonObject readShulkerData() {
        JsonObject jsonObject = new JsonObject();

        try (FileReader reader = new FileReader(plugin.dataFile)) {
            jsonObject = plugin.gson.fromJson(reader, JsonObject.class);
        } catch (IOException excep) {
            excep.printStackTrace();
        }

        return jsonObject;
    }


    //Coordinates Object for the adding and removing of the data in the json
    class Coordinates {
        private UUID world = worldUUID;
        private int x = blockPosX;
        private int y = blockPosY;
        private int z = blockPosZ;

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
