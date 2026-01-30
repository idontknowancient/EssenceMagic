package com.idk.essence.items.arcana.types;

import com.idk.essence.items.arcana.Arcana;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.configs.EssenceConfig;
import org.bukkit.inventory.ItemStack;

public abstract class WandArcana extends Arcana {

    private final int maxStackSize = 1;

    protected WandArcana(Builder<?> builder) {
        super(builder);
    }

    @Override
    public ItemStack create() {
        ItemStack item = super.create();
        item.editMeta(meta -> meta.setMaxStackSize(maxStackSize));
        return item;
    }

    public abstract void cast();

    public abstract static class Builder<T extends Builder<T>> extends Arcana.Builder<T> {
        protected int slot = 1;
        protected double maxMana = 100;

        public Builder(String materialString) {
            super(materialString);
        }

        public T slot(int slot) {
            this.slot = Math.max(slot, 1);
            itemBuilder.container(Key.Type.WAND_SLOT, slot);
            return self();
        }

        public T maxMana(double maxMana) {
            this.maxMana = maxMana;
            return self();
        }

        protected T fromConfig(EssenceConfig config) {
            super.fromConfig(config);
            slot(config.getInteger(internalName + ".magic-slot"));
            itemBuilder.container(Key.Type.ARCANA_WAND, internalName);
            return self();
        }
    }
}
