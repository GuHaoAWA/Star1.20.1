package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.units.StarArrayUnit;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;

@Mixin(value = GuardSkill.class, remap = false, priority = 500)
public abstract class GuardSkillMixin extends Skill {

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

        EpicFightDamageSource damageSource = StarArrayUnit.getEpicFightDamageSources(event.getDamageSource());


        if (damageSource != null && (StarArrayUnit.isNoGuard(damageSource.getAnimation()) || StarArrayUnit.isNoDodge(damageSource.getAnimation()))) {
            ci.cancel();
        }
        if (damageSource != null && (StarArrayUnit.isNoDodge(damageSource.getAnimation()) || StarArrayUnit.isNoDodge(damageSource.getAnimation()))) {
            ci.cancel();
        }
    }
}