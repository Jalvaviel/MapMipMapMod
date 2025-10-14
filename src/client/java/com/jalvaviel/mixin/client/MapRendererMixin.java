package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

import static net.minecraft.client.render.RenderPhase.*;

@Mixin(value = MapRenderer.class, priority = 1200)
public abstract class MapRendererMixin {

    /**
     * New layering for the shader below to avoid Z fighting with item frame texture.
     */
    @Unique
    private static final Layering VIEW_OFFSET_Z_LAYERING_MAPS = new Layering("view_offset_z_layering", () -> {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        RenderSystem.getProjectionType().apply(matrix4fStack, MapMipMapModClient.options().generalOptions.getDepthBias());
    }, () -> {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.popMatrix();
    });

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
                    .layering(VIEW_OFFSET_Z_LAYERING_MAPS)
                    .program(TEXT_PROGRAM)
                    .texture(new RenderPhase.Texture(texture, TriState.FALSE, true))
                    .transparency(TRANSLUCENT_TRANSPARENCY)
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
