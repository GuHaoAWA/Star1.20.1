package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SoundManager.class)
public abstract class SoundManagerMixin {

    @ModifyVariable(method = "tick", at = @At("HEAD"), argsOnly = true)
    private boolean tick(boolean v) {
        if (StarDataUnit.isTimeStopped()) v = true;
        return v;
    }

}
