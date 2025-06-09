package com.guhao.stars.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.UUID;

public class ImpactEnhance extends MobEffect {
    public ImpactEnhance() {
        super(MobEffectCategory.NEUTRAL, 0xFF0000);

    }
    private static final UUID KNOCKBACK_MODIFIER_UUID = UUID.fromString("4f2c9273-c7d1-46cb-8de8-4571c696c2e4");
    private static final UUID KNOCKBACK_OFF_MODIFIER_UUID = UUID.fromString("4f2c9273-c7d1-46cb-8de9-4571c696c2e4");
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        AttributeInstance attackDamage = entity.getAttribute(EpicFightAttributes.IMPACT.get());
        if (attackDamage != null) {
            attackDamage.removeModifier(KNOCKBACK_MODIFIER_UUID);
            AttributeModifier modifier = new AttributeModifier(
                    KNOCKBACK_MODIFIER_UUID,
                    "enhance_impact",
                    0.3 *(amplifier+1),
                    AttributeModifier.Operation.ADDITION
            );
            attackDamage.addTransientModifier(modifier);
        }
        AttributeInstance off = entity.getAttribute(EpicFightAttributes.OFFHAND_IMPACT.get());
        if (off != null) {
            off.removeModifier(KNOCKBACK_OFF_MODIFIER_UUID);
            AttributeModifier modifier = new AttributeModifier(
                    KNOCKBACK_OFF_MODIFIER_UUID,
                    "enhance_off_impact",
                    0.3 *(amplifier+1),
                    AttributeModifier.Operation.ADDITION
            );
            off.addTransientModifier(modifier);
        }

    }
    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        AttributeInstance knockbackAttribute = entity.getAttribute(EpicFightAttributes.IMPACT.get());
        if (knockbackAttribute != null) {
            knockbackAttribute.removeModifier(KNOCKBACK_MODIFIER_UUID);
        }
        AttributeInstance knockbackOffAttribute = entity.getAttribute(EpicFightAttributes.OFFHAND_IMPACT.get());
        if (knockbackOffAttribute != null) {
            knockbackOffAttribute.removeModifier(KNOCKBACK_OFF_MODIFIER_UUID);
        }
    }
}