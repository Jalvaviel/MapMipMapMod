package com.jalvaviel.mixin.client;

import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.function.Function;

import static net.minecraft.client.render.RenderPhase.*;

@Mixin(value = MapRenderer.MapTexture.class, priority = 1200)
public class MapTextureManagerMixin {
    /**
     * Mipmap shader for maps. Just a copy of the default one for maps with mipmaps enabled.
     */
    @Unique
    private static final Function<Identifier, RenderLayer> MAP_MIPMAP_LAYER = Util.memoize(texture -> RenderLayer.of("map_mipmap_layer",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
            VertexFormat.DrawMode.QUADS,
            786432,
            false,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(TEXT_PROGRAM)
                    .texture(new RenderPhase.Texture(texture, false,true))
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .lightmap(ENABLE_LIGHTMAP)
                    .build(true)));

    /**
     * Applies the shader function and saves it as the default renderLayer for maps.
     * @param identifier The map atlas identifier.
     * @return the custom renderLayer function applied and assigned.
     */
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getText(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer redirectRenderLayerGetText(Identifier identifier) {
        return MAP_MIPMAP_LAYER.apply(identifier);
    }
}
