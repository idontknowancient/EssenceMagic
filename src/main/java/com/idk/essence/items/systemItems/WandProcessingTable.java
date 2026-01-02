package com.idk.essence.items.systemItems;

import com.idk.essence.items.systemItems.features.SISlotBlock;
import lombok.Getter;
import org.bukkit.NamespacedKey;

public class WandProcessingTable extends SISlotBlock {

    @Getter private static final NamespacedKey wandProcessingTableKey = new NamespacedKey(getPlugin(), "wand-processing-table-key");

    public WandProcessingTable(String name) {
        super(name);
    }

    @Override
    protected NamespacedKey getCustomKey() {
        return wandProcessingTableKey;
    }
}
