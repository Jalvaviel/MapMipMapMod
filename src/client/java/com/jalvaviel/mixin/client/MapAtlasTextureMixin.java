package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapAtlasTexture.class)
public class MapAtlasTextureMixin {
    @ModifyExpressionValue(method="<init>", at= @At(value = "CONSTANT", args = "intValue=4096"), remap = false)
    private int setSizeMapAtlasTexture(int original) {
        return MapMipMapModClient.ATLAS_SIZE;
    }

    @ModifyExpressionValue(method="getNextMapLocation", at= @At(value = "CONSTANT", args = "intValue=1024"), remap = false)
    private int setSizeGetNextMapLocation(int original) {
        return MapMipMapModClient.MAPS_PER_ATLAS;
    }
    @ModifyExpressionValue(method="getNextMapLocation", at= @At(value = "CONSTANT", args = "intValue=32"), remap = false)
    private int setOffsetGetNextMapLocation(int original) {
        return MapMipMapModClient.ATLAS_SIZE / 128;
    }
}
