package com.idk.essence.menus.holders;

import lombok.Getter;

/**
 * Get items from menus ny click.
 */
public class GetItemHolder extends CancelHolder {

    @Getter private static final GetItemHolder instance = new GetItemHolder();

    private GetItemHolder() {}
}
