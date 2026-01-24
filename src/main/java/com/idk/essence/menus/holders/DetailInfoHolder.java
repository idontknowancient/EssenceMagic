package com.idk.essence.menus.holders;

import lombok.Getter;

/**
 * Get detail info by click.
 */
public class DetailInfoHolder extends CancelHolder {

    @Getter private static final DetailInfoHolder instance = new DetailInfoHolder();

    protected DetailInfoHolder() {}
}
