package com.jalvaviel.config.enums;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum InvisibleFrames implements GuiText {
    DEFAULT(0, Text.translatable("entry.mapmipmapmod.invisible_frames_default"), Text.translatable("tooltip.mapmipmapmod.invisible_frames_default")),
    ONLY_MAPS(1, Text.translatable("entry.mapmipmapmod.invisible_frames_only_maps"), Text.translatable("tooltip.mapmipmapmod.invisible_frames_only_maps")),
    ALL(2, Text.translatable("entry.mapmipmapmod.invisible_frames_all"),Text.translatable("tooltip.mapmipmapmod.invisible_frames_all"));

    private static final IntFunction<InvisibleFrames> BY_ID = ValueLists.createIndexToValueFunction(InvisibleFrames::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);

    private final int id;
    private final Text name;
    private final Text tooltip;

    InvisibleFrames(final int id, final Text name, final Text tooltip) {
        this.id = id;
        this.name = name;
        this.tooltip = tooltip;
    }

    public static InvisibleFrames get(int id) {
        return BY_ID.apply(id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Text getName() {
        return name;
    }

    @Override
    public Text getTextTooltip() {
        return tooltip;
    }

    @Override
    public Tooltip getTooltip() {
        return Tooltip.of(tooltip);
    }
}
