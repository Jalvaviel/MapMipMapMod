package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import com.jalvaviel.config.enums.InvisibleFrames;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFrameRenderer.class)
public class ItemFrameRendererMixin {
    @ModifyExpressionValue(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockModelRenderState;isEmpty()Z"))
    private boolean shouldRenderItemFrame(boolean original, @Local LocalRef<ItemFrameRenderState> state) {
        return !original && (MapMipMapModClient.options().generalOptions.getInvisibleFrames() == InvisibleFrames.ALL
                || MapMipMapModClient.options().generalOptions.getInvisibleFrames() == InvisibleFrames.ONLY_MAPS && state.get().mapId != null);
    }


    @ModifyExpressionValue(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
    at = @At(value = "CONSTANT", args = "floatValue=0.4375F"))
    private float render(float org, @Local LocalRef<ItemFrameRenderState> state) {
        if (MapMipMapModClient.options().generalOptions.getInvisibleFrames() == InvisibleFrames.ALL
        || MapMipMapModClient.options().generalOptions.getInvisibleFrames() == InvisibleFrames.ONLY_MAPS && state.get().mapId != null) {
            return 0.5F;
        }
        return org;
    }
}
