package de.yungcat.lostshulkernoproblem;

import org.bukkit.plugin.java.JavaPlugin;
import de.yungcat.lostshulkernoproblem.handler.allEvents;

public final class LostShulkerNoProblem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        new allEvents(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
