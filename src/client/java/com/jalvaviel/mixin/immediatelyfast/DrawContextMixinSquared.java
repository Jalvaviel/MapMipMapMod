package com.jalvaviel.mixin.immediatelyfast;

import com.bawnorton.mixinsquared.TargetHandler;
import com.jalvaviel.MapMipMapModClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.DrawContext;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = DrawContext.class, priority = 1200)
public abstract class DrawContextMixinSquared {
    /**
     * GUI maps (including map-in-slot) use a separate atlas UV calculation from
     * world maps. All four UV bounds must use the configured atlas dimensions.
     */
    @TargetHandler(mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinGuiGraphics", name = "modifyTextureCoordinates")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "FIELD", target = "Lnet/raphimc/immediatelyfast/feature/map_atlas_generation/MapAtlasTexture;ATLAS_SIZE:I", opcode = Opcodes.GETSTATIC), require = 4)
    private int mapmipmapmod$guiAtlasSize(int original) {
        return MapMipMapModClient.options().generalOptions.getAtlasSize();
    }
}
