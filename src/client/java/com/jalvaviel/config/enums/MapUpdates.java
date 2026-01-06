package com.jalvaviel.config.enums;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum MapUpdates implements GuiText {
    ALL(0, Text.translatable("entry.mapmipmapmod.map_updates_all"), Text.translatable("tooltip.mapmipmapmod.map_updates_all")),
    ONLY_UNLOCKED(1, Text.translatable("entry.mapmipmapmod.map_updates_only_unlocked"), Text.translatable("tooltip.mapmipmapmod.map_updates_only_unlocked")),
    NONE(2, Text.translatable("entry.mapmipmapmod.map_updates_none"), Text.translatable("tooltip.mapmipmapmod.map_updates_none"));

    private static final IntFunction<MapUpdates> BY_ID = ValueLists.createIndexToValueFunction(MapUpdates::getId, values(), ValueLists.OutOfBoundsHandling.WRAP);

    private final int id;
    private final Text name;
    private final Text tooltip;

    MapUpdates(final int id, final Text name, final Text tooltip) {
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
