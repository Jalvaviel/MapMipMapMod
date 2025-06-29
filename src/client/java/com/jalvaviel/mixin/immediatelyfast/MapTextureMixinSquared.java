package com.jalvaviel.mixin.immediatelyfast;

import com.bawnorton.mixinsquared.TargetHandler;
import com.jalvaviel.MapMipMapModClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.render.MapRenderer;
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
        value = {MapRenderer.MapTexture.class},
        priority = 1200
)
public class MapTextureMixinSquared {
    @Shadow
    private MapAtlasTexture immediatelyFast$atlasTexture;

    /**
     * This is the squared mixin that prepares the atlas textures to get mipmapped. It uploads the atlas texture to the graphics library,
     * to be rendered on screen later. The last line is a direct call to OpenGl to generate the mipmaps with the GPU.
     * Stupid fact: Minecraft calculates all the native-supported mipmaps (block textures, entity textures, etc.) with the CPU (manually).
     * This makes the default implementation very inefficient, but since they're only generated when the game starts or when the
     * mipmaps are updated on the menu options, it isn't as noticeable.
     */

    @TargetHandler(mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapRenderer_MapTexture", name = "drawAtlasTexture")
    @ModifyExpressionValue(method = "@MixinSquared:Handler", at = @At(value = "CONSTANT", args = "floatValue=4096.0F")) //
    public float onDrawAtlasTexture(float original) {
        return MapMipMapModClient.options().generalOptions.getAtlasSize();
    }

    @TargetHandler(
            mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapRenderer_MapTexture",
            name = "updateAtlasTexture"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImageBackedTexture;bindTexture()V")) // OK
    public void updateAtlasTexture(CallbackInfo ci) {
        if (!MapMipMapModClient.OUTDATED_DRIVER) {
            NativeImageBackedTexture atlasTexture = this.immediatelyFast$atlasTexture.getTexture();
            NativeImage atlasImage = atlasTexture.getImage();
            int mipmapValue = MapMipMapModClient.options().generalOptions.getMapmipmapLevels();
            TextureUtil.prepareImage(atlasTexture.getGlId(), mipmapValue, atlasImage.getWidth(), atlasImage.getHeight());
            atlasTexture.upload();
            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        }
    }
}
