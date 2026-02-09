package com.idk.essence.magics.individuals;

import com.idk.essence.magics.RoalkomEngine;
import com.idk.essence.magics.domains.IntensityDomain;
import com.idk.essence.players.PlayerData;
import com.idk.essence.players.PlayerDataManager;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.Util;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManaInfusion {

    /**
     * Indicate the mana infused percentage based on the capacity of the wand.
     */
    private static final BossBar.Color[] colors = {BossBar.Color.BLUE, BossBar.Color.GREEN, BossBar.Color.YELLOW,
            BossBar.Color.RED, BossBar.Color.PURPLE};

    @Getter private final Player player;
    @Getter private final ItemStack wand;
    private final PlayerData data;
    private double infusedMana = 0;
    private double maxMana;
    private double addAmount;
    private final BossBar bossBar;
    /**
     * Whether infusion stops and the bossbar is hidden.
     */
    @Getter private boolean isHidden;

    public ManaInfusion(Player player, ItemStack wand) {
        this.player = player;
        this.wand = wand;
        data = PlayerDataManager.get(player);

        bossBar = BossBar.bossBar(Component.empty(), 0f, colors[0], BossBar.Overlay.NOTCHED_10);
        reset();
    }

    public void reset() {
        infusedMana = 0;
        maxMana = Key.Type.WAND_MAX_MANA.getContentOrDefault(wand, 120d);
        addAmount = Key.Type.WAND_MANA_INFUSION.getContentOrDefault(wand, 2d);
        isHidden = false;
        player.showBossBar(bossBar);
    }

    public void infuse() {
        // Mana not sufficient
        if(data.getMana() < addAmount)
            return;
        infusedMana += addAmount;
        data.deductMana(addAmount);
        if(infusedMana > maxMana) {
            stop();
            // Create explosion to simulate overload. yield / fire / break block
            player.getWorld().createExplosion(player.getLocation().clone().add(0, 1, 0), 4.0f, false, false);
        }

        double roalkomIndex = RoalkomEngine.calculateRoalkomIndex(1, infusedMana);
        IntensityDomain domain = RoalkomEngine.indexToDomain(roalkomIndex);
        if(domain == null) return;

        // Default color is &7
        Component format = RoalkomEngine.format(roalkomIndex)
                .append(Component.text(" " + Util.Tool.round(infusedMana, 2))
                        .colorIfAbsent(TextColor.color(170, 170, 170)));
        RoalkomEngine.AvailableRange range = domain.getAvailableRange();
        // Color adds 1 if ratio raises 0.2
        bossBar.color(colors[Math.clamp((int) (infusedMana / maxMana * 5), 0, 4)]);
        bossBar.name(format);
        bossBar.progress((float) Math.min(1.0, (roalkomIndex - range.getLowerLimit())
                / (range.getUpperLimit() - range.getLowerLimit())));

        player.getWorld().spawnParticle(
                Particle.SOUL_FIRE_FLAME, player.getLocation().add(0, 1.2, 0), 2, 0.2, 0.2, 0.2, 0.02);
    }

    public void fade() {
        infusedMana -= addAmount * 2;
        if(infusedMana < 0) {
            stop();
            infusedMana = 0;
            return;
        }

        double roalkomIndex = RoalkomEngine.calculateRoalkomIndex(1, infusedMana);
        IntensityDomain domain = RoalkomEngine.indexToDomain(roalkomIndex);
        if(domain == null) return;

        // Default color is &7
        Component format = RoalkomEngine.format(roalkomIndex)
                .append(Component.text(" " + Util.Tool.round(infusedMana, 2))
                        .colorIfAbsent(TextColor.color(170, 170, 170)));
        RoalkomEngine.AvailableRange range = domain.getAvailableRange();
        // Color subtracts 1 if ratio reduces 0.2
        bossBar.color(colors[Math.clamp((int) (infusedMana / maxMana * 5), 0, 4)]);
        bossBar.name(format);
        bossBar.progress((float) Math.min(1.0, (roalkomIndex - range.getLowerLimit())
                / (range.getUpperLimit() - range.getLowerLimit())));
    }

    public void stop() {
        isHidden = true;
        player.hideBossBar(bossBar);
    }
}
