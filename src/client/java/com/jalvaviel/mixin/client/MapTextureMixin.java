package com.jalvaviel.mixin.client;

import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.block.MapColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.item.map.MapState;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

import static net.minecraft.client.render.RenderPhase.*;

/**
 * This is the main mixin of this mod.
 */
@Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture")
public class MapTextureMixin {

    @Shadow
    private MapState state;

    @Mutable
    @Final
    @Shadow
    private NativeImageBackedTexture texture;

    @Mutable
    @Final
    @Shadow
    private RenderLayer renderLayer;

    @Unique
    public Identifier identifier;

    /**
     * The function tasked to render the mipmapped maps. It's a basic copy of the original "text" function used to render
     * text and map textures, but it allows mipmaps on its textures.
     */
    private static final Function<Identifier, RenderLayer> MAP_MIPMAP = Util.memoize(texture -> RenderLayer.of("mapmipmap",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
            VertexFormat.DrawMode.QUADS,
            786432,
            false,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(TEXT_PROGRAM)
                    .texture(new RenderPhase.Texture(texture, false, true))
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .lightmap(ENABLE_LIGHTMAP)
                    .build(true)));


    /**
     * The MapTexture constructor. Here it stores the identifier of each map in the same object,
     * since the original code only uses it to register the original texture.
     * This effectively registers again the dynamic map texture.
     * @param mapRenderer The MapRenderer associated with the MapTexture
     * @param id the numerical id of the map
     * @param state the MapState
     * @param ci callback (unused)
     */
    @Inject(method= "<init>", at = @At("TAIL"))
    private void onMapTexture(MapRenderer mapRenderer, int id, MapState state, CallbackInfo ci){
        this.identifier = mapRenderer.textureManager.registerDynamicTexture("map/" + id, this.texture);
    }

    /**
     * @author Jalvaviel
     * @reason Updated to add mipmapping to map render.
     * When updateTexture is called, mipmaps for it are generated based on the mipmap video config.
     * Then, they're uploaded to blaze3D and rendered by the MAP_MIPMAP function above.
     */
    @Overwrite
    public void updateTexture() {
        for (int i = 0; i < 128; ++i) {
            for (int j = 0; j < 128; ++j) {
                int k = j + i * 128;
                this.texture.getImage().setColor(j, i, MapColor.getRenderColor((int) this.state.colors[k]));
            }
        }
        texture.setFilter(false,true);
        int mipmapValue = MinecraftClient.getInstance().options.getMipmapLevels().getValue();
        TextureUtil.prepareImage(texture.getGlId(),mipmapValue,texture.getImage().getWidth(),texture.getImage().getHeight());
        SpriteContents spriteContents = new SpriteContents(this.identifier, new SpriteDimensions(texture.getImage().getWidth(), texture.getImage().getHeight()),  texture.getImage(), ResourceMetadata.NONE);
        spriteContents.generateMipmaps(mipmapValue);
        spriteContents.upload(0,0);
        this.renderLayer = MAP_MIPMAP.apply(spriteContents.getId());
    }
}
