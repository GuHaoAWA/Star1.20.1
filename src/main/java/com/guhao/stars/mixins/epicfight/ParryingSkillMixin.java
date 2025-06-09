package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.units.StarArrayUnit;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(value = ParryingSkill.class,remap = false,priority = 500)
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
    @Inject(
            method = "guard",
            at = @At(
                    value = "INVOKE",
                    target = "Lyesman/epicfight/world/capabilities/entitypatch/player/ServerPlayerPatch;playSound(Lnet/minecraft/sounds/SoundEvent;FFF)V",
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    public void star$guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        DOTEPassive.breakdown(container);
    }
    // 在if块开头注入
    @Inject(
            method = "guard",
            at = @At(
                    value = "INVOKE",
                    target = "Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;setParried(Z)V",
                    ordinal = 0
            )
    )
    private void onSuccessfulParry(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        AdvancedCustomHumanoidMobPatch<?> longpatch = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getEntity(), AdvancedCustomHumanoidMobPatch.class);
        if (longpatch != null) {
            longpatch.setStamina((float) (longpatch.getStamina() - longpatch.getOriginal().getAttribute(StarAttributes.PARRY_STAMINA_LOSE.get()).getValue()));
            if (longpatch.getOriginal().hasEffect(Effect.STA.get())) {
                longpatch.setStamina(longpatch.getStamina() - longpatch.getOriginal().getEffect(Effect.STA.get()).getAmplifier() +1);
            }
        }
    }
    @Inject(
            method = "guard",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void star$head_guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        EpicFightDamageSource damageSource = StarArrayUnit.getEpicFightDamageSources(event.getDamageSource());
        if (damageSource != null && StarArrayUnit.isNoGuard(damageSource.getAnimation())) {
            ci.cancel();
        }
        if (damageSource != null && StarArrayUnit.isNoDodge(damageSource.getAnimation())) {
            ci.cancel();
        }
    }
}
