package com.idk.essence.players.managers;

import com.idk.essence.magics.MagicManager;
import com.idk.essence.players.AbstractDataManager;
import com.idk.essence.players.PlayerDataRegistry;
import com.idk.essence.players.providers.MagicDataProvider;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MagicDataManager extends AbstractDataManager implements MagicDataProvider {

    // In config
    private final List<String> gottenAptitudes;
    private final List<String> learnedDomains;
    private final List<String> learnedSignets;

    public MagicDataManager(OfflinePlayer player) {
        super(player);

        // Use ArrayList to make the list mutable
        final PlayerDataRegistry aptitudeRegistry = PlayerDataRegistry.GOTTEN_APTITUDES;
        gottenAptitudes = getOrDefault(aptitudeRegistry.getName(), c ->
                c.getStringList(aptitudeRegistry.getName()).stream()
                        .filter(MagicManager::hasDomain)
                        .collect(Collectors.toCollection(ArrayList::new)), this::getInitialAptitudes);
        // Handle Reset
        if(gottenAptitudes.isEmpty())
            gottenAptitudes.addAll(getInitialAptitudes());

        final PlayerDataRegistry domainRegistry = PlayerDataRegistry.LEARNED_DOMAINS;
        learnedDomains = getOrDefault(domainRegistry.getName(), c ->
                c.getStringList(domainRegistry.getName()).stream()
                        .filter(MagicManager::hasDomain)
                        .collect(Collectors.toCollection(ArrayList::new)), ArrayList::new);

        final PlayerDataRegistry signetRegistry = PlayerDataRegistry.LEARNED_SIGNETS;
        learnedSignets = getOrDefault(signetRegistry.getName(), c ->
                c.getStringList(signetRegistry.getName()).stream()
                        .filter(MagicManager::hasSignet)
                        .collect(Collectors.toCollection(ArrayList::new)), ArrayList::new);
    }

    @Override
    public OfflinePlayer getOfflinePlayer() {
        return getPlayerInstance();
    }

    @Override
    public void setToConfig() {
        if(getConfig() == null) return;
        getConfig().set(PlayerDataRegistry.GOTTEN_APTITUDES.getName(), gottenAptitudes);
        getConfig().set(PlayerDataRegistry.LEARNED_DOMAINS.getName(), learnedDomains);
        getConfig().set(PlayerDataRegistry.LEARNED_SIGNETS.getName(), learnedSignets);
        getConfig().save();
    }

    @Override
    public Map<String, String> getPlaceholders() {
        return Map.of();
    }
}
