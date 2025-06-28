package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import net.minecraft.client.texture.MapTextureManager;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MapTextureManager.class, priority = 1200)
public class MapTextureManagerMixin {
    /**
     * This mixin ignores map updates (which happen very frequently) for locked maps, since they aren't supposed to
     * be updated. It helps frame stability and removes lag spikes when loading lots of maps simultaneously.
     * @param mapIdComponent the MapIdComponent object (unused).
     * @param mapState the mapState object, which contains if the map is locked or not.
     * @param ci callback which gets cancelled if the map is locked, effectively removing a lot of pointless logic.
     */
    @Inject(method = "setNeedsUpdate", at = @At("HEAD"), cancellable = true)
    public void setNeedsUpdate(MapIdComponent mapIdComponent, MapState mapState, CallbackInfo ci){
        if (mapState.locked && MapMipMapModClient.options().generalOptions.isLockedMapUpdates()) {
            ci.cancel();
        }
    }

}
