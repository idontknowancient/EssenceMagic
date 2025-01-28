package com.idk.essencemagic.commands.essence_sub.skill_sub;

import com.idk.essencemagic.commands.SubCommand;
import com.idk.essencemagic.skills.Skill;
import com.idk.essencemagic.skills.SkillHandler;
import com.idk.essencemagic.utils.messages.SystemMessage;
import com.idk.essencemagic.utils.permissions.Permission;
import com.idk.essencemagic.utils.permissions.SystemPermission;
import org.bukkit.entity.Player;

import java.util.List;

public class ForceCommand extends SubCommand {

    @Override
    public String getName() {
        return "force";
    }

    @Override
    public String getDescription() {
        return "Cast a specific skill forcibly";
    }

    @Override
    public String getSyntax() {
        return "/essence skill force <skill>";
    }

    @Override
    public void perform(Player p, String[] args) {
        if(!SystemPermission.checkPerm(p, Permission.COMMAND_SKILL_FORCE.name)){
            SystemMessage.INADEQUATE_PERMISSION.send(p);
            return;
        }
        if(args.length <= 2) {
            SystemMessage.TOO_LITTLE_ARGUMENT.send(p, getSyntax());
            return;
        }
        for(String s : Skill.skills.keySet()) {
            if(args[2].equalsIgnoreCase(s)) {
                SkillHandler.handleSkill(p, Skill.skills.get(s), new int[]{0}, true);
                SystemMessage.SKILL_FORCED.send(p, Skill.skills.get(s));
                return;
            }
        }
        SystemMessage.SKILL_NOT_FOUND.send(p);
    }
}
