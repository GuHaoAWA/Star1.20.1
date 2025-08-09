package com.guhao.stars.mixins.time.millis;


import com.guhao.stars.units.data.TimeContext;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LoadingOverlay.class)
public class LoadingOverlayMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"))
    private long aLong() {
        return TimeContext.Both.getRealMillis();
    }
}
