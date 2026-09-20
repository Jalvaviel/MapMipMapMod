package com.jalvaviel.mixin.immediatelyfast;

import com.bawnorton.mixinsquared.TargetHandler;
import com.jalvaviel.MapMipMapModClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {GuiGraphicsExtractor.class},
        priority = 1200
)
public class GuiGraphicsMixinSquared {
    @TargetHandler(mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinGuiGraphicsExtractor", name = "modifyTextureCoordinates")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "FIELD", target = "Lnet/raphimc/immediatelyfast/feature/map_atlas_generation/MapAtlasTexture;ATLAS_SIZE:I", opcode = Opcodes.GETSTATIC))
    public int modifyTextureCoordinates(int original){
        return MapMipMapModClient.options().generalOptions.getAtlasSize();
    }
}
