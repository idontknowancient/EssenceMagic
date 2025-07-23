package com.idk.essencemagic.items.systemItems;

import com.idk.essencemagic.items.SystemItem;

import java.util.function.Function;

public enum Registry {

    WAND_PROCESSING_TABLE(WandProcessingTable::new),
    ;

    public final Function<String, SystemItem> constructor;

    Registry(Function<String, SystemItem> constructor) {
        this.constructor = constructor;
    }

}
