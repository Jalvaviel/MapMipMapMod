package com.jalvaviel.mixin.immediatelyfast;

import com.jalvaviel.MapMipMapModClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import static com.jalvaviel.MapMipMapModClient.MAP_SIZE;

/**
 * This mixin just overwrites all the instances of the static constant final on ImmediatelyFast for the right atlas size
 * in MapMipMapMod config. 4096 is a very high value that can cause instability when loading lots of maps, specially with
 * mipmaps.
 * Also, this relies on ImmediatelyFast to not change these values, or this will break.
 */
@Mixin(MapAtlasTexture.class)
public class MapAtlasTextureMixin {

    @ModifyExpressionValue(method="<init>", at= @At(value = "CONSTANT", args = "intValue=4096"), remap = false)
    private int setSizeMapAtlasTexture(int original) {
        return MapMipMapModClient.options().generalOptions.getAtlasSize();
    }
    @ModifyExpressionValue(method="getNextMapLocation", at= @At(value = "CONSTANT", args = "intValue=1024"), remap = false)
    private int setSizeGetNextMapLocation(int original) {
        int literalAtlasSize = MapMipMapModClient.options().generalOptions.getAtlasSize() / MAP_SIZE;
        return literalAtlasSize*literalAtlasSize;
    }

    @ModifyExpressionValue(method="getNextMapLocation", at= @At(value = "CONSTANT", args = "intValue=32"), remap = false)
    private int setOffsetGetNextMapLocation(int original) {
        return MapMipMapModClient.options().generalOptions.getAtlasSize() / MAP_SIZE;
    }
}
