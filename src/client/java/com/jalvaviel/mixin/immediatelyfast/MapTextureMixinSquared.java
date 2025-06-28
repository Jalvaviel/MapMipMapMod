package com.jalvaviel.mixin.immediatelyfast;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.client.texture.MapTextureManager;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.jalvaviel.MapMipMapModClient.OUTDATED_DRIVER;


@Mixin(
        value = {MapTextureManager.MapTexture.class},
        priority = 1200
)
public class MapTextureMixinSquared {
    /**
     * This is the squared mixin that prepares the atlas textures to get mipmapped. It uploads the atlas texture to the graphics library,
     * to be rendered on screen later. The last line is a direct call to OpenGl to generate the mipmaps with the GPU.
     * Stupid fact: Minecraft calculates all the native-supported mipmaps (block textures, entity textures, etc) with the CPU (manually).
     * This makes the default implementation very inefficient, but since they're only generated when the game starts or when the
     * mipmaps are updated on the menu options, it isn't as noticeable.
     * @param ci the callback of the method (unused).
     */
    @TargetHandler(
            mixin = "net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation.MixinMapTextureManager_MapTexture",
            name = "updateAtlasTexture"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "FIELD", target = "Lnet/minecraft/client/texture/MapTextureManager$MapTexture;needsUpdate:Z"))
    public void onUpdateAtlasTexture(CallbackInfo ci) {
        if(!OUTDATED_DRIVER)GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
    }
}
