package com.idk.essence.utils.nodes.types;

import com.idk.essence.Essence;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.nodes.BaseNode;
import com.idk.essence.utils.nodes.NodeManager;
import com.idk.essence.utils.nodes.NodeRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;

public class ItemNode extends BaseNode {

    private ItemStack currentItem;
    /**
     * The attachment of the item node
     */
    private ActionNode actionNode;
    private TextNode textNode;

    private BukkitTask animation;

    public ItemNode(Location location) {
        super(location);
    }

    @Override
    public NodeRegistry getNodeType() {
        return NodeRegistry.ITEM;
    }

    @Override
    public void onSpawn(Entity entity) {
        if(!(entity instanceof ItemDisplay display)) return;
        // Correlation action node
        actionNode = (ActionNode) NodeRegistry.ACTION.getConstructor().apply(getLocation());
        actionNode.setAttachment(true);
        actionNode.setAction(this::setItem);
        // Correlation text node
        textNode = (TextNode) NodeRegistry.TEXT.getConstructor().apply(getLocation());
        textNode.setAttachment(true);

        // Link
        setCorrelationActionUUID(actionNode.getSelfUUID());
        setCorrelationTextUUID(textNode.getSelfUUID());
        Key.Type.NODE_CORRELATION_ACTION.set(display, getCorrelationActionUUID());
        Key.Type.NODE_CORRELATION_TEXT.set(display, getCorrelationTextUUID());

        // ItemDisplay attributes
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        if(currentItem != null)
            display.setItemStack(currentItem);
        applyAnimation(display);
        setScale(0.7f);

        actionNode.spawn();
        textNode.spawn();
    }

    @Override
    protected void onLoad(Entity entity) {
        if(!(entity instanceof ItemDisplay display)) return;
        setDisplayEntity(display);
        // Get the item back from the entity
        setItem(display.getItemStack());
        applyAnimation(display);
        if(currentItem == null || currentItem.getType().equals(Material.AIR))
            generateParticle();
    }

    @Override
    public void onRemove() {
        dropItem();
        actionNode.remove();
        textNode.remove();
        stopAnimation();
    }

    @Override
    protected void onUnload() {
        stopAnimation();
    }

    @Override
    protected void onCorrelate() {
        if(NodeManager.get(getCorrelationActionUUID()) instanceof ActionNode action) {
            action.setAction(this::setItem);
            this.actionNode = action;
        } else {
            setCorrelationActionUUID(null);
            Key.Type.NODE_CORRELATION_ACTION.remove(getDisplayEntity());
        }
        if(NodeManager.get(getCorrelationTextUUID()) instanceof TextNode text) {
            this.textNode = text;
        } else {
            setCorrelationTextUUID(null);
            Key.Type.NODE_CORRELATION_TEXT.remove(getDisplayEntity());
        }
    }

    @Override
    protected void onPerform(PlayerInteractEntityEvent event) {
        actionNode.perform(event);
    }

    private void setItem(ItemStack item) {
        this.currentItem = item;
        if(getDisplayEntity() instanceof ItemDisplay display)
            display.setItemStack(item);
    }

    private void setItem(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if(itemInHand.getType() == Material.AIR) {
            dropItem();
            generateParticle();
            textNode.setText(Component.empty());
            return;
        }
        // Drop item if there is an item on the display and player's hand
        if(this.currentItem != null && this.currentItem.getType() != Material.AIR) {
            dropItem();
            generateParticle();
            textNode.setText(Component.empty());
            return;
        }

        ItemStack toPut = itemInHand.clone();
        toPut.setAmount(1);
        setItem(toPut);
        textNode.setText(toPut.getItemMeta().displayName());

        // Deduct the amount
        if(itemInHand.getAmount() > 1) {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
            stopParticle();
        } else {
            player.getInventory().setItemInMainHand(null);
            stopParticle();
        }
    }

    public void setScale(float scale) {
        if(getDisplayEntity() instanceof ItemDisplay display) {
            Transformation transformation = display.getTransformation();
            transformation.getScale().set(scale, scale, scale);
            display.setTransformation(transformation);
        }
    }

    private void dropItem() {
        if(currentItem != null && currentItem.getType() != Material.AIR) {
            getLocation().getWorld().dropItemNaturally(getLocation(), currentItem);
            currentItem = null;
            setItem(new ItemStack(Material.AIR));
        }
    }

    /**
     * Make display keep rotating.
     */
    private void applyAnimation(ItemDisplay display) {
        animation = new BukkitRunnable() {
            double time = 0;

            @Override
            public void run() {
                if(!display.isValid() || display.isDead()) {
                    this.cancel();
                    return;
                }
                Transformation transformation = display.getTransformation();

                // Rotation
                float yaw = (float) (time * 0.2);
                transformation.getLeftRotation().rotationY(yaw);

                // Float
                float floatingY = (float) Math.sin(time) * 0.07f;
                transformation.getTranslation().set(0, floatingY, 0);

                // Interpolation, same as period
                display.setInterpolationDuration(4);
                display.setInterpolationDelay(0);
                display.setTransformation(transformation);

                time += 0.5;
                if(time >= 10000000) time = 0;
            }
        }.runTaskTimer(Essence.getPlugin(), 0L, 4L);
    }

    private void stopAnimation() {
        if(animation != null)
            animation.cancel();
    }
}
