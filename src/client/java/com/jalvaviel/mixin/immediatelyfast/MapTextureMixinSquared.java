package com.jalvaviel.mixin.immediatelyfast;

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

    /**
     * This is the squared mixin that prepares the atlas textures to get mipmapped. It uploads the atlas texture to the graphics library,
     * to be rendered on screen later. The last line is a direct call to OpenGl to generate the mipmaps with the GPU.
     *
     * Stupid fact: Minecraft calculates all of the native supported mipmaps (block textures, entity textures, etc) with the CPU (manually).
     * This makes the default implementation very inefficient, but since they're only generated when the game starts or when the
     * mipmaps are updated on the menu options, it isn't as noticeable.
     * @param ci the callback of the method (unused).
     */
    @TargetHandler(
            mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapTextureManager_MapTexture",
            name = "updateAtlasTexture"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImageBackedTexture;bindTexture()V")) // OK
    public void updateAtlasTexture(CallbackInfo ci) {
        NativeImageBackedTexture atlasTexture = this.immediatelyFast$atlasTexture.getTexture();
        NativeImage atlasImage = atlasTexture.getImage();
        int mipmapValue = MapMipMapModClient.options().generalOptions.getMapmipmapLevels();
        TextureUtil.prepareImage(atlasTexture.getGlId(), mipmapValue, atlasImage.getWidth(), atlasImage.getHeight());
        atlasTexture.upload();
        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
    }
}
