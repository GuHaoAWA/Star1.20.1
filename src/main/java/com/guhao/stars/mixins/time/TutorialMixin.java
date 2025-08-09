package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.tutorial.Tutorial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tutorial.class)
public class TutorialMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped()) ci.cancel();
    }

}
