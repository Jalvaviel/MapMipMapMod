package com.jalvaviel.config;

import com.jalvaviel.config.enums.InvisibleFrames;
import com.jalvaviel.config.enums.MapUpdates;
import com.jalvaviel.mixin.client.VideoOptionsScreenMixin;
import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.texture.MapTextureManager;
import net.minecraft.text.Text;

import java.util.Arrays;

import static com.jalvaviel.MapMipMapModClient.MAP_SIZE;
import static com.jalvaviel.config.MmmmGameOptions.*;

/** <h1>MmmmOptionScreen class</h1>
 * The option screen for MapMipMapMod without sodium. It gets called when the MapMipMapMod button is pressed in the vanilla's VideoOptionsScreen
 * @see VideoOptionsScreenMixin
 */
public class MmmmOptionScreen extends GameOptionsScreen {

    private static final MmmmOptionsStorage mmmmOpts = new MmmmOptionsStorage();

    /**
     * The option screen constructor.
     * @param parent the parent screen (previous screen).
     * @param gameOptions the gameOptions with most of the vanilla config. (I don't know why it's mandatory for any GameOptionsScreen children when addOptions exist).
     */
    public MmmmOptionScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable("tab.mapmipmapmod.general"));
    }

    /**
     * Adds the options for MapMipMapMod using vanilla's SimpleOption builders.
     * @see SimpleOption
     */
    @Override
    protected void addOptions() {
        // Map Mipmap Levels Option
        SimpleOption<Integer> mapmipmapLevels = new SimpleOption<>(
                "entry.mapmipmapmod.map_mipmap_levels",
                SimpleOption.constantTooltip(Text.translatable("tooltip.mapmipmapmod.map_mipmap_levels")),
                (optionText, value) -> {
                    Text textValue = value <= -1 ?
                        Text.translatable("entry.mapmipmapmod.auto") :
                        Text.literal(Integer.toString(value));
                    return Text.translatable("entry.mapmipmapmod.map_mipmap_levels").append(": "+textValue.getString());
                },
                new SimpleOption.ValidatingIntSliderCallbacks(-1, 8, false),
                mmmmOpts.getData().generalOptions.getLiteralMapmipmapLevels(),
                (value) -> {
                    mmmmOpts.getData().generalOptions.setMapmipmapLevels(value);
                    MinecraftClient.getInstance().getMapTextureManager().clear();
                });

        // Atlas Size Option
        SimpleOption<Integer> atlasSize = new SimpleOption<>(
                "entry.mapmipmapmod.atlas_size",
                SimpleOption.constantTooltip(Text.translatable("tooltip.mapmipmapmod.atlas_size")),
                (optionText, value) -> {
                    Text textValue = value <= 0 ?
                        Text.translatable("entry.mapmipmapmod.auto") :
                        Text.literal(value + "x" + value + " (" + (value * MAP_SIZE) + "x" + (value * MAP_SIZE) + "px)");
                    return Text.translatable("entry.mapmipmapmod.atlas_size").append(": "+textValue.getString());
                },
                new SimpleOption.ValidatingIntSliderCallbacks(0, 32, false),
                mmmmOpts.getData().generalOptions.getLiteralAtlasSize(),
                (value) -> {
                    mmmmOpts.getData().generalOptions.setAtlasSize(value);
                    MinecraftClient.getInstance().getMapTextureManager().clear();
                });

        // Depth Bias Option
        SimpleOption<Integer> depthBias = new SimpleOption<>(
                "entry.mapmipmapmod.depth_bias",
                SimpleOption.constantTooltip(Text.translatable("tooltip.mapmipmapmod.depth_bias")),
                (optionText, value) -> {
                    Text textValue = value <= -1 ?
                            Text.translatable("entry.mapmipmapmod.auto") :
                            Text.literal(Integer.toString(value));
                    return Text.translatable("entry.mapmipmapmod.depth_bias").append(": "+textValue.getString());
                },
                new SimpleOption.ValidatingIntSliderCallbacks(0, 8, false),
                mmmmOpts.getData().generalOptions.getDepthBias(),
                (value) -> {
                    mmmmOpts.getData().generalOptions.setDepthBias(value);
                });

        // Locked Map Updates Option
        SimpleOption<MapUpdates> lockedMapUpdates = new SimpleOption<>(
                "entry.mapmipmapmod.map_updates",
                value -> switch (value) {
                    case MapUpdates.ALL -> Tooltip.of(ALL_MAP_UPDATES_TOOLTIP);
                    case MapUpdates.ONLY_UNLOCKED -> Tooltip.of(ONLY_UNLOCKED_MAP_UPDATES_TOOLTIP);
                    case MapUpdates.NONE -> Tooltip.of(NONE_MAP_UPDATES_TOOLTIP);
                    default -> throw new IllegalStateException("Unexpected value: " + value);
                },
                SimpleOption.enumValueText(),
                new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(MapUpdates.values()), Codec.INT.xmap(MapUpdates::get, MapUpdates::getId)),
                mmmmOpts.getData().generalOptions.getMapUpdates(),
                (value) -> mmmmOpts.getData().generalOptions.setMapUpdates(value));


        // Invisible Item Frames Option
        SimpleOption<InvisibleFrames> invisibleFrames = new SimpleOption<>(
                "entry.mapmipmapmod.invisible_frames",
                value -> switch (value) {
                    case InvisibleFrames.DEFAULT -> Tooltip.of(DEFAULT_INVISIBLE_FRAMES_TOOLTIP);
                    case InvisibleFrames.ONLY_MAPS -> Tooltip.of(ONLY_MAPS_INVISIBLE_FRAMES_TOOLTIP);
                    case InvisibleFrames.ALL -> Tooltip.of(ALL_INVISIBLE_FRAMES_TOOLTIP);
                    default -> throw new IllegalStateException("Unexpected value: " + value);
                },
                SimpleOption.enumValueText(),
                new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(InvisibleFrames.values()), Codec.INT.xmap(InvisibleFrames::get, InvisibleFrames::getId)),
                mmmmOpts.getData().generalOptions.getInvisibleFrames(),
                (value) -> mmmmOpts.getData().generalOptions.setInvisibleFrames(value));


        // Add all options to the screen body with full width
        this.body.addSingleOptionEntry(mapmipmapLevels);
        this.body.addSingleOptionEntry(atlasSize);
        this.body.addSingleOptionEntry(depthBias);
        this.body.addSingleOptionEntry(lockedMapUpdates);
        this.body.addSingleOptionEntry(invisibleFrames);

    }

    /**
     * Callback called when the screen is closed.
     * It saves the running config to a file and refreshes all the maps rendered by the MapTextureManager.
     * @see MapTextureManager
     */
    @Override
    public void close() {
        mmmmOpts.save();
        MinecraftClient.getInstance().getMapTextureManager().clear();
        super.close();
    }
}
