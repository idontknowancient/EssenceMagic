package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.configs.ConfigFile;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {

    ConfigFile.ConfigName config = ConfigFile.ConfigName.MESSAGES;

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reload the plugin config";
    }

    @Override
    public String getSyntax() {
        return "/essence reload";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void perform(Player p, String[] args) {
        EssenceMagic.initialize();
    }
}
