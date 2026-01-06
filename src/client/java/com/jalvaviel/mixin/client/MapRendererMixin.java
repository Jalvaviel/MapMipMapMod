package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.OptionalDouble;
import java.util.function.Function;

@Mixin(value = MapRenderer.class, priority = 1200)
public abstract class MapRendererMixin {

    /**
     * New layering for the shader below to avoid Z fighting with item frame texture.
     */
    @Unique
    private static final LayeringTransform VIEW_OFFSET_Z_LAYERING_MAPS = new LayeringTransform("view_offset_z_layering_maps",
            matrices -> RenderSystem.getProjectionType().apply(matrices,MapMipMapModClient.options().generalOptions.getDepthBias()));

    /**
     * Mipmap shader for maps. Just a copy of the default one for maps with mipmaps enabled.
     */
    @Unique
    private static final Function<Identifier, RenderLayer> MAP_MIPMAP_LAYER = Util.memoize(
            texture -> RenderLayer.of(
                    "map_mipmap_layer",
                    RenderSetup.builder(RenderPipelines.RENDERTYPE_TEXT)
                            .layeringTransform(VIEW_OFFSET_Z_LAYERING_MAPS)
                            .texture("Sampler0", texture, () -> RenderSystem.getDevice().createSampler(AddressMode.REPEAT,AddressMode.REPEAT,FilterMode.NEAREST,FilterMode.NEAREST,1,OptionalDouble.empty())) // Ignore this, the sampler uses the correct filters on another mixin () -> RenderSystem.getDevice().createSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.LINEAR, 1, OptionalDouble.empty()))//("SamplerM8", new RenderSetup.Texture(MinecraftClient.getInstance().getTextureManager().getTexture(texture).getGlTextureView(),RenderSystem.getSamplerCache().get(FilterMode.LINEAR)))
                            .useLightmap()
                            .expectedBufferSize(786432)
                            .build()
            )
    );


    /**
     * Applies the shader with the mipmap support when rendering the maps on the world.
     * @param texture the map atlas identifier.
     * @return the shader program.
     */
    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayers;text(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer drawAtlasLayer(Identifier texture) {
        return MAP_MIPMAP_LAYER.apply(texture);
    }
}
