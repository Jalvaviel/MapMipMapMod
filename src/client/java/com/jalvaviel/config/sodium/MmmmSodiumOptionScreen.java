package com.jalvaviel.config.sodium;

import com.google.common.collect.ImmutableList;
import net.caffeinemc.mods.sodium.client.gui.options.*;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static com.jalvaviel.MapMipMapModClient.MAP_SIZE;

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
                            MinecraftClient.getInstance().gameRenderer.getMapRenderer().clearStateTextures();
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
                            MinecraftClient.getInstance().gameRenderer.getMapRenderer().clearStateTextures();
                        },
                        (options) -> options.generalOptions.getLiteralAtlasSize())
                .setImpact(OptionImpact.HIGH)
                .build()).build());

        // Locked Map Updates Option
        groups.add(OptionGroup.createBuilder().add(OptionImpl.createBuilder(Boolean.TYPE, mmmmOpts)
                .setName(Text.translatable("entry.mapmipmapmod.locked_map_updates"))
                .setTooltip(Text.translatable("tooltip.mapmipmapmod.locked_map_updates"))
                .setControl(TickBoxControl::new)
                .setImpact(OptionImpact.HIGH)
                .setBinding((options, value) -> options.generalOptions.setLockedMapUpdates(value),
                        (options) -> options.generalOptions.isLockedMapUpdates())
                .build()).build());

        return new OptionPage(Text.translatable("tab.mapmipmapmod.general"), ImmutableList.copyOf(groups));
    }
}