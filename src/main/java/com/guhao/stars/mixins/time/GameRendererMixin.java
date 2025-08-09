package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import com.guhao.stars.units.data.TimeContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float render(float value) {
        if (StarDataUnit.isTimeStopped())
            value = TimeContext.Client.timer.partialTick;
        return value;
    }

    @ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 0), ordinal = 0, argsOnly = true)
    private float render_beforeCamera(float value) {
        if (StarDataUnit.isTimeStopped())
            value = TimeContext.Client.timer.partialTick;
        return value;
    }

    @ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onCameraSetup(Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/Camera;F)Lnet/minecraftforge/client/event/ViewportEvent$ComputeCameraAngles;"), remap = false, ordinal = 0, argsOnly = true)
    private float render_afterCamera(float value) {
        if (StarDataUnit.isTimeStopped())
            value = Minecraft.instance.timer.partialTick;
        return value;
    }

    @ModifyVariable(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;prepareCullFrustum(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;Lorg/joml/Matrix4f;)V"), ordinal = 0, argsOnly = true)
    private float render_beforeLevel(float value) {
        if (StarDataUnit.isTimeStopped())
            value = Minecraft.instance.timer.partialTick;
        return value;
    }

    @ModifyVariable(method = "bobView", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float bobView(float value) {
        if (StarDataUnit.isTimeStopped() && Minecraft.getInstance().player != null) {
            value = TimeContext.Client.timer.partialTick;
        }
        return value;
    }

    @ModifyVariable(method = "bobHurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float bobHurt(float value) {
        if (StarDataUnit.isTimeStopped() && Minecraft.getInstance().player != null) {
            value = TimeContext.Client.timer.partialTick;
        }
        return value;
    }
}
