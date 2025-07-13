package com.idk.essencemagic.magics;

import com.idk.essencemagic.magics.childTypes.FireBeam;
import com.idk.essencemagic.magics.childTypes.SakuraMaite;
import com.idk.essencemagic.utils.configs.ConfigFile;

import java.util.Set;

public class MagicHandler {

    public static void initialize() {
        Magic.magics.clear();
        setMagics();
    }

    private static void setMagics() {
        Set<String> magicSet = ConfigFile.ConfigName.MAGICS.getConfig().getKeys(false);
        for(String magicName : magicSet) {
            if(magicName.equalsIgnoreCase(ChildType.FIRE_BEAM.name))
                Magic.magics.put(magicName, new FireBeam(magicName));
            if(magicName.equalsIgnoreCase(ChildType.SAKURA_MAITE.name))
                Magic.magics.put(magicName, new SakuraMaite(magicName));
        }
    }
}
