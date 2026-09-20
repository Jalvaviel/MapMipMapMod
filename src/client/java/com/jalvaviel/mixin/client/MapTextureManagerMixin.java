package com.jalvaviel.mixin.client;

import com.jalvaviel.MapMipMapModClient;
import com.jalvaviel.config.enums.MapUpdates;
import net.minecraft.client.resources.MapTextureManager;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MapTextureManager.class, priority = 1200)
public class MapTextureManagerMixin {
    /**
     * This mixin ignores map updates (which happen very frequently) for locked maps, since they aren't supposed to
     * be updated. It helps frame stability and removes lag spikes when loading lots of maps simultaneously.
     * @param id the MapIdComponent object (unused).
     * @param data the mapState object, which contains if the map is locked or not.
     * @param ci callback which gets canceled if the map is locked, effectively removing a lot of pointless logic.
     */
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void setNeedsUpdate(MapId id, MapItemSavedData data, CallbackInfo ci){
        if ((data.locked && MapMipMapModClient.options().generalOptions.getMapUpdates() == MapUpdates.ONLY_UNLOCKED)
                || MapMipMapModClient.options().generalOptions.getMapUpdates() == MapUpdates.NONE) {
            ci.cancel();
        }
    }

}
