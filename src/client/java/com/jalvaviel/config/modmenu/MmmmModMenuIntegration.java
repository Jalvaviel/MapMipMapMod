package com.jalvaviel.config.modmenu;

import com.jalvaviel.config.MmmmOptionScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;

public class MmmmModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new MmmmOptionScreen(parent, MinecraftClient.getInstance().options);
    }
}
