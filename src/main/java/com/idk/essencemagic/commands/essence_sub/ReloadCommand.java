package com.idk.essencemagic.commands.essence_sub;

import com.idk.essencemagic.EssenceMagic;
import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.utils.configs.ConfigFile;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin config.";
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
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_RELOAD.name))
            SystemMessage.INADEQUATE_PERMISSION.send(p);
        EssenceMagic.initialize();
        SystemMessage.SUCCESSFULLY_RELOADED.send(p);
    }
}
