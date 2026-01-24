package com.idk.essence.items.artifacts;

import com.idk.essence.items.artifacts.behaviors.WandWorkTableBehavior;

import java.util.Arrays;

public enum ArtifactRegistry {

    WAND_WORK_TABLE("wand_work_table", new WandWorkTableBehavior())
    ;

    private final String internalName;
    private final ArtifactBehavior behavior;

    ArtifactRegistry(String internalName, ArtifactBehavior behavior) {
        this.internalName = internalName;
        this.behavior = behavior;
    }

    public static void registerBehaviors() {
        Arrays.stream(ArtifactRegistry.values()).forEach(artifact -> ArtifactFactory.addBehavior(artifact.internalName, artifact.behavior));
    }
}
