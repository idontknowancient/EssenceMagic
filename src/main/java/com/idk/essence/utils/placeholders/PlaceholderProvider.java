package com.idk.essence.utils.placeholders;

import java.util.Map;

public interface PlaceholderProvider {
    Map<String, String> getPlaceholders();

    default String applyTo(String input) {
        for(Map.Entry<String, String> entry : getPlaceholders().entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }
}
