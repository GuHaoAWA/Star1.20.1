package com.guhao.stars.mixins.epicfight.dangerAnimSystem;

import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.DodgeLocationIndicator;

@Mixin(DodgeLocationIndicator.class)
public class DodgeLocationIndicatorMixin {

    @Inject(
            method = "hurt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHurt(DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource instanceof EpicFightDamageSource epicFightDamageSource) {
            if (epicFightDamageSource.getAnimation() != null) {
                var animation = epicFightDamageSource.getAnimation().get();
                if (AnimationEffectManager.isNoDodgeAnimation(animation)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
