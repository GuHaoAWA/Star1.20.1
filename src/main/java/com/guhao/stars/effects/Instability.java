package com.guhao.stars.effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class Instability extends InstantenousMobEffect {
    public Instability() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
    }

}
