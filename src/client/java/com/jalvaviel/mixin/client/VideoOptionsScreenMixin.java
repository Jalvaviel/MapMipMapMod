package com.jalvaviel.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin refreshes the maps rendering when the mipmap config is refreshed, so you don't need to leave the game to update them.
 */
@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin {
	@Inject(method = "removed",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/MinecraftClient;reloadResourcesConcurrently()Ljava/util/concurrent/CompletableFuture;"))
	private void onRemoved(CallbackInfo info) {
		MinecraftClient.getInstance().gameRenderer.getMapRenderer().clearStateTextures();
	}
}
