package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DynamicTexture.class)
public class DynamicTextureMixin extends AbstractTexture {

    /**
     * Brittle workarounds to make the texture have mipmaps. If Mojang actually made a createTexture method with mipmap support this wouldn't be necessary.
     * It checks for a match on the passed name, making it so if ImmediatelyFast decides to change it, this will fail.
     *
     * @param name The name of the texture. If it isn't from ImmediatelyFast, it ignores it.
     * @param args The original arguments passed.
     */

    @ModifyArgs(method = "createTexture(Ljava/lang/String;)V", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/GpuDevice;createTexture(Ljava/lang/String;ILcom/mojang/blaze3d/textures/TextureFormat;IIII)Lcom/mojang/blaze3d/textures/GpuTexture;"))
    private void onCreateTexture(Args args, String name) {
        if (name.equals("ImmediatelyFast Map Atlas")) {
            args.set(6,MapMipMapModClient.options().generalOptions.getMapmipmapLevels()+1);
        }
    }

    @ModifyArgs(method = "createTexture(Ljava/lang/String;)V", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/SamplerCache;getRepeat(Lcom/mojang/blaze3d/textures/FilterMode;)Lcom/mojang/blaze3d/textures/GpuSampler;"))
    private void onSetTextureFilter(Args args, String name) {
        if (name.equals("ImmediatelyFast Map Atlas")) {
            args.set(0, FilterMode.LINEAR);
        }
    }
}
