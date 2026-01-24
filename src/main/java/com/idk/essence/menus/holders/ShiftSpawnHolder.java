package com.idk.essence.menus.holders;

import lombok.Getter;

/**
 * Spawn mobs from menus by shift-click.
 */
public class ShiftSpawnHolder extends CancelHolder {

    @Getter private static final ShiftSpawnHolder instance = new ShiftSpawnHolder();

    private ShiftSpawnHolder() {}
}
