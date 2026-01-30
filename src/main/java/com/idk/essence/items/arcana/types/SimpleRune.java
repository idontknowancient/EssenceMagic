package com.idk.essence.items.arcana.types;

import com.idk.essence.utils.configs.EssenceConfig;

public class SimpleRune extends RuneArcana {

    private SimpleRune(Builder builder) {
        super(builder);
    }

    public static class Builder extends RuneArcana.Builder<Builder> {

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
        public SimpleRune build() {
            return new SimpleRune(this);
        }
    }
}
