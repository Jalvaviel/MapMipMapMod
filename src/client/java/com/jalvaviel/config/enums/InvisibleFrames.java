package com.jalvaviel.config.enums;

import net.minecraft.util.TranslatableOption;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum InvisibleFrames implements TranslatableOption {
    DEFAULT(0, "entry.mapmipmapmod.invisible_frames_default"),
    ONLY_MAPS(1, "entry.mapmipmapmod.invisible_frames_only_maps"),
    ALL(2, "entry.mapmipmapmod.invisible_frames_all");


    private static final IntFunction<InvisibleFrames> BY_ID = ValueLists.createIndexToValueFunction(InvisibleFrames::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);

    private final int id;
    private final String name;

    InvisibleFrames(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.name;
    }

    public static InvisibleFrames get(int id) {
        return BY_ID.apply(id);
    }
}
