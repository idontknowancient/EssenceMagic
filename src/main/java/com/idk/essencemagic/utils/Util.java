package com.idk.essencemagic.utils;

import com.idk.essencemagic.EssenceMagic;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final EssenceMagic plugin = EssenceMagic.getPlugin();


    private static final Pattern hexCode = Pattern.compile("#[a-fA-F0-9]{6}"); //regex

    /*private static final Map<String, Util> utilList = new HashMap();

    public static Util getUtil() { //get default config
        return utilList.get("");
    }

    public static Util getUtil(String configName) { //get custom config, no .yml needed
        return utilList.get(configName+".yml");
    }

    private Util() { //the constructors should be prior to other methods using these
        config = plugin.getConfig();
    }

    private Util(String config_file) {
        config = EssenceConfig.getConfigMap().get(config_file).getConfig();
    }

    public static void setUtil() {
        utilList.put("", new Util()); //default config
        Arrays.stream(EssenceMagic.getConfigNames()).forEach(s-> utilList.put(s, new Util(s))); //custom config
    }*/

    public static String translatePlaceholder(Player p, String s) {
        return PlaceholderAPI.setPlaceholders(p, s);
    }

    public static String colorize(String string) { //hex support
        Matcher match = hexCode.matcher(string); //find regex in a string
        while(match.find()) {
            String hexStr = string.substring(match.start(), match.end());
            string = string.replace(hexStr, ChatColor.of(hexStr)+"");
            match = hexCode.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public void consoleOuts(String s) {
        plugin.getLogger().log(Level.INFO, s);
    }
}
