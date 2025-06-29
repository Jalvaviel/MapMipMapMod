package com.jalvaviel.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import static com.jalvaviel.MapMipMapModClient.MAP_SIZE;

/** <h1>MmmmOptionScreen class</h1>
 * The option screen for MapMipMapMod without sodium. It gets called when the MapMipMapMod button is pressed in the vanilla's VideoOptionsScreen
 * @see com.jalvaviel.mixin.client.VideoOptionsScreenMixin
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
                    MinecraftClient.getInstance().gameRenderer.getMapRenderer().clearStateTextures();
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
                    MinecraftClient.getInstance().gameRenderer.getMapRenderer().clearStateTextures();
                });

        // Locked Map Updates Option
        SimpleOption<Boolean> lockedMapUpdates = SimpleOption.ofBoolean(
                "entry.mapmipmapmod.locked_map_updates",
                SimpleOption.constantTooltip(Text.translatable("tooltip.mapmipmapmod.locked_map_updates")),
                mmmmOpts.getData().generalOptions.isLockedMapUpdates(),
                (value) -> mmmmOpts.getData().generalOptions.setLockedMapUpdates(value));

        // Add all options to the screen body with full width
        this.body.addSingleOptionEntry(mapmipmapLevels);
        this.body.addSingleOptionEntry(atlasSize);
        this.body.addSingleOptionEntry(lockedMapUpdates);
    }

    /**
     * Callback called when the screen is closed.
     * It saves the running config to a file and refreshes all the maps rendered by the MapTextureManager.
     */
    @Override
    public void close() {
        mmmmOpts.save();
        MinecraftClient.getInstance().gameRenderer.getMapRenderer().clearStateTextures();
        super.close();
    }
}
