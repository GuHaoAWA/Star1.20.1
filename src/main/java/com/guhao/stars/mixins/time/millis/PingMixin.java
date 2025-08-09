package com.guhao.stars.mixins.time.millis;


import com.guhao.stars.units.data.TimeContext;
import com.mojang.realmsclient.client.Ping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Ping.class)
public class PingMixin {
    @Redirect(method = "now", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private static long aLong() {
        return TimeContext.Both.getRealMillis();
    }
}
