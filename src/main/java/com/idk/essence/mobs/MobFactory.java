package com.idk.essence.mobs;

import com.idk.essence.utils.configs.ConfigFile;

import java.util.HashMap;
import java.util.Map;

public class MobFactory {

    private static final Map<String, MobTemplate> mobs = new HashMap<>();

    private static ConfigFile.ConfigName cm;

    public static void initialize() {
        mobs.clear();
        cm = ConfigFile.ConfigName.MOBS;
    }
}
