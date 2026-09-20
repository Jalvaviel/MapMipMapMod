package com.jalvaviel.mixin.client;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.jalvaviel.MapMipMapModClient.*;

@Mixin(Minecraft.class)
public class MinecraftAtlasSizeMixin {
    /**
     * Mixin that checks the OpenGl version of the GPU driver. If it's a very old version (OpenGl < 3.0), it stops
     * MapMipMapMod from working to prevent crashes.
     * @param ci the method callback (unused).
     */
    @Inject(method = "onResourceLoadFinished", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;onGameLoadFinished(Lnet/minecraft/client/Minecraft$GameLoadCookie;)V"))
    private void onFinishedLoadingAtlasSize(CallbackInfo ci) {
        String openGlVersion = Objects.requireNonNull(GL11.glGetString(GL11.GL_VERSION)).split(" ")[0];
        int majorVersion = Integer.parseInt(openGlVersion.split("\\.")[0]);
        if (majorVersion < 3) {
            OUTDATED_DRIVER = true;
            LOG.error("OpenGL version {} does not support native mipmap generation (>= v3.0).", openGlVersion);
            LOG.error("Consider updating your graphics card drivers if possible.");
            LOG.error("Disabling mipmaps for maps...");
        }
    }
}
