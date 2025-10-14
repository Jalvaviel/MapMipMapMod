package com.jalvaviel.config.enums;

import net.minecraft.util.TranslatableOption;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum MapUpdates implements TranslatableOption {
    ALL(0, "entry.mapmipmapmod.map_updates_all"),
    ONLY_UNLOCKED(1, "entry.mapmipmapmod.map_updates_only_unlocked"),
    NONE(2, "entry.mapmipmapmod.map_updates_none");

    private static final IntFunction<MapUpdates> BY_ID = ValueLists.createIdToValueFunction(MapUpdates::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);

    private final int id;
    private final String name;

    MapUpdates(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.name;
    }

    public static MapUpdates get(int id) {
        return BY_ID.apply(id);
    }
}
