package com.idk.essence.commands.essence_sub.skill_sub;

import com.idk.essence.commands.EssenceCommand;
import com.idk.essence.commands.SubCommand;
import com.idk.essence.skills.Skill;
import com.idk.essence.skills.SkillHandler;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.permissions.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForceCommand extends SubCommand {

    public ForceCommand(String name) {
        super(name);
    }

    @Override
    public String getDescription() {
        return "Cast a specific skill forcibly.";
    }

    @Override
    public String getSyntax(CommandSender sender) {
        return EssenceCommand.getSyntaxFromStrings("/essence skill force", "skill", true);
    }

    @Override
    public Permission getPermission() {
        return Permission.COMMAND_SKILL_FORCE;
    }

    @Override
    protected int getLeastArgs() {
        return 3;
    }

    @Override
    protected boolean isPlayerOnly() {
        return true;
    }

    @Override
    public @Nullable List<String> getTabCompletion(Player p, String[] args) {
        return Skill.skills.keySet().stream().toList();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!preCheck(sender, args)) return;
        Player p = (Player) sender;
        String internalName = args[2];
        Skill skill = Skill.skills.get(internalName);
        if(skill == null) {
            SystemMessage.SKILL_NOT_FOUND.send(p);
            return;
        }
        SkillHandler.handleSkill(p, skill, new int[]{0}, true);
        SystemMessage.SKILL_FORCED.send(p, skill);
    }
}
