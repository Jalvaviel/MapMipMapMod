package com.jalvaviel.config.sodium;

import com.google.common.collect.ImmutableList;
import com.jalvaviel.config.enums.InvisibleFrames;
import com.jalvaviel.config.enums.MapUpdates;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.CyclingControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static com.jalvaviel.MapMipMapModClient.MAP_SIZE;
import static com.jalvaviel.config.MmmmGameOptions.*;
import static com.jalvaviel.config.MmmmGameOptions.ALL_INVISIBLE_FRAMES_TOOLTIP;

/** <h1>MmmmOptionScreen class</h1>
 * The options screen for MapMipMapMod with sodium. It gets called when the MapMipMapMod tab is selected in the sodium's SodiumOptionsGUI screen.
 * @see com.jalvaviel.mixin.sodium.SodiumOptionsGuiMixin
 */
public class MmmmSodiumOptionScreen extends Screen {
    private static final MmmmSodiumOptionsStorage mmmmOpts = new MmmmSodiumOptionsStorage();

    public MmmmSodiumOptionScreen() {
        super(Text.translatable("tab.mapmipmapmod.general"));
    }

    /**
     * Just a text formatter to display -1 in mapMipMapLevels as "Auto"
     * @return the consumer with the Text depending if it is "Auto" or any other value.
     */
    static ControlValueFormatter mapMipMapLevels() {
        return (v) -> v == -1 ? Text.translatable("entry.mapmipmapmod.auto") : Text.literal(String.valueOf(v));
    }

    /**
     * Just a text formatter to display 0 in atlasSize as "Auto"
     * @return the consumer with the Text depending if it is "Auto" or any other value.
     */
    static ControlValueFormatter atlasSize() {
        return (v) -> v == 0 ? Text.translatable("entry.mapmipmapmod.auto") : Text.literal(v + "x" + v + " (" + v*MAP_SIZE + "x" + v*MAP_SIZE + "px)");
    }

    /**
     * Adds the options for MapMipMapMod using sodium's OptionGroups and OptionImpls builders.
     * @return an OptionPage representing the tab
     */
    public static OptionPage general() {
        List<OptionGroup> groups = new ArrayList<>();

        // Map Mipmap Levels Option
        groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(Integer.TYPE, mmmmOpts)
                .setName(Text.translatable("entry.mapmipmapmod.map_mipmap_levels"))
                .setTooltip(Text.translatable("tooltip.mapmipmapmod.map_mipmap_levels"))
                .setControl((option) ->
                        new SliderControl(option, -1, 8, 1, mapMipMapLevels()))
                .setBinding((options, value) -> {
                            options.generalOptions.setMapmipmapLevels(value);
                            MinecraftClient.getInstance().getMapTextureManager().clear();
                        },
                        (options) -> options.generalOptions.getLiteralMapmipmapLevels())
                .setImpact(OptionImpact.LOW)
                .build()).build());

        // Atlas Size Option
        groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(Integer.TYPE, mmmmOpts)
                .setName(Text.translatable("entry.mapmipmapmod.atlas_size"))
                .setTooltip(Text.translatable("tooltip.mapmipmapmod.atlas_size"))
                .setControl((option) ->
                        new SliderControl(option, 0, 32, 1, atlasSize()))
                .setBinding((options, value) -> {
                            options.generalOptions.setAtlasSize(value);
                            MinecraftClient.getInstance().getMapTextureManager().clear();
                        },
                        (options) -> options.generalOptions.getLiteralAtlasSize())
                .setImpact(OptionImpact.HIGH)
                .build()).build());

        // Depth Bias Option
        groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(Integer.TYPE, mmmmOpts)
                .setName(Text.translatable("entry.mapmipmapmod.depth_bias"))
                .setTooltip(Text.translatable("tooltip.mapmipmapmod.depth_bias"))
                .setControl((option) ->
                        new SliderControl(option, 0, 8, 1, mapMipMapLevels()))
                .setBinding((options, value) -> {
                            options.generalOptions.setDepthBias(value);
                        },
                        (options) -> options.generalOptions.getDepthBias())
                .setImpact(OptionImpact.LOW)
                .build()).build());

        // Locked Map Updates Option
        groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(MapUpdates.class, mmmmOpts)
                .setName(Text.translatable("entry.mapmipmapmod.map_updates"))
                .setTooltip(value -> switch (value) {
                    case MapUpdates.ALL -> ALL_MAP_UPDATES_TOOLTIP;
                    case MapUpdates.ONLY_UNLOCKED -> ONLY_UNLOCKED_MAP_UPDATES_TOOLTIP;
                    case MapUpdates.NONE -> NONE_MAP_UPDATES_TOOLTIP;
                    default -> throw new IllegalStateException("Unexpected value: " + value);
                })
                .setControl((option) ->
                        new CyclingControl<>(option, MapUpdates.class,
                                new Text[]{Text.translatable("entry.mapmipmapmod.map_updates_all"),
                                        Text.translatable("entry.mapmipmapmod.map_updates_only_unlocked"),
                                        Text.translatable("entry.mapmipmapmod.map_updates_none")}))
                .setBinding((opts, value) ->
                                opts.generalOptions.setMapUpdates(value),
                        (opts) -> opts.generalOptions.getMapUpdates())
                .setImpact(OptionImpact.HIGH)
                .build()).build());

        // Invisible Item Frames Option
        groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(InvisibleFrames.class, mmmmOpts)
                .setName(Text.translatable("entry.mapmipmapmod.invisible_frames"))
                .setTooltip(value -> switch (value) {
                    case InvisibleFrames.DEFAULT -> DEFAULT_INVISIBLE_FRAMES_TOOLTIP;
                    case InvisibleFrames.ONLY_MAPS -> ONLY_MAPS_INVISIBLE_FRAMES_TOOLTIP;
                    case InvisibleFrames.ALL -> ALL_INVISIBLE_FRAMES_TOOLTIP;
                    default -> throw new IllegalStateException("Unexpected value: " + value);
                })
                .setControl((option) ->
                        new CyclingControl<>(option, InvisibleFrames.class,
                                new Text[]{Text.translatable("entry.mapmipmapmod.invisible_frames_default"),
                                        Text.translatable("entry.mapmipmapmod.invisible_frames_only_maps"),
                                        Text.translatable("entry.mapmipmapmod.invisible_frames_all")}))
                .setBinding((opts, value) ->
                                opts.generalOptions.setInvisibleFrames(value),
                        (opts) -> opts.generalOptions.getInvisibleFrames())
                .build()).build());

        return new OptionPage(Text.translatable("tab.mapmipmapmod.general"), ImmutableList.copyOf(groups));
    }
}