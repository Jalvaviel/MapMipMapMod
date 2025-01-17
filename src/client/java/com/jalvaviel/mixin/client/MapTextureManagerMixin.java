package com.jalvaviel.mixin.client;

import net.minecraft.client.texture.MapTextureManager;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MapTextureManager.class, priority = 1200)
public class MapTextureManagerMixin {
    @Inject(method = "setNeedsUpdate", at = @At("HEAD"), cancellable = true)
    public void setNeedsUpdate(MapIdComponent mapIdComponent, MapState mapState, CallbackInfo ci){
        if (mapState.locked) {
            ci.cancel();
        }
    }
}
