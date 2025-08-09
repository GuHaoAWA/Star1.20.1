package com.guhao.stars.mixins.time.millis;

import com.guhao.stars.units.data.TimeContext;
import net.minecraft.server.dedicated.ServerWatchdog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWatchdog.class)
public class ServerWatchdogMixin {
    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong() {
        return TimeContext.Both.getRealMillis();
    }
}
