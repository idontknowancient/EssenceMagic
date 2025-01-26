package com.idk.essencemagic.skills;

public enum Trigger {

    LEFT_CLICK("left_click"),
    SHIFT_LEFT_CLICK("shift_left_click"),
    RIGHT_CLICK("right_click"),
    SHIFT_RIGHT_CLICK("shift_right_click"),
    ATTACK("attack"),
    ;

    public final String name;

    Trigger(String name) {
        this.name = name;
    }
}
