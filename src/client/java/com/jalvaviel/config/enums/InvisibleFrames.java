package com.jalvaviel.config.enums;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum InvisibleFrames implements GuiText {
    DEFAULT(0, Component.translatable("entry.mapmipmapmod.invisible_frames_default"), Component.translatable("tooltip.mapmipmapmod.invisible_frames_default")),
    ONLY_MAPS(1, Component.translatable("entry.mapmipmapmod.invisible_frames_only_maps"), Component.translatable("tooltip.mapmipmapmod.invisible_frames_only_maps")),
    ALL(2, Component.translatable("entry.mapmipmapmod.invisible_frames_all"), Component.translatable("tooltip.mapmipmapmod.invisible_frames_all"));

    private static final IntFunction<InvisibleFrames> BY_ID = ByIdMap.continuous(InvisibleFrames::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    private final int id;
    private final Component name;
    private final Component tooltip;

    InvisibleFrames(final int id, final Component name, final Component tooltip) {
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
    public Component getName() {
        return name;
    }

    @Override
    public Component getTextTooltip() {
        return tooltip;
    }

    @Override
    public Tooltip getTooltip() {
        return Tooltip.create(tooltip);
    }
}
