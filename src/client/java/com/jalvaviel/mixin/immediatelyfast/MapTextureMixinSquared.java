package com.jalvaviel.mixin.immediatelyfast;

import com.bawnorton.mixinsquared.TargetHandler;
import com.jalvaviel.MapMipMapModClient;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.texture.MapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.jalvaviel.MapMipMapModClient.LOG;

import java.nio.IntBuffer;


@Mixin(
        value = {MapTextureManager.MapTexture.class},
        priority = 1200
)
public class MapTextureMixinSquared {
    @Shadow
    private MapAtlasTexture immediatelyFast$atlasTexture;

    @Shadow
    private boolean needsUpdate;

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
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "FIELD", target = "Lnet/minecraft/client/texture/MapTextureManager$MapTexture;needsUpdate:Z"), remap = false) // OK Lcom/mojang/blaze3d/systems/CommandEncoder;writeToTexture(Lcom/mojang/blaze3d/textures/GpuTexture;Lnet/minecraft/client/texture/NativeImage;IIIIIIII)V
    public void updateAtlasTexture(CallbackInfo ci) {
        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
    }
}
