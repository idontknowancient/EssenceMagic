package com.idk.essence.commands;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class SubCommand {

    private final List<SubCommand> subCommands = new ArrayList<>();

    private final List<String> subCommandsString = new ArrayList<>();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(Player p, String[] args);
}
