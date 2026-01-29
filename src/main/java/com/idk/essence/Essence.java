package com.idk.essence;

import com.idk.essence.utils.Register;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Essence extends JavaPlugin {

    @Getter private static Essence plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Register.registerDependencies();
        Register.initialize();
        Register.registerListeners();
        Register.registerCommands();
    }

    @Override
    public void onDisable() {
        Register.shutdown();
    }
}
