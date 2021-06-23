package com.cre3tor.mobhealthindicator.mixin;

import com.cre3tor.mobhealthindicator.client.gui.HealthIndicator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InGameHud.class)
public class GameHudMixin extends DrawableHelper {

    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledHeight;
    @Shadow private int scaledWidth;

    @Redirect(method = "renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
            ordinal = 0
    ))
    private void redrawCrosshair(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if ((client.targetedEntity instanceof LivingEntity livingEntity) && !(livingEntity instanceof WitherEntity) && !(livingEntity instanceof EnderDragonEntity)) {
            HealthIndicator.setNewTarget(livingEntity);
        }
        HealthIndicator.drawCrossHair(matrices, this.scaledWidth, this.scaledHeight);

    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void tick(CallbackInfo info) {
        HealthIndicator.tick();
    }
}
