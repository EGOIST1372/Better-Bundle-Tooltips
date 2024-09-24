package io.github.andrew6rant.betterbundletooltips.mixin.client;

import io.github.andrew6rant.betterbundletooltips.BundleTooltipUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

@Mixin(BundleTooltipComponent.class)
public abstract class BundleTooltipComponentMixin {
	@Shadow @Final private BundleContentsComponent bundleContents;

    @Shadow
    private static int getHeightOfEmpty(TextRenderer textRenderer) {
        return 0;
    }

    @Shadow protected abstract int getHeightOfNonEmpty();

	@Shadow protected abstract Identifier getProgressBarFillTexture();

	@Shadow protected abstract int getProgressBarFill();

	@Shadow @Final
    private static Identifier BUNDLE_PROGRESS_BAR_BORDER_TEXTURE;

	@Shadow @Nullable protected abstract Text getProgressBarLabel();

	@Shadow protected abstract List<ItemStack> firstStacksInContents(int numberOfStacksShown);

	@Shadow protected abstract int getXMargin(int width);

	@Shadow protected abstract int getRows();

    @Shadow
    private static boolean shouldDrawExtraItemsCount(boolean hasMoreItems, int column, int row) {
        return false;
    }

    @Shadow
    private static void drawExtraItemsCount(int x, int y, int numExtra, TextRenderer textRenderer, DrawContext drawContext) {
    }

    @Shadow protected abstract int numContentItemsAfter(List<ItemStack> items);

    @Shadow
    private static boolean shouldDrawItem(List<ItemStack> items, int itemIndex) {
        return false;
    }

    @Shadow protected abstract void drawItem(int index, int x, int y, List<ItemStack> stacks, int seed, TextRenderer textRenderer, DrawContext drawContext);

	@Shadow protected abstract void drawSelectedItemTooltip(TextRenderer textRenderer, DrawContext drawContext, int x, int y, int width);

	@Shadow protected abstract int getRowsHeight();

	/**
	 * @author Andrew6rant (Andrew Grant)
	 * @reason Inject-cancel on `getHeightOfEmpty` and `getHeightOfNonEmpty` to change bundle tooltip height would
	 * not be more compatible than an Overwrite anyway.
	 */
	@Overwrite
	public int getHeight(TextRenderer textRenderer) {
		return this.bundleContents.isEmpty() ? getHeightOfEmpty(textRenderer) + BundleTooltipUtil.getTooltipBackgroundXoffset() : this.getHeightOfNonEmpty() + BundleTooltipUtil.getTooltipBackgroundXoffset();
	}


	/**
	 * @author Andrew6rant (Andrew Grant)
	 * @reason This is the whole purpose of the mod (to change bundle tooltip progress bar rendering)
	 */
	@Overwrite
	private void drawProgressBar(int x, int y, TextRenderer textRenderer, DrawContext drawContext) {
		BundleTooltipUtil.drawProgressBar(x, y, textRenderer, drawContext, this.getProgressBarFillTexture(), this.getProgressBarFill(), BUNDLE_PROGRESS_BAR_BORDER_TEXTURE, this.getProgressBarLabel());
	}

	@ModifyConstant(method = {
			"getRowsHeight()I",
			"drawItem(IIILjava/util/List;ILnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;)V"/*,
			"drawNonEmptyTooltip(Lnet/minecraft/client/font/TextRenderer;IIIILnet/minecraft/client/gui/DrawContext;)V"*/
	}, constant = @Constant(intValue = 24))
	private int betterbundletooltips$shrinkItemPadding(int padding) {
		return 18;
	}
	@ModifyConstant(method = "drawItem(IIILjava/util/List;ILnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;)V",
			constant = @Constant(intValue = 4))
	private int betterbundletooltips$drawItemOffset(int offset) {
		return 1;
	}
	@ModifyConstant(method = "drawExtraItemsCount(IIILnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;)V",
			constant = @Constant(intValue = 12))
	private static int betterbundletooltips$drawExtraItemsTextOffsetX(int offsetX) {
		return 8;
	}
	@ModifyConstant(method = "drawExtraItemsCount(IIILnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;)V",
			constant = @Constant(intValue = 10))
	private static int betterbundletooltips$drawExtraItemsTextOffsetY(int offsetY) {
		return 6;
	}

	/**
	 * @author Andrew6rant (Andrew Grant)
	 * @reason Inject-cancel or modifyConstant would not be more compatible anyway
	 */
	@Overwrite
	private int getNumVisibleSlots() {
		return Math.min(16, this.bundleContents.size());
	}


	/**
	 * @author Andrew6rant (Andrew Grant)
	 * @reason Just testing for now, will replace with something more compatible later
	 */
	@Overwrite
	private void drawNonEmptyTooltip(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
		boolean bl = this.bundleContents.size() > 16;
		List<ItemStack> list = this.firstStacksInContents(BundleTooltipUtil.getNumberOfStacksShown(this.bundleContents.size()));
		int i = x + this.getXMargin(width) + 96;
		int j = y + this.getRows() * 18;
		int k = 1;

		for(int l = 1; l <= this.getRows(); ++l) {
			for(int m = 1; m <= 4; ++m) {
				int n = i - m * 18;
				int o = j - l * 18;
				if (shouldDrawExtraItemsCount(bl, m, l)) {
					drawExtraItemsCount(n, o, this.numContentItemsAfter(list), textRenderer, context);
				} else if (shouldDrawItem(list, k)) {
					this.drawItem(k, n, o, list, k, textRenderer, context);
					++k;
				}
			}
		}
		this.drawSelectedItemTooltip(textRenderer, context, x, y, width);
		this.drawProgressBar(x + this.getXMargin(width), y + this.getRowsHeight() + 4, textRenderer, context);
	}
}
