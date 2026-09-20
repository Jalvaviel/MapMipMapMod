package com.jalvaviel.config.enums;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum MapUpdates implements GuiText {
    ALL(0, Component.translatable("entry.mapmipmapmod.map_updates_all"), Component.translatable("tooltip.mapmipmapmod.map_updates_all")),
    ONLY_UNLOCKED(1, Component.translatable("entry.mapmipmapmod.map_updates_only_unlocked"), Component.translatable("tooltip.mapmipmapmod.map_updates_only_unlocked")),
    NONE(2, Component.translatable("entry.mapmipmapmod.map_updates_none"), Component.translatable("tooltip.mapmipmapmod.map_updates_none"));

    private static final IntFunction<MapUpdates> BY_ID = ByIdMap.continuous(MapUpdates::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);

    private final int id;
    private final Component name;
    private final Component tooltip;

    MapUpdates(final int id, final Component name, final Component tooltip) {
        this.id = id;
        this.name = name;
        this.tooltip = tooltip;
    }

    public static MapUpdates get(int id) {
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
