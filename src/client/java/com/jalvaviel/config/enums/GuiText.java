package com.jalvaviel.config.enums;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

public interface GuiText {
    Text getName();
    Text getTextTooltip();
    Tooltip getTooltip();
    int getId();
}
