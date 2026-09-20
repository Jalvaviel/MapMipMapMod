package com.jalvaviel.config;

import com.jalvaviel.config.enums.InvisibleFrames;
import com.jalvaviel.config.enums.MapUpdates;
import com.jalvaviel.mixin.client.VideoSettingsScreenMixin;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.Options;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

import static com.jalvaviel.MapMipMapModClient.MAP_SIZE;

/** <h1>MmmmOptionScreen class</h1>
 * The option screen for MapMipMapMod without sodium. It gets called when the MapMipMapMod button is pressed in the vanilla's VideoOptionsScreen
 * @see VideoSettingsScreenMixin
 */
public class MmmmOptionScreen extends OptionsSubScreen {

    private static final MmmmOptionsStorage mmmmOpts = new MmmmOptionsStorage();

    /**
     * The option screen constructor.
     * @param parent the parent screen (previous screen).
     * @param gameOptions the gameOptions with most of the vanilla config. (I don't know why it's mandatory for any GameOptionsScreen children when addOptions exist).
     */
    public MmmmOptionScreen(Screen parent, Options gameOptions) {
        super(parent, gameOptions, Component.translatable("tab.mapmipmapmod.general"));
    }

    /**
     * Adds the options for MapMipMapMod using vanilla's SimpleOption builders.
     * @see OptionInstance
     */
    @Override
    protected void addOptions() {
        // Map Mipmap Levels Option
        OptionInstance<Integer> mapmipmapLevels = new OptionInstance<>(
                "entry.mapmipmapmod.map_mipmap_levels",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.mapmipmapmod.map_mipmap_levels")),
                (_, value) -> {
                    Component textValue = value <= -1 ?
                        Component.translatable("entry.mapmipmapmod.auto") :
                        Component.literal(Integer.toString(value));
                    return Component.translatable("entry.mapmipmapmod.map_mipmap_levels").append(": "+textValue.getString());
                },
                new OptionInstance.IntRange(-1, 8, false),
                mmmmOpts.getData().generalOptions.getLiteralMapmipmapLevels(),
                (value) -> {
                    mmmmOpts.getData().generalOptions.setMapmipmapLevels(value);
                    Minecraft.getInstance().getMapTextureManager().resetData();
                });

        // Atlas Size Option
        OptionInstance<Integer> atlasSize = new OptionInstance<>(
                "entry.mapmipmapmod.atlas_size",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.mapmipmapmod.atlas_size")),
                (_, value) -> {
                    Component textValue = value <= 0 ?
                        Component.translatable("entry.mapmipmapmod.auto") :
                        Component.literal(value + "x" + value + " (" + (value * MAP_SIZE) + "x" + (value * MAP_SIZE) + "px)");
                    return Component.translatable("entry.mapmipmapmod.atlas_size").append(": "+textValue.getString());
                },
                new OptionInstance.IntRange(0, 32, false),
                mmmmOpts.getData().generalOptions.getLiteralAtlasSize(),
                (value) -> {
                    mmmmOpts.getData().generalOptions.setAtlasSize(value);
                    Minecraft.getInstance().getMapTextureManager().resetData();
                });

        // Depth Bias Option
        OptionInstance<Integer> depthBias = new OptionInstance<>(
                "entry.mapmipmapmod.depth_bias",
                OptionInstance.cachedConstantTooltip(Component.translatable("tooltip.mapmipmapmod.depth_bias")),
                (_, value) -> {
                    Component textValue = value <= -1 ?
                            Component.translatable("entry.mapmipmapmod.auto") :
                            Component.literal(Integer.toString(value));
                    return Component.translatable("entry.mapmipmapmod.depth_bias").append(": "+textValue.getString());
                },
                new OptionInstance.IntRange(0, 8, false),
                mmmmOpts.getData().generalOptions.getDepthBias(),
                (value) -> mmmmOpts.getData().generalOptions.setDepthBias(value));

        // Locked Map Updates Option
        OptionInstance<MapUpdates> lockedMapUpdates = new OptionInstance<>(
                "entry.mapmipmapmod.map_updates",
                MapUpdates::getTooltip,
                (_, value) -> value.getName(),
                new OptionInstance.Enum<>(Arrays.asList(MapUpdates.values()), Codec.INT.xmap(MapUpdates::get, MapUpdates::getId)),
                mmmmOpts.getData().generalOptions.getMapUpdates(),
                (value) -> mmmmOpts.getData().generalOptions.setMapUpdates(value));


        // Invisible Item Frames Option
        OptionInstance<InvisibleFrames> invisibleFrames = new OptionInstance<>(
                "entry.mapmipmapmod.invisible_frames",
                InvisibleFrames::getTooltip,
                (_, value) -> value.getName(),
                new OptionInstance.Enum<>(Arrays.asList(InvisibleFrames.values()), Codec.INT.xmap(InvisibleFrames::get, InvisibleFrames::getId)),
                mmmmOpts.getData().generalOptions.getInvisibleFrames(),
                (value) -> mmmmOpts.getData().generalOptions.setInvisibleFrames(value));


        // Add all options to the screen body with full width
        assert this.list != null;
        this.list.addBig(mapmipmapLevels);
        this.list.addBig(atlasSize);
        this.list.addBig(depthBias);
        this.list.addBig(lockedMapUpdates);
        this.list.addBig(invisibleFrames);

    }

    /**
     * Callback called when the screen is closed.
     * It saves the running config to a file and refreshes all the maps rendered by the MapTextureManager.
     * @see MapTextureManager
     */
    @Override
    public void onClose() {
        mmmmOpts.save();
        Minecraft.getInstance().getMapTextureManager().resetData();
        super.onClose();
    }
}
