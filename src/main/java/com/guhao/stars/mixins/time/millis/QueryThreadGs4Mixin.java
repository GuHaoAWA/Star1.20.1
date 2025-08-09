package com.guhao.stars.mixins.time.millis;


import com.guhao.stars.units.data.TimeContext;
import net.minecraft.server.rcon.thread.QueryThreadGs4;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(QueryThreadGs4.class)
public class QueryThreadGs4Mixin {
    @Redirect(method = "buildRuleResponse", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "pruneChallenges", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong2() {
        return TimeContext.Both.getRealMillis();
    }

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong3() {
        return TimeContext.Both.getRealMillis();
    }
}
