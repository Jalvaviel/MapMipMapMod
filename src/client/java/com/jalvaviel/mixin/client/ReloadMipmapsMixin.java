package com.jalvaviel.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

import static com.jalvaviel.MapMipMapModClient.updateAtlasSize;

@Mixin(MinecraftClient.class)
public class ReloadMipmapsMixin {
    /**
     * Sodium workaround that forces the atlas size and all atlases to be updated when the game's resources are reloaded.
     * (F3+T or by changing the mipmap level option on the game config menu).
     * @param cir async callback (unused).
     */
    @Inject(method = "reloadResources()Ljava/util/concurrent/CompletableFuture;",
            at = @At(value = "TAIL"))
    private void onReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> cir) { // FIXME, doesn't mix with sodium
        updateAtlasSize();
    }
}