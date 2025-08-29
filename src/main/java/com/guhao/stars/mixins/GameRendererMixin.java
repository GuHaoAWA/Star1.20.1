package com.guhao.stars.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    public ItemStack itemActivationItem;
    @Shadow
    public int itemActivationTicks;
    @Shadow
    public float itemActivationOffX;
    @Shadow
    public float itemActivationOffY;
    @Shadow
    public Minecraft minecraft;
    @Shadow
    public RenderBuffers renderBuffers;
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderItemActivationAnimation(int screenWidth, int screenHeight, float partialTicks) {
        if (this.itemActivationItem != null && this.itemActivationTicks > 0) {
            int i = 40 - this.itemActivationTicks;
            float f = ((float)i + partialTicks) / 40.0F;
            float f1 = f * f;
            float f2 = f * f1;
            float f3 = 10.25F * f2 * f1 - 24.95F * f1 * f1 + 25.5F * f2 - 13.8F * f1 + 4.0F * f;
            float f4 = f3 * 3.1415927F;

            float screenFactor = Math.min(screenWidth, screenHeight) / 1080.0F;

            float f5 = this.itemActivationOffX * (float)(screenWidth / 4);
            float f6 = this.itemActivationOffY * (float)(screenHeight / 4);
            RenderSystem.enableDepthTest();
            RenderSystem.disableCull();
            PoseStack posestack = new PoseStack();
            posestack.pushPose();
            posestack.translate(
                    (float)(screenWidth / 2) + f5 * Mth.abs(Mth.sin(f4 * 2.0F)),
                    (float)(screenHeight / 2) + f6 * Mth.abs(Mth.sin(f4 * 2.0F)),
                    -50.0F
            );

            float baseSize = 50.0F * screenFactor;
            float animationScale = 175.0F * screenFactor * Mth.sin(f4);
            float f7 = baseSize + animationScale;

            posestack.scale(f7, -f7, f7);
            posestack.mulPose(Axis.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin(f4))));
            posestack.mulPose(Axis.XP.rotationDegrees(6.0F * Mth.cos(f * 8.0F)));
            posestack.mulPose(Axis.ZP.rotationDegrees(6.0F * Mth.cos(f * 8.0F)));

            MultiBufferSource.BufferSource multibuffersource$buffersource = this.renderBuffers.bufferSource();
            this.minecraft.getItemRenderer().renderStatic(
                    this.itemActivationItem,
                    ItemDisplayContext.FIXED,
                    15728880,
                    OverlayTexture.NO_OVERLAY,
                    posestack,
                    multibuffersource$buffersource,
                    this.minecraft.level,
                    0
            );
            posestack.popPose();
            multibuffersource$buffersource.endBatch();
            RenderSystem.enableCull();
            RenderSystem.disableDepthTest();
        }
    }
}
