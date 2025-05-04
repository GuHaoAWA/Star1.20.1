package com.guhao.stars.mixins.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin(value = ParryingSkill.class,remap = false)
public class ParryingSkillMixin extends GuardSkill {
    public ParryingSkillMixin(Builder builder) {
        super(builder);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public static GuardSkill.Builder createActiveGuardBuilder() {
        return GuardSkill.createGuardBuilder().addAdvancedGuardMotion(CapabilityItem.WeaponCategories.SWORD, (itemCap, playerpatch) -> itemCap.getStyle(playerpatch) == CapabilityItem.Styles.ONE_HAND ? new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2} : new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT2, Animations.SWORD_GUARD_ACTIVE_HIT3})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.LONGSWORD, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.AXE, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.UCHIGATANA, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.SWORD_GUARD_ACTIVE_HIT1, Animations.SWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.TACHI, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2})
                .addAdvancedGuardMotion(CapabilityItem.WeaponCategories.SPEAR, (itemCap, playerpatch) -> new StaticAnimation[]{Animations.LONGSWORD_GUARD_ACTIVE_HIT1, Animations.LONGSWORD_GUARD_ACTIVE_HIT2});
    }
}
