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

    //Shulker Position
    Location blockPos;
    int blockPosX;
    int blockPosY;
    int blockPosZ;
    String blockPosWorld;
    String blockPosXYZ;

    //Saving

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        blockPlaced = e.getBlockPlaced();
        blockState = e.getBlockPlaced().getState();
        playerPlaced = e.getPlayer();
        playerName = e.getPlayer().getName();

        if (blockState instanceof ShulkerBox) {
            //Set all Variables
            blockType = blockPlaced.getType();
            blockPos = blockPlaced.getLocation();
            blockPosX = blockPlaced.getLocation().getBlockX();
            blockPosY = blockPlaced.getLocation().getBlockY();
            blockPosZ = blockPlaced.getLocation().getBlockZ();
            blockPosXYZ = "x: " + blockPosX + ", y: " + blockPosY + ", z: " + blockPosZ;

            //Changing World Name to make it more readable for user
            if(blockPos.getWorld().getName().equals("world")) {
                blockPosWorld = "Overworld";
            }
            else if (blockPos.getWorld().getName().equals("world_nether")) {
                blockPosWorld = "Nether";
            }
            else blockPosWorld = "The End";

            //Changing blockType Name to make it more readable
            String blockTypeString;
            blockTypeString = blockType.toString().replace("_", " ");
            blockTypeString = blockTypeString.toLowerCase();

            //DEBUG PLAYER MESSAGE
            playerPlaced.sendMessage(playerName + "\n" + blockTypeString + "\n" + blockPosXYZ + "\n" + blockPosWorld);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

    }
    //customName of Shulker Box in Array
}
