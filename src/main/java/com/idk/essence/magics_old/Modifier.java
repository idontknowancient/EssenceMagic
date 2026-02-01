package com.idk.essence.magics_old;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Modifier {

    private final String name;

    private final List<String> info = new ArrayList<>();

    public Modifier(String name) {
        this.name = name;
    }
}
