package com.jalvaviel.mixin.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.jalvaviel.MapMipMapModClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.*;
import net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapRenderer_MapTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
        value = {MapRenderer.MapTexture.class},
        priority = 1200
)
public abstract class MapRendererMixinSquared {
    /**
     * This squared mixin just overwrites the 4096 default atlas size in ImmediatelyFast when calculating the UVs
     * to draw the selected map texture.
     * @param original just the original value (unused).
     * @return the atlas size in the config.
     */
    @TargetHandler(mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapRenderer_MapTexture", name = "drawAtlasTexture")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "CONSTANT", args = "floatValue=4096.0F")) //
    public float onDrawAtlasTexture(float original){
        return MapMipMapModClient.ATLAS_SIZE;
    }
}
