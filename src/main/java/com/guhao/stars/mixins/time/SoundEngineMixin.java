package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @Inject(method = "tickNonPaused", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped())
            ci.cancel();
    }

    @Inject(method = "resume", at = @At("HEAD"), cancellable = true)
    private void resume(CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped())
            ci.cancel();
    }
}
