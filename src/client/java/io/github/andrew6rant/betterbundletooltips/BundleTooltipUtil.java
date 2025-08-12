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
    // Draw the filled portion of the progress bar
    drawContext.drawGuiTexture(progressBarFillTexture, x + 1, y - 2, progressBarFill, 4);

    // Draw the border of the progress bar
    drawContext.drawGuiTexture(progressBarBorderTexture, x, y - 2, 108, 4);
    }


    // This is to make it easier for other mods to modify bundle tooltip heights/widths after mine.
    public static int getTooltipBackgroundXoffset() {
        return -13;
    }
    public static int getTooltipWidth() {
        return 108;
    }


    public static int getProgressBarFill() {
        return 106;
    }
    public static int getItemPadding() {
        return 18;
    }
    public static int getItemOffset() {
        return 1;
    }
    public static int getExtraItemsTextOffsetY() {
        return 6;
    }
    public static int getItemTextOffsetX() {
        return 8;
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
