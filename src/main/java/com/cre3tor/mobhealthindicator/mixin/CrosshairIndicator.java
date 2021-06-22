package com.cre3tor.mobhealthindicator.mixin;

import com.cre3tor.mobhealthindicator.client.gui.GuiElements;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(InGameHud.class)
public class CrosshairIndicator extends DrawableHelper {

    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledHeight;
    @Shadow private int scaledWidth;

    @Redirect(method = "renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
            ordinal = 0
    ))
    private void redrawCrosshair(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if (client.targetedEntity instanceof LivingEntity livingEntity) {
            drawHealthIndicator(matrices, livingEntity);
        }
            this.drawTexture(matrices, x, y, u, v, width, height);

    }

    private void drawHealthIndicator(MatrixStack matrices, LivingEntity livingEntity) {
        float healthPercent = livingEntity.getHealth() / livingEntity.getMaxHealth();

        RenderSystem.setShaderTexture(0, GuiElements.MHI_TEXTURE);

        if (healthPercent > 0.0f) {
            float i = Math.min(healthPercent / 0.25f, 1.0f);
            RenderSystem.setShaderColor(i, i / 2, i / 2, 1.0f);
            drawTexture(matrices, (this.scaledWidth - 15) / 2, (this.scaledHeight - 15) / 2, 0, 0, 7, 7, 15, 15);
            if (healthPercent > 0.25f) {
                i = Math.min((healthPercent - 0.25f) / 0.25f, 1.0f);
                RenderSystem.setShaderColor(i, i / 2, i / 2, 1.0f);
                drawTexture(matrices, (this.scaledWidth + 1) / 2, (this.scaledHeight - 15) / 2, 8, 0, 7, 7, 15, 15);
                if (healthPercent > 0.5f) {
                    i = Math.min((healthPercent - 0.5f) / 0.25f, 1.0f);
                    RenderSystem.setShaderColor(i, i / 2, i / 2, 1.0f);
                    drawTexture(matrices, (this.scaledWidth + 1) / 2, (this.scaledHeight + 1) / 2, 8, 8, 7, 7, 15, 15);
                    if (healthPercent > 0.75f) {
                        i = Math.min((healthPercent - 0.75f) / 0.25f, 1.0f);
                        RenderSystem.setShaderColor(i, i / 2, i / 2, 1.0f);
                        drawTexture(matrices, (this.scaledWidth - 15) / 2, (this.scaledHeight + 1) / 2, 0, 8, 7, 7, 15, 15);
                    }
                }
            }
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
    }


}
