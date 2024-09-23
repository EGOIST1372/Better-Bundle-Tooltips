package io.github.andrew6rant.betterbundletooltips.mixin.client;

import io.github.andrew6rant.betterbundletooltips.BundleTooltipUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
}