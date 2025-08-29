package com.guhao.stars.mixins;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
    private int modifyResistanceAmplifierForDamageCalc(MobEffectInstance instance) {
        if (instance.getEffect() == MobEffects.DAMAGE_RESISTANCE) {
            return instance.getAmplifier(); // 保持原 amplifier，但后续计算会调整
        }
        return instance.getAmplifier();
    }

    @ModifyVariable(
            method = "getDamageAfterMagicAbsorb",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            ordinal = 0
    )
    private int modifyResistanceCalculation(int k) {
        // 检查当前是否在处理抗性效果
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
            return (k / 5);
        }
        return k;
    }
}