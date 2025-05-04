package com.guhao.stars.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class Slow_Time extends MobEffect {
    public Slow_Time() {
        super(MobEffectCategory.BENEFICIAL, -13261);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.star.slow_time";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
