package com.jalvaviel.mixin.immediatelyfast;

import com.bawnorton.mixinsquared.TargetHandler;
import com.jalvaviel.MapMipMapModClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
        value = {MapRenderer.class},
        priority = 1200
)
public abstract class MapRendererMixinSquared {
    /**
     * This squared mixin just overwrites the 4096 default atlas size in ImmediatelyFast when calculating the UVs
     * to draw the selected map texture.
     * @param original just the original value (unused).
     * @return the atlas size in the config.
     */
    @TargetHandler(mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapRenderer", name = "drawAtlasTexture")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "CONSTANT", args = "floatValue=4096.0F")) //
    public float onDrawAtlasTexture(float original){
        return MapMipMapModClient.options().generalOptions.getAtlasSize();
    }
}
