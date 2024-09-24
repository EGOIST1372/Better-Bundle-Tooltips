package io.github.andrew6rant.betterbundletooltips;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BundleTooltipUtil {
    public static int BUNDLE_ITEM_ROW_COUNT = 6; // number of items in a row, not number of rows lol
    public static int BUNDLE_ITEM_COLUMN_COUNT = 5;
    public static int BUNDLE_MAX_COUNT = BUNDLE_ITEM_ROW_COUNT * BUNDLE_ITEM_COLUMN_COUNT;

    public static void drawProgressBar(int x, int y, TextRenderer textRenderer, DrawContext drawContext, Identifier progressBarFillTexture, int progressBarFill, Identifier progressBarBorderTexture) {
        drawContext.drawGuiTexture(RenderLayer::getGuiTextured, progressBarFillTexture,x + 1, y - 3, progressBarFill, 4);
        drawContext.drawGuiTexture(RenderLayer::getGuiTextured, progressBarBorderTexture, x, y - 3, 96, 4);
    }

    // This is to make it easier for other mods to modify bundle tooltip heights after mine.
    public static int getTooltipBackgroundXoffset() {
        return -14;
    }

    public static int getNumberOfStacksShown(int i) {
        int j = i > BUNDLE_MAX_COUNT ? (BUNDLE_MAX_COUNT-1) : BUNDLE_MAX_COUNT;
        int k = i % 4;
        int l = k == 0 ? 0 : 4 - k;
        return Math.min(i, j - l);
    }

    public static int getNumberOfStacksShown(ItemStack stack) {
        BundleContentsComponent bundleContentsComponent = stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
        return getNumberOfStacksShown(bundleContentsComponent.size());
    }
}
