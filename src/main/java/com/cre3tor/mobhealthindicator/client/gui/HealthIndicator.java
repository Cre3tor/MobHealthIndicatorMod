package com.cre3tor.mobhealthindicator.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import static net.minecraft.client.gui.DrawableHelper.GUI_ICONS_TEXTURE;

@Environment(EnvType.CLIENT)
public class HealthIndicator {
    private static int age;
    private static float healthPercent;
    
    public static void drawCrossHair(MatrixStack matrices, int scaledWidth, int scaledHeight) {
        if (age < 10) {
            int animationTime = (int)(Math.pow(Math.max(age, 0), 3) / 100);
            drawIndicator(matrices, scaledWidth, scaledHeight, animationTime);
            drawCrosshair(matrices, scaledWidth, scaledHeight, animationTime);
        }
        else {
            DrawableHelper.drawTexture(matrices, (scaledWidth - 15) / 2, (scaledHeight - 15) / 2, 0, 0, 15, 15, 256, 256);
        }
    }

    private static void drawIndicator(MatrixStack matrices, int scaledWidth, int scaledHeight, int animationTime) {
            RenderSystem.setShaderTexture(0, GuiElements.MHI_TEXTURE);
            for (int i = 0; i < 4; i++) {
                if (healthPercent > 0.25f * i) {
                    float colorK = Math.min((healthPercent - i * 0.25f) / 0.25f, 1.0f) * 0.8f * ((10.0f - animationTime) / 10);
                    RenderSystem.setShaderColor(colorK, colorK , colorK, 1.0f);
                    int kx = (int)Math.pow(-1, (int)((i + 1) / 2 + 1) % 2);
                    int ky = (int)Math.pow(-1, (int)(i / 2) % 2);
                    DrawableHelper.drawTexture(matrices, (scaledWidth - 7 + (8 + animationTime) * kx) / 2, (scaledHeight - 7 + (8 + animationTime) * ky) / 2, 0, 8 * (kx + 1.0f) / 2, 8 * (ky + 1.0f) / 2, 7, 7, 15, 15);
                }
                else {
                    break;
                }
            }
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
    }

    private static void drawCrosshair(MatrixStack matrices, int scaledWidth, int scaledHeight, int animationTime) {
        int delta = 5 + animationTime;
        DrawableHelper.drawTexture(matrices, (scaledWidth - delta) / 2, (scaledHeight - delta) / 2, (10.0f - animationTime) / 2, (10.0f - animationTime) / 2, delta, delta, 256, 256);
    }

    public static void setNewTarget(LivingEntity targetedEntity) {
        if (age > -2) {
            age -= 2;
        }
        healthPercent = targetedEntity.getHealth() / targetedEntity.getMaxHealth();
    }

    public static void tick() {
        if (age < 10) {
            age++;
        }
    }
}
