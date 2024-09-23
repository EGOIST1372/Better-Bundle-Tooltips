package io.github.andrew6rant.betterbundletooltips;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BundleTooltipUtil {
    public static void drawProgressBar(int x, int y, TextRenderer textRenderer, DrawContext drawContext, Identifier progressBarFillTexture, int progressBarFill, Identifier progressBarBorderTexture, Text progressBarLabel) {
        drawContext.drawGuiTexture(RenderLayer::getGuiTextured, progressBarFillTexture,x + 1, y - 3, progressBarFill, 4);
        drawContext.drawGuiTexture(RenderLayer::getGuiTextured, progressBarBorderTexture, x, y - 3, 96, 4);
        /*
        if (progressBarLabel != null) {
            drawContext.drawCenteredTextWithShadow(textRenderer, progressBarLabel, x + 48, y - 4, 16777215);
        }
        */
    }

    // This is to make it easier for other mods to modify bundle tooltip heights after mine.
    public static int getTooltipBackgroundXoffset() {
        return -14;
    }
}
