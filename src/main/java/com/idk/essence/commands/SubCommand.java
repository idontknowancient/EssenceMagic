package com.idk.essence.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private final List<String> subCommandsString = new ArrayList<>();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    public List<String> getSubCommandsString() {
        return subCommandsString;
    }

    public abstract void perform(Player p, String[] args);
}
