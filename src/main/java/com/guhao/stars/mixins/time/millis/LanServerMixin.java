package com.guhao.stars.mixins.time.millis;


import com.guhao.stars.units.data.TimeContext;
import net.minecraft.client.server.LanServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanServer.class)
public class LanServerMixin {
    @Shadow
    private long pingTime;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(String p_120076_, String p_120077_, CallbackInfo ci) {
        this.pingTime = TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "updatePingTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long getPingTime() {
        return TimeContext.Both.getRealMillis();
    }
}
