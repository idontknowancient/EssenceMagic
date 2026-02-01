package com.idk.essence.players;

import com.idk.essence.Essence;
import org.bukkit.entity.Player;

public interface DataProvider {

    Essence plugin = Essence.getPlugin();
    Player getPlayer();

}
