package com.jalvaviel.mixin.sodium;

import com.jalvaviel.config.sodium.MmmmSodiumOptionScreen;
import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsGuiMixin {
    @Final
    @Shadow
    private List<OptionPage> pages;

    /**
     * Injects the "MapMipMapMod" tab to the sodium video options screen. This tab opens the custom options screen.
     * @param ci the method callback (unused).
     */
    @Inject(method="<init>", at=@At("TAIL"))
    private void onInit(CallbackInfo ci) {
        pages.add(MmmmSodiumOptionScreen.general());
    }

}
