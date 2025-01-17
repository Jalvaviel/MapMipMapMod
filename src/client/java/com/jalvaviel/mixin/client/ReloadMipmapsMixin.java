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

    @Inject(method = "reloadResources()Ljava/util/concurrent/CompletableFuture;",
            at = @At(value = "TAIL"))
    private void onReloadResources(CallbackInfoReturnable<CompletableFuture<Void>> cir) { // FIXME, doesn't mix with sodium
        updateAtlasSize();
    }
    /*
    @ModifyArg(method = "reloadResources(ZLnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/util/concurrent/CompletableFuture;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"))
    private Overlay onReloadResources(Overlay overlay) { // FIXME, doesn't mix with sodium
        updateAtlasSize();
        return overlay;
    }
     */
}