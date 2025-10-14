package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import com.jalvaviel.config.MmmmOptionsStorage;
import com.jalvaviel.config.enums.InvisibleFrames;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameEntityRenderer.class)
public class ItemFrameEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/ItemFrameEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
    at = @At(value = "HEAD"))
    private void render(ItemFrameEntityRenderState itemFrameEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (MapMipMapModClient.options().generalOptions.getInvisibleFrames() == InvisibleFrames.ALL
        || MapMipMapModClient.options().generalOptions.getInvisibleFrames() == InvisibleFrames.ONLY_MAPS && itemFrameEntityRenderState.mapId != null)
            itemFrameEntityRenderState.invisible = true;
    }
}
