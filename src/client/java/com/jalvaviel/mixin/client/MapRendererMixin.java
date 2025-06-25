package com.jalvaviel.mixin.client;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

import static net.minecraft.client.render.RenderPhase.*;

@Mixin(value = MapRenderer.class, priority = 1200)
public abstract class MapRendererMixin {
    /**
     * Mipmap shader for maps. Just a copy of the default one for maps with mipmaps enabled.
     */
    @Unique
    private static final Function<Identifier, RenderLayer> MAP_MIPMAP_LAYER = Util.memoize((texture) ->
            RenderLayer.of("map_mipmap_layer",
                    786432,
                    false,
                    true,
                    RenderPipelines.RENDERTYPE_TEXT,
                    RenderLayer.MultiPhaseParameters.builder()
                            .texture(new RenderPhase.Texture(texture, TriState.FALSE, true))
                            .lightmap(ENABLE_LIGHTMAP)
                            .build(true)));

    /**
     * Applies the shader with the mipmap support when rendering the maps on the world.
     * @param texture the map atlas identifier.
     * @return the shader program.
     */
    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getText(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer drawAtlasLayer(Identifier texture) {
        return MAP_MIPMAP_LAYER.apply(texture);
    }
}
