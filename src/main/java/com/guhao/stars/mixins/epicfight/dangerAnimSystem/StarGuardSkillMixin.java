package com.guhao.stars.mixins.epicfight.dangerAnimSystem;

import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.TakeDamageEvent;

@SuppressWarnings("all")
@Mixin(GuardSkill.class)
public class StarGuardSkillMixin extends Skill {
    @Unique
    private TakeDamageEvent.Attack star1_20_1$event;

    public StarGuardSkillMixin(GuardSkill.Builder builder) {
        super(builder);
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
    public void star$guard(SkillContainer container, CapabilityItem itemCapability, TakeDamageEvent.Attack event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        DOTEPassive.breakdown(container);
    }

    @Inject(
            method = "guard",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    protected void onGuard(SkillContainer container, CapabilityItem itemCapability,
                           TakeDamageEvent.Attack event, float knockback, float impact,
                           boolean advanced, CallbackInfo ci) {

        EpicFightDamageSource damageSource = AnimationEffectManager.getEpicFightDamageSources(event.getDamageSource());

        if (damageSource != null && damageSource.getAnimation() != null) {
            StaticAnimation animation = damageSource.getAnimation().get();

            if (AnimationEffectManager.isNoGuardAnimation(animation)) {
                event.setParried(false);
                event.setResult(AttackResult.ResultType.SUCCESS);
                ci.cancel();
            }
        }
    }

    @Inject(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/TakeDamageEvent$Attack;FFZ)V"},
            at = {@At("HEAD")},
            remap = false
    )
    private void getSuccessParry(SkillContainer container, CapabilityItem itemCapability, TakeDamageEvent.Attack event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        this.star1_20_1$event = event;
    }

    @ModifyVariable(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/TakeDamageEvent$Attack;FFZ)V"},
            at = @At("HEAD"),
            ordinal = 1,
            remap = false,
            argsOnly = true)
    private float setImpact(float impact) {
        float blockrate = 1.0F - Math.min((float) this.star1_20_1$event.getPlayerPatch().getOriginal().getAttributeValue(StarAttributes.BLOCK_RATE.get()) / 100.0F, 0.9F);
        Object var4 = this.star1_20_1$event.getDamageSource();
        if (var4 instanceof EpicFightDamageSource epicdamagesource) {
            float k = epicdamagesource.calculateImpact();
            return this.star1_20_1$event.getDamage() * (1.0F + k / 5F) * blockrate;
        } else {
            return this.star1_20_1$event.getDamage() / 3.0F * blockrate;
        }
    }
}


