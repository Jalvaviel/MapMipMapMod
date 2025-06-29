package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import net.minecraft.client.render.*;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MapRenderer.class, priority = 1200)
public abstract class MapRendererMixin {
    /**
     * This mixin ignores map updates (which happen very frequently) for locked maps, since they aren't supposed to
     * be updated. It helps frame stability and removes lag spikes when loading lots of maps simultaneously.
     * @param mapIdComponent the MapIdComponent object (unused).
     * @param mapState the mapState object, which contains if the map is locked or not.
     * @param ci callback which gets canceled if the map is locked, effectively removing a lot of pointless logic.
     */
    @Inject(method = "updateTexture", at = @At("HEAD"), cancellable = true)
    public void setNeedsUpdate(MapIdComponent mapIdComponent, MapState mapState, CallbackInfo ci){
        if (mapState.locked && MapMipMapModClient.options().generalOptions.isLockedMapUpdates()) {
            ci.cancel();
        }
    }
}
