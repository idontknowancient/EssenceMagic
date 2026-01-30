package com.idk.essence.items.arcana.types;

import com.idk.essence.items.arcana.Arcana;
import com.idk.essence.utils.Key;
import com.idk.essence.utils.configs.EssenceConfig;

public abstract class RuneArcana extends Arcana {

    protected RuneArcana(Builder<?> builder) {
        super(builder);
    }

    public abstract static class Builder<T extends Builder<T>> extends Arcana.Builder<T> {

        public Builder(String materialString) {
            super(materialString);
        }

        protected T fromConfig(EssenceConfig config) {
            super.fromConfig(config);
            itemBuilder.container(Key.Type.ARCANA_RUNE, internalName);
            return self();
        }
    }
}
