package com.jalvaviel.mixin.client;

import com.jalvaviel.config.MmmmOptionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {

    @Shadow private OptionListWidget list;

    /**
     * VideoOptionsScreenMixin constructor. It doesn't do anything since it gets discarded at compile time.
     * Since it's an abstract class that extends another, it needs to be declared.
     * @param parent the parent screen.
     * @param gameOptions the game options.
     * @param title the title of the screen.
     */
    public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    /**
     * Injects the "MapMipMapMod" button to the vanilla video options screen. This button opens the custom options screen.
     * In this version, it can't be injected into the OptionListWidget of the VideoOptionsScreen, because it's a button and can't be cast into a SimpleOption.
     * @param ci the method callback (unused).
     */
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/VideoOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    protected void addOptions(CallbackInfo ci) {
        int i = this.width / 2 - 155;
        int k = this.height - 27;
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.translatable("tab.mapmipmapmod.general"), buttonWidget1 ->
            MinecraftClient.getInstance().setScreen(new MmmmOptionScreen((VideoOptionsScreen)(Object)this,gameOptions))
                    ).dimensions(i,k,150,20).build();
        this.addDrawableChild(buttonWidget);
    }

    /**
     * Since in the last injection I inject the button to the "footer", I need to move the "Done" button to the right.
     * @param args the args of the method (0 being the offset, and 2 being the width).
     */
    @ModifyArgs(method = "init", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"))
    private void changeDimensions(Args args) {
        int i = this.width / 2 - 155;
        int j = i + 160;
        args.set(0,j);
        args.set(2,150);
    }
}
