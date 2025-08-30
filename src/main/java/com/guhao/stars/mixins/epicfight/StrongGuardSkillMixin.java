package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.entity.StarAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.guard.ImpactGuardSkill;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.TakeDamageEvent;

@Mixin(value = ImpactGuardSkill.class, remap = false)
public class StrongGuardSkillMixin {
    private TakeDamageEvent.Attack event;

    @Inject(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/TakeDamageEvent$Attack;FFZ)V"},
            at = {@At("HEAD")},
            remap = false
    )
    private void getSuccessParry(SkillContainer container, CapabilityItem itemCapability, TakeDamageEvent.Attack event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        this.event = event;
    }

    @ModifyVariable(
            method = {"guard(Lyesman/epicfight/skill/SkillContainer;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/world/entity/eventlistener/TakeDamageEvent$Attack;FFZ)V"},
            at = @At("HEAD"),
            ordinal = 1,
            remap = false,
            argsOnly = true)
    private float setImpact(float impact) {
        float blockrate = 1.0F - Math.min((float) this.event.getPlayerPatch().getOriginal().getAttributeValue(StarAttributes.BLOCK_RATE.get()) / 100.0F, 0.9F);
        Object var4 = this.event.getDamageSource();
        if (var4 instanceof EpicFightDamageSource epicdamagesource) {
            float k = epicdamagesource.calculateImpact(); // 修复：getImpact() -> calculateImpact()
            return this.event.getDamage() * (1.0F + k / 5F) * blockrate; // 修复：getAmount() -> getDamage()
        } else {
            return this.event.getDamage() / 3.0F * blockrate; // 修复：getAmount() -> getDamage()
        }
    }
}
