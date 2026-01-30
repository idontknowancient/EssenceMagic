package com.idk.essence.items.artifacts.behaviors;

import com.idk.essence.items.arcana.ArcanaFactory;
import com.idk.essence.items.artifacts.ArtifactBehavior;
import com.idk.essence.items.artifacts.ArtifactFactory;
import com.idk.essence.items.artifacts.ArtifactRegistry;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.messages.SystemMessage;
import com.idk.essence.utils.nodes.NodeFeature;
import com.idk.essence.utils.particles.ParticleFeature;
import org.bukkit.Color;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WandWorkTableBehavior implements ArtifactBehavior, ParticleFeature, NodeFeature {

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        generateParticle(event.getBlockPlaced().getLocation());
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        stopParticle(block.getLocation());
        removeNode(block);
    }

    @Override
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack item = event.getItem();
        if(block == null) return;
        if(Key.Type.NODE_SELF.getContent(block) == null) {
            if(!ArcanaFactory.isWand(item)) {
                SystemMessage.NOT_WAND.send(player);
                return;
            }

            int amount = Key.Type.WAND_SLOT.getContentOrDefault(item, 1);
            // Surrounding
            spawnNode(block, player.getLocation().getYaw(), amount);
            // Center
            spawnItemNode(Key.Type.NODE_SELF.getContent(block), player.getEquipment().getItemInMainHand(),
                    block.getLocation().clone().add(0.5, 1.8, 0.5), false,
                    Color.GREEN, false, true);
            player.getEquipment().setItemInMainHand(null);
        } else {
            removeNode(block);
        }
    }

    @Override
    public void onItemInteract(PlayerInteractEvent event) {

    }

    @Override
    @Nullable
    public ConfigurationSection getParticleSection() {
        return ArtifactFactory.getParticleSection(ArtifactRegistry.WAND_WORK_TABLE.getInternalName());
    }

    @Override
    @Nullable
    public ConfigurationSection getNodeSection() {
        return ArtifactFactory.getNodeSection(ArtifactRegistry.WAND_WORK_TABLE.getInternalName());
    }
}
