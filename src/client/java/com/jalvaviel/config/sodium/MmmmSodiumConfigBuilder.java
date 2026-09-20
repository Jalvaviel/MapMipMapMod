package com.jalvaviel.config.sodium;

import com.jalvaviel.config.enums.InvisibleFrames;
import com.jalvaviel.config.enums.MapUpdates;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.option.ControlValueFormatter;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.ModOptionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Collection;

import static com.jalvaviel.MapMipMapModClient.MAP_SIZE;

public class MmmmSodiumConfigBuilder implements ConfigEntryPoint {
    private final MmmmSodiumOptionsStorage mmmmOpts = new MmmmSodiumOptionsStorage();
    private final StorageEventHandler handler = mmmmOpts::save;
    private static final Identifier RELOAD_MAP_TEXTURES = Identifier.fromNamespaceAndPath("mapmipmapmod","reload_map_textures");

    private static ControlValueFormatter mapMipMapLevels() {
        return (v) -> v == -1 ? Component.translatable("entry.mapmipmapmod.auto") : Component.literal(String.valueOf(v));
    }

    static ControlValueFormatter atlasSize() {
        return (v) -> v == 0 ? Component.translatable("entry.mapmipmapmod.auto") : Component.literal(v + "x" + v + " (" + v*MAP_SIZE + "x" + v*MAP_SIZE + "px)");
    }

    static ControlValueFormatter depthBias() {
        return (v) -> Component.literal(String.valueOf(v));
    }

    static void reloadMapTextureManager(Collection<Identifier> flags) {
        if (flags.contains(RELOAD_MAP_TEXTURES)) {
            Minecraft.getInstance().getMapTextureManager().resetData();
        }
    }

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        ModOptionsBuilder modOpts = builder.registerOwnModOptions();
        modOpts.registerFlagHook((flags, _) -> reloadMapTextureManager(flags), RELOAD_MAP_TEXTURES);
        modOpts.setIcon(Identifier.fromNamespaceAndPath("mapmipmapmod","icon.png"))
                .addPage(builder.createOptionPage()
                        .setName(Component.translatable("settinggroup.mapmipmapmod.general"))
                        .addOptionGroup(builder.createOptionGroup()
                                .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("mapmipmapmod","mipmap_levels"))
                                        .setName(Component.translatable("entry.mapmipmapmod.map_mipmap_levels"))
                                        .setTooltip(Component.translatable("tooltip.mapmipmapmod.map_mipmap_levels"))
                                        .setStorageHandler(handler)
                                        .setBinding(mmmmOpts.getData().generalOptions::setMapmipmapLevels, mmmmOpts.getData().generalOptions::getLiteralMapmipmapLevels)
                                        .setDefaultValue(-1)
                                        .setRange(-1,8,1)
                                        .setImpact(OptionImpact.LOW)
                                        .setFlags(RELOAD_MAP_TEXTURES)
                                        .setValueFormatter(mapMipMapLevels()))
                                .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("mapmipmapmod","atlas_size"))
                                        .setName(Component.translatable("entry.mapmipmapmod.atlas_size"))
                                        .setTooltip(Component.translatable("tooltip.mapmipmapmod.atlas_size"))
                                        .setStorageHandler(handler)
                                        .setBinding(mmmmOpts.getData().generalOptions::setAtlasSize, mmmmOpts.getData().generalOptions::getLiteralAtlasSize)
                                        .setDefaultValue(0)
                                        .setRange(0,32,1)
                                        .setImpact(OptionImpact.HIGH)
                                        .setFlags(RELOAD_MAP_TEXTURES)
                                        .setValueFormatter(atlasSize()))
                                .addOption(builder.createIntegerOption(Identifier.fromNamespaceAndPath("mapmipmapmod","depth_bias"))
                                        .setName(Component.translatable("entry.mapmipmapmod.depth_bias"))
                                        .setTooltip(Component.translatable("tooltip.mapmipmapmod.depth_bias"))
                                        .setStorageHandler(handler)
                                        .setBinding(mmmmOpts.getData().generalOptions::setDepthBias, mmmmOpts.getData().generalOptions::getDepthBias)
                                        .setDefaultValue(1)
                                        .setValueFormatter(depthBias())
                                        .setImpact(OptionImpact.LOW)
                                        .setRange(0,8,1)))
                                .addOption(builder.createEnumOption(Identifier.fromNamespaceAndPath("mapmipmapmod","map_updates"), MapUpdates.class)
                                        .setName(Component.translatable("entry.mapmipmapmod.map_updates"))
                                        .setTooltip(MapUpdates::getTextTooltip)
                                        .setStorageHandler(handler)
                                        .setBinding(mmmmOpts.getData().generalOptions::setMapUpdates, mmmmOpts.getData().generalOptions::getMapUpdates)
                                        .setElementNameProvider(MapUpdates::getName)
                                        .setImpact(OptionImpact.HIGH)
                                        .setDefaultValue(MapUpdates.ONLY_UNLOCKED))
                                .addOption(builder.createEnumOption(Identifier.fromNamespaceAndPath("mapmipmapmod","invisible_frames"), InvisibleFrames.class)
                                        .setName(Component.translatable("entry.mapmipmapmod.invisible_frames"))
                                        .setTooltip(InvisibleFrames::getTextTooltip)
                                        .setStorageHandler(handler)
                                        .setBinding(mmmmOpts.getData().generalOptions::setInvisibleFrames, mmmmOpts.getData().generalOptions::getInvisibleFrames)
                                        .setElementNameProvider(InvisibleFrames::getName)
                                        .setDefaultValue(InvisibleFrames.DEFAULT)));
    }
}
