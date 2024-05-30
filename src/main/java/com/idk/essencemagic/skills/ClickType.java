package com.idk.essencemagic.skills;

public enum ClickType {

    LEFT_CLICK("left_click"),
    SHIFT_LEFT_CLICK("shift_left_click"),
    RIGHT_CLICK("right_click"),
    SHIFT_RIGHT_CLICK("shift_right_click"),
    ;

    public final String name;

    ClickType(String name) {
        this.name = name;
    }
}
