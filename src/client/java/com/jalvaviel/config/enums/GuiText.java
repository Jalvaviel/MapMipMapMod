package com.jalvaviel.config.enums;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public interface GuiText {
    Component getName();
    Component getTextTooltip();
    Tooltip getTooltip();
    int getId();
}
