package io.github.andrew6rant.betterbundletooltips.mixin.client;

import io.github.andrew6rant.betterbundletooltips.BundleTooltipUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.BundleTooltipSubmenuHandler;
import net.minecraft.client.input.Scroller;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.BundleItemSelectedC2SPacket;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BundleTooltipSubmenuHandler.class)
public class BundleTooltipSubmenuHandlerMixin {
    @Shadow @Final private Scroller scroller;

    @Shadow @Final private MinecraftClient client;

    /**
     * @author Andrew6rant (Andrew Grant)
     * @reason Just testing for now, will replace with something more compatible later
     */
    @Overwrite
    public boolean onScroll(double horizontal, double vertical, int slotId, ItemStack item) {
        int i = BundleTooltipUtil.getNumberOfStacksShown(item);
        if (i == 0) {
            return false;
        } else {
            Vector2i vector2i = this.scroller.update(horizontal, vertical);
            int j = vector2i.y == 0 ? -vector2i.x : vector2i.y;
            if (j != 0) {
                int k = BundleItem.getSelectedStackIndex(item);
                k = Scroller.scrollCycling(j, k, i);
                this.sendPacket(item, slotId, k);
            }

            return true;
        }
    }

    /**
     * @author Andrew6rant (Andrew Grant)
     * @reason Just testing for now, will replace with something more compatible later
     */
    @Overwrite
    private void sendPacket(ItemStack item, int slotId, int selectedItemIndex) {
        if (this.client.getNetworkHandler() != null && selectedItemIndex < BundleTooltipUtil.getNumberOfStacksShown(item)) {
            ClientPlayNetworkHandler clientPlayNetworkHandler = this.client.getNetworkHandler();
            BundleItem.setSelectedStackIndex(item, selectedItemIndex);
            clientPlayNetworkHandler.sendPacket(new BundleItemSelectedC2SPacket(slotId, selectedItemIndex));
        }
    }
}