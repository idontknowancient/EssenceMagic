package com.idk.essence.magics;

import com.idk.essence.utils.Util;
import com.idk.essence.utils.configs.ConfigManager;
import com.idk.essence.utils.configs.EssenceConfig;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MagicManager implements Listener {

    /**
     * Used to register listener
     */
    @Getter private static final MagicManager instance = new MagicManager();

    private final static Map<String, MagicDomain> domains = new LinkedHashMap<>();
    private final static Map<String, MagicSignet> signets = new LinkedHashMap<>();

    private MagicManager() {}

    public static void initialize() {
        domains.values().forEach(MagicDomain::initialize);
        domains.clear();
        signets.clear();
        DomainAccordance.registerDomains();
        ConfigManager.Folder.MAGICS.load(MagicManager::register);
        Util.System.info("Registered Magic Domains", domains.size());
        Util.System.info("Registered Magic Signets", signets.size());
    }

    public static void addDomain(String internalName, MagicDomain domain) {
        domains.put(internalName, domain);
    }

    public static void addSignet(String internalName, MagicSignet signet) {
        signets.put(internalName, signet);
    }

    public static boolean hasDomain(String internalName) {
        return domains.containsKey(internalName);
    }

    public static boolean hasSignet(String internalName) {
        return signets.containsKey(internalName);
    }

    @Nullable
    public static MagicDomain getDomain(@Nullable String internalName) {
        return domains.get(internalName);
    }

    @Nullable
    public static MagicSignet getSignet(@Nullable String internalName) {
        return signets.get(internalName);
    }

    @NotNull
    public static Collection<MagicSignet> getAllSignets() {
        return signets.values();
    }

    @NotNull
    public List<MagicSignet> getSignetsByDomain(@Nullable MagicDomain domain) {
        return signets.values().stream().filter(signet -> signet.isDomain(domain)).toList();
    }


    /**
     * Register a magic signet. Case-sensitive.
     * @param internalName the internal name of the magic
     */
    public static void register(String internalName, EssenceConfig config) {
        if(!config.has(internalName) || hasSignet(internalName)) return;

        MagicSignet signet = new MagicSignet.Builder(internalName).fromConfig(config).build();
        signets.put(internalName, signet);
    }
}
