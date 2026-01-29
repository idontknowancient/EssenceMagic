package com.idk.essence.utils.nodes.types;

import com.idk.essence.utils.nodes.BaseNode;
import com.idk.essence.utils.nodes.NodeRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TextNode extends BaseNode {

    private Component text = Component.empty();

    public TextNode(Location location) {
        super(location.clone().add(0, 0.5, 0));
    }

    @Override
    public NodeRegistry getNodeType() {
        return NodeRegistry.TEXT;
    }

    @Override
    protected void onSpawn(Entity entity) {
        if(!(entity instanceof TextDisplay display)) return;

        display.text(text);
        display.setBillboard(Display.Billboard.CENTER);
        display.setBrightness(new Display.Brightness(15, 15));
        display.setGlowing(true);
        display.setShadowed(true);
    }

    @Override
    protected void onLoad(Entity entity) {
        if(!(entity instanceof TextDisplay display)) return;
        setDisplayEntity(display);
        setText(display.text());
        generateParticle();
    }

    @Override
    protected void onRemove() {

    }

    @Override
    protected void onUnload() {

    }

    @Override
    protected void onCorrelate() {

    }

    @Override
    protected void onPerform(PlayerInteractEntityEvent event) {

    }

    public void setText(Component text) {
        this.text = text;
        if(getDisplayEntity() instanceof TextDisplay display)
            display.text(text);
    }
}
