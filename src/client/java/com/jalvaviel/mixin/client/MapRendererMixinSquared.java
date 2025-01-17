package com.jalvaviel.mixin.client;

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
    @TargetHandler(mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapRenderer", name = "drawAtlasTexture")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "CONSTANT", args = "floatValue=4096.0F")) //
    public float onDrawAtlasTexture(float original){
        return MapMipMapModClient.ATLAS_SIZE;
    }
}
