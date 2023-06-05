package de.yungcat.lostshulkernoproblem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import de.yungcat.lostshulkernoproblem.handler.EventProcessor;

import java.io.File;
import java.io.IOException;

public final class LostShulkerNoProblem extends JavaPlugin {

    private static Gson gson;

    public File dataFile;
    public static Gson getGson() {
        return gson;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        new EventProcessor(this);

        gson = new GsonBuilder().setPrettyPrinting().create();

        File dataFolder = getDataFolder();
        dataFile = new File(dataFolder, "lostShulker.json");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        else if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
