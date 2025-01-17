package com.jalvaviel.mixin.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.jalvaviel.MapMipMapModClient;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.MapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(
        value = {MapTextureManager.MapTexture.class},
        priority = 1200
)
public class MapTextureMixinSquared {
    @Shadow
    private MapAtlasTexture immediatelyFast$atlasTexture;

    @TargetHandler(
            mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapTextureManager_MapTexture",
            name = "updateAtlasTexture"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImageBackedTexture;bindTexture()V")) // OK
    public void updateAtlasTexture(CallbackInfo ci) {
        if (!MapMipMapModClient.OUTDATED_DRIVER) {
            NativeImageBackedTexture atlasTexture = this.immediatelyFast$atlasTexture.getTexture();
            NativeImage atlasImage = atlasTexture.getImage();
            int mipmapValue = MinecraftClient.getInstance().options.getMipmapLevels().getValue();
            TextureUtil.prepareImage(atlasTexture.getGlId(), mipmapValue, atlasImage.getWidth(), atlasImage.getHeight());
            atlasTexture.upload();
            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        }
    }
}
