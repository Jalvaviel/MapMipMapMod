package com.jalvaviel.mixin.client;

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
    @Unique
    private static final Function<Identifier, RenderLayer> MAP_MIPMAP_LAYER = Util.memoize(texture -> RenderLayer.of("mapmipmap",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
            VertexFormat.DrawMode.QUADS,
            786432,
            false,
            true,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(TEXT_PROGRAM)
                    .texture(new RenderPhase.Texture(texture, TriState.FALSE, true))
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .lightmap(ENABLE_LIGHTMAP)
                    .build(true)));

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getText(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer drawAtlasLayer(Identifier texture) {
        return MAP_MIPMAP_LAYER.apply(texture);
    }
}
