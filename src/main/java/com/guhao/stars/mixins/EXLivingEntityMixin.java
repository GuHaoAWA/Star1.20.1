package com.guhao.stars.mixins;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class EXLivingEntityMixin {


    @Redirect(
            method = "getDamageAfterMagicAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffectInstance;getAmplifier()I"
            )
    )
    private int modifyResistanceAmplifierForDamageCalc(net.minecraft.world.effect.MobEffectInstance instance) {
        if (instance.getEffect() == MobEffects.DAMAGE_RESISTANCE) {
            return (int) ((instance.getAmplifier() + 1) * 1.25F) - 1;
        }
        return instance.getAmplifier();
    }
}
