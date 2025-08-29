package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.units.StarDataUnit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(value = GuardSkill.class, remap = false, priority = 500)
public abstract class GuardSkillMixin extends Skill {
    private HurtEvent.Pre event;
    public GuardSkillMixin(Builder<? extends Skill> builder) {
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
    public void star$guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        DOTEPassive.breakdown(container);
    }
    @Inject(
            method = "guard",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    public void star$head_guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {

        EpicFightDamageSource damageSource = StarDataUnit.getEpicFightDamageSources(event.getDamageSource());

        if (damageSource != null && (StarDataUnit.isNoGuard(damageSource.getAnimation()))) {
            event.setParried(false);
            event.setResult(AttackResult.ResultType.SUCCESS);
            ci.cancel();
        }
        if (damageSource != null && (StarDataUnit.isNoDodge(damageSource.getAnimation()))) {
            event.setParried(false);
            event.setResult(AttackResult.ResultType.SUCCESS);
            ci.cancel();
        }
    }
    @Inject(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;FFZ)V"},
            at = {@At("HEAD")},
            remap = false
    )
    private void getSuccessParry(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        this.event = event;
    }

    @ModifyVariable(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/HurtEvent$Pre;FFZ)V"},
            at = @At("HEAD"),
            ordinal = 1,
            remap = false,
            argsOnly = true)
    private float setImpact(float impact) {
        float blockrate = 1.0F - Math.min((float) this.event.getPlayerPatch().getOriginal().getAttributeValue(StarAttributes.BLOCK_RATE.get()) / 100.0F, 0.9F);
        Object var4 = this.event.getDamageSource();
        if (var4 instanceof EpicFightDamageSource epicdamagesource) {
            float k = epicdamagesource.getImpact();
            return this.event.getAmount() * (1.0F + k / 5F) * blockrate;
        } else {
            return this.event.getAmount() / 3.0F * blockrate;
        }
    }
}