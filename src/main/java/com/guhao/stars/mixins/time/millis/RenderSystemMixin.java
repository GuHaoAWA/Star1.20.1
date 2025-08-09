package com.guhao.stars.mixins.time.millis;


import com.guhao.stars.units.data.TimeContext;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Redirect(method = "pollEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private static long aLong() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "isFrozenAtPollEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private static long aLong2() {
        return TimeContext.Both.getRealMillis();
    }
}
