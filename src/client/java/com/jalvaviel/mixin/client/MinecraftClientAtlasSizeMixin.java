package com.jalvaviel.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jalvaviel.MapMipMapModClient.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientAtlasSizeMixin {
    /**
     * Mixin that checks the OpenGl version of the GPU driver. If it's a very old version (OpenGl < 3.0), it stops
     * MapMipMapMod from working to prevent crashes.
     * @param ci the method callback (unused).
     */
    @Inject(method = "onFinishedLoading", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;collectLoadTimes(Lnet/minecraft/client/MinecraftClient$LoadingContext;)V"))
    private void onFinishedLoadingAtlasSize(CallbackInfo ci) {
        String openGlVersion = GL11.glGetString(GL11.GL_VERSION).split(" ")[0];
        int majorVersion = Integer.parseInt(openGlVersion.split("\\.")[0]);
        if (majorVersion < 3) {
            OUTDATED_DRIVER = true;
            LOG.error("OpenGL version {} does not support native mipmap generation (>= v3.0).", openGlVersion);
            LOG.error("Consider updating your graphics card drivers if possible.");
            LOG.error("Disabling mipmaps for maps...");
        }
    }
}
