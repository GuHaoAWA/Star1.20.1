package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import com.guhao.stars.units.data.TimeContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Camera.class)
public class CameraMixin {
    @ModifyVariable(method = "setup", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float partial(float value) {
        if (StarDataUnit.isTimeStopped() && Minecraft.getInstance().player != null) {
            value = TimeContext.Client.timer.partialTick;
        }
        return value;
    }
}
