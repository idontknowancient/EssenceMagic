package com.idk.essencemagic.magics.modifiers;

import com.idk.essencemagic.magics.Modifier;
import com.idk.essencemagic.utils.configs.ConfigFile;
import lombok.Getter;

@Getter
public class Damage extends Modifier {

    private final double base;

    //linear or exponential
    private final String type;

    private final String add;

    private final double amount;

    public Damage(String magicName) {
        ConfigFile.ConfigName cm = ConfigFile.ConfigName.MAGICS;
        String path = magicName + ".modifier.damage";

        // set modifier damage base (default to 0)
        if(cm.isDouble(path + ".base") && cm.getDouble(path + ".base") >= 0)
            base = cm.getDouble(path + ".base");
        else if(cm.isInteger(path + ".base") && cm.getInteger(path + ".base") >= 0)
            base = cm.getInteger(path + ".base");
        else
            base = 0;

        // set modifier damage type (linear / exponential)(default to linear)
        if(cm.isString(path + ".type") && cm.getString(path + ".type").equalsIgnoreCase("exponential"))
            type = "exponential";
        else
            type = "linear";

        // set modifier damage add (tier / level)(default to tier)
        if(cm.isString(path + ".add") && cm.getString(path + ".add").equalsIgnoreCase("level"))
            add = "level";
        else
            add = "tier";

        // set modifier damage add amount (default to 0)
        if(cm.isDouble(path + ".amount") && cm.getDouble(path + ".amount") >= 0)
            amount = cm.getDouble(path + ".amount");
        else if(cm.isInteger(path + ".amount") && cm.getInteger(path + ".amount") >= 0)
            amount = cm.getInteger(path + ".amount");
        else
            amount = 0;
    }
}
