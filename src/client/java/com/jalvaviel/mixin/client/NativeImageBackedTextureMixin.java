package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(NativeImageBackedTexture.class)
public class NativeImageBackedTextureMixin extends AbstractTexture {

    /**
     * Brittle workarounds to make the texture have mipmaps. If Mojang actually made a createTexture method with mipmap support this wouldn't be necessary.
     * It checks for a match on the passed name, making it so if ImmediatelyFast decides to change it, this will fail.
     *
     * @param name The name of the texture. If it isn't from ImmediatelyFast, it ignores it.
     * @param args The original arguments passed.
     */

    @ModifyArgs(method = "createTexture(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/lang/String;ILcom/mojang/blaze3d/textures/TextureFormat;IIII)Lcom/mojang/blaze3d/textures/GpuTexture;"))
    private void onCreateTexture(Args args, String name) {
        if (name.equals("ImmediatelyFast Map Atlas")) args.set(6,MapMipMapModClient.options().generalOptions.getMapmipmapLevels()+1);
    }

    @ModifyArgs(method = "createTexture(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/textures/GpuTexture;setTextureFilter(Lcom/mojang/blaze3d/textures/FilterMode;Z)V"))
    private void onSetTextureFilter(Args args, String name) {
        if (name.equals("ImmediatelyFast Map Atlas")) args.set(1,true);
    }
}
