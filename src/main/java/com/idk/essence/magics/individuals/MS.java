package com.idk.essence.magics.individuals;

import com.idk.essence.magics.MagicDomain;
import com.idk.essence.magics.MagicManager;
import com.idk.essence.magics.MagicSignet;
import com.idk.essence.players.PlayerData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Magic Storage, part of Individual Magical System
 */
public class MS {

    public static List<Component> getAptitudeDisplay(@NotNull PlayerData data) {
        return data.getGottenAptitudes().stream()
                .map(MagicManager::getDomain)
                .filter(Objects::nonNull)
                .map(MagicDomain::getFullDisplay).toList();
    }

    public static List<Component> getDomainDisplay(@NotNull PlayerData data) {
        return data.getLearnedDomains().stream()
                .map(MagicManager::getDomain)
                .filter(Objects::nonNull)
                .map(MagicDomain::getFullDisplay).toList();
    }


    public static List<Component> getSignetDisplay(@NotNull PlayerData data) {
        return data.getLearnedSignets().stream()
                .map(MagicManager::getSignet)
                .filter(Objects::nonNull)
                .map(MagicSignet::getDisplayName).toList();
    }

    public static boolean hasAptitude(@NotNull PlayerData data, String aptitudeName) {
        return Optional.ofNullable(MagicManager.getDomain(aptitudeName)).map(domain ->
                data.getGottenAptitudes().contains(domain.getInternalName())).orElse(false);
    }

    public static boolean hasAptitude(@NotNull PlayerData data, @Nullable MagicDomain aptitude) {
        return Optional.ofNullable(aptitude).map(a ->
                data.getGottenAptitudes().contains(a.getInternalName())).orElse(false);
    }

    public static boolean hasDomain(@NotNull PlayerData data, String domainName) {
        return Optional.ofNullable(MagicManager.getDomain(domainName)).map(domain ->
                data.getLearnedDomains().contains(domain.getInternalName())).orElse(false);
    }

    public static boolean hasDomain(@NotNull PlayerData data, @Nullable MagicDomain domain) {
        return Optional.ofNullable(domain).map(d ->
                data.getLearnedDomains().contains(d.getInternalName())).orElse(false);
    }

    public static boolean hasSignet(@NotNull PlayerData data, String signetName) {
        return Optional.ofNullable(MagicManager.getSignet(signetName)).map(signet ->
                data.getLearnedSignets().contains(signet.getInternalName())).orElse(false);
    }

    public static boolean hasSignet(@NotNull PlayerData data, @Nullable MagicSignet signet) {
        return Optional.ofNullable(signet).map(s ->
                data.getLearnedSignets().contains(s.getInternalName())).orElse(false);
    }

    /**
     * Add magic aptitude to a player.
     * Automatically save to config.
     * @return successful
     */
    public static boolean addAptitude(@NotNull PlayerData data, String aptitudeName) {
        return Optional.ofNullable(MagicManager.getDomain(aptitudeName))
                .filter(domain -> !hasAptitude(data, domain))
                .map(domain -> {
                    data.getGottenAptitudes().add(domain.getInternalName());
                    data.setToConfig();
                    return true;
                }).orElse(false);
    }

    /**
     * Add magic domain to a player.
     * Automatically save to config.
     * @return successful
     */
    public static boolean addDomain(@NotNull PlayerData data, String domainName) {
        return Optional.ofNullable(MagicManager.getDomain(domainName))
                .filter(domain -> !hasDomain(data, domain))
                .map(domain -> {
                    data.getLearnedDomains().add(domain.getInternalName());
                    data.setToConfig();
                    return true;
                }).orElse(false);
    }

    /**
     * Add magic signet to a player.
     * Automatically save to config.
     * @return successful
     */
    public static boolean addSignet(@NotNull PlayerData data, String signetName) {
        return Optional.ofNullable(MagicManager.getSignet(signetName))
                .filter(signet -> !hasSignet(data, signet))
                .map(signet -> {
                    data.getLearnedSignets().add(signet.getInternalName());
                    data.setToConfig();
                    return true;
                }).orElse(false);
    }
}
