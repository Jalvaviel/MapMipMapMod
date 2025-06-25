package com.jalvaviel.mixin.client;

import com.jalvaviel.config.MmmmOptionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {

    /**
     * VideoOptionsScreenMixin constructor. It doesn't do anything since it gets discarded at compile time.
     * Since it's an abstract class that extends another it needs to be declared.
     * @param parent the parent screen.
     * @param gameOptions the game options.
     * @param title the title of the screen.
     */
    public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    /**
     * Injects the "MapMipMapMod" button to the vanilla video options screen. This button opens the custom options screen.
     * @param ci the method callback (unused).
     */
    @Inject(method = "addOptions", at = @At("TAIL"))
    protected void addOptions(CallbackInfo ci) {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.translatable("tab.mapmipmapmod.general"), buttonWidget1 ->
            MinecraftClient.getInstance().setScreen(new MmmmOptionScreen((VideoOptionsScreen)(Object)this,gameOptions))
        )
                .build();
        this.body.addWidgetEntry(buttonWidget,null);
    }
}
