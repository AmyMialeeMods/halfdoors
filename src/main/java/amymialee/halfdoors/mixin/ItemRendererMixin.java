package amymialee.halfdoors.mixin;

import amymialee.halfdoors.Halfdoors;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow protected abstract void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
        if (!stack.isEmpty()) {
            if (stack.isOf(Halfdoors.GOLD_DOOR_NUGGET) && shouldShowGoldBar(stack)) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                int i = getGoldBarStep(stack);
                int j = getGoldBarColor(stack);
                this.renderGuiQuad(bufferBuilder, x + 2, y + 13, i + 1, 2, 0, 0, 0, 255);
                this.renderGuiQuad(bufferBuilder, x + 2, y + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
    }

    private static boolean shouldShowGoldBar(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("hd:recharge") != 0;
    }

    private static int getGoldBarStep(ItemStack stack) {
        if (stack.isOf(Halfdoors.GOLD_DOOR_NUGGET)) {
            return Math.round(13.0F - (float)stack.getOrCreateNbt().getInt("hd:recharge") * 13.0F / 100f);
        }
        return 0;
    }

    private static int getGoldBarColor(ItemStack stack) {
        float f = Math.max(0.0F, (stack.getCount() - (stack.getOrCreateNbt().getBoolean("hd:empty") ? 1 : 0)) / 3f);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
