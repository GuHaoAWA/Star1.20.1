package com.guhao.stars.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;

public class StaminaReduce extends MobEffect {
    public StaminaReduce() {
        super(MobEffectCategory.HARMFUL, -13261);
    }
    @Override
    public @NotNull String getDescriptionId() {
        return "effect.star.stamina_reduce";
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
//        com.guhao.star.event.ExecutedRemoveEvent.execute(entity);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
