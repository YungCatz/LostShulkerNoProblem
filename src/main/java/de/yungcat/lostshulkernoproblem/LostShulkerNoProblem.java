package de.yungcat.lostshulkernoproblem;

import org.bukkit.plugin.java.JavaPlugin;
import de.yungcat.lostshulkernoproblem.handler.EventProcessor;

public final class LostShulkerNoProblem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        new EventProcessor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
