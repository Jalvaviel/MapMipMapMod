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

import static com.jalvaviel.MapMipMapModClient.LOG;

@Mixin(NativeImageBackedTexture.class)
public class NativeImageBackedTextureMixin extends AbstractTexture {

    /**
     * Brittle workarounds to make the texture have mipmaps. If Mojang actually made a createTexture method with mipmap support, this wouldn't be necessary.
     * It checks for a match on the passed name, making it so if ImmediatelyFast decides to change it, this will fail.
     *
     * @param args The original arguments passed.
     */

    @ModifyArgs(method = "<init>(Ljava/lang/String;IIZ)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/lang/String;Lcom/mojang/blaze3d/textures/TextureFormat;III)Lcom/mojang/blaze3d/textures/GpuTexture;"))//value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/util/function/Supplier;Lcom/mojang/blaze3d/textures/TextureFormat;III)Lcom/mojang/blaze3d/textures/GpuTexture;"))
    private void onCreateTexture(Args args) {
        if (args.get(0).equals("ImmediatelyFast Map Atlas")) args.set(4,MapMipMapModClient.options().generalOptions.getMapmipmapLevels()+1);
    }
}
