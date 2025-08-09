package com.guhao.stars.mixins.time;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.guhao.stars.units.StarDataUnit.isTimeStopped;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Inject(method = "tickBlockEntities", at = @At("HEAD"), cancellable = true)
    private void onTickBlockEntities(CallbackInfo ci) {
        if (isTimeStopped()) {
            ci.cancel();
        }
    }

}