package com.idk.essence.items.arcana.types;

import com.idk.essence.utils.configs.EssenceConfig;

public class SimpleWand extends WandArcana {

    private SimpleWand(Builder builder) {
        super(builder);
    }

    @Override
    public void cast() {

    }

    public static class Builder extends WandArcana.Builder<Builder> {

        public Builder(String materialString) {
            super(materialString);
        }

        public Builder fromConfig(EssenceConfig config) {
            super.fromConfig(config);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public SimpleWand build() {
            return new SimpleWand(this);
        }
    }
}
