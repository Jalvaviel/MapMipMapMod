package com.jalvaviel.mixin.client;

import com.jalvaviel.config.MmmmOptionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsScreenMixin extends OptionsSubScreen {

    /**
     * VideoOptionsScreenMixin constructor. It doesn't do anything since it gets discarded at compile time.
     * Since it's an abstract class that extends another it needs to be declared.
     * @param parent the parent screen.
     * @param gameOptions the game options.
     * @param title the title of the screen.
     */
    public VideoSettingsScreenMixin(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    /**
     * Injects the "MapMipMapMod" button to the vanilla video options screen. This button opens the custom options screen.
     * @param ci the method callback (unused).
     */
    @Inject(method = "addOptions", at = @At("TAIL"))
    protected void addOptions(CallbackInfo ci) {
        Button buttonWidget = Button.builder(Component.translatable("settinggroup.mapmipmapmod.general"), _ ->
            Minecraft.getInstance().setScreen(new MmmmOptionScreen((VideoSettingsScreen)(Object)this, options))
        ).width(310).build();
        assert this.list != null;
        this.list.addHeader(Component.translatable("tab.mapmipmapmod.general"));
        this.list.addSmall(buttonWidget,null);
    }
}
