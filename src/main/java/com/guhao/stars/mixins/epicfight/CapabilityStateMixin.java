package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.units.StarDataUnit;
import com.nameless.indestructible.api.animation.types.LivingEntityPatchEvent;
import com.nameless.indestructible.data.AdvancedMobpatchReloader;
import com.nameless.indestructible.world.capability.Utils.CapabilityState;
import com.nameless.indestructible.world.capability.Utils.IAdvancedCapability;
import com.nameless.indestructible.world.capability.Utils.IAnimationEventCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

@Mixin(value = CapabilityState.class,remap = false)
public class CapabilityStateMixin<T extends MobPatch<?>, V extends AdvancedMobpatchReloader.AdvancedCustomMobPatchProvider> {
    @Shadow
    public boolean neutralized;
    @Mutable
    @Final
    @Shadow
    private final T mobPatch;
    @Shadow
    private Entity lastAttacker;
    @Shadow
    private float lastGetImpact;
    @Mutable
    @Final
    @Shadow
    private final float staminaLoseMultiply;

    public CapabilityStateMixin(T mobPatch, float staminaLoseMultiply) {
        this.mobPatch = mobPatch;
        this.staminaLoseMultiply = staminaLoseMultiply;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public StunType processStun(StunType stunType){
        if (this.neutralized) {
            stunType = stunType == StunType.KNOCKDOWN ? stunType : StunType.NONE;
        } else if (mobPatch instanceof IAdvancedCapability iac && this.staminaLoseMultiply > 0 && this.lastGetImpact > 0 && mobPatch.getStunShield() <= 0) {
            float reduceX;
            if (!mobPatch.getOriginal().hasEffect(Effect.TOUGHNESS.get())) {
                reduceX = (float) (1.0f - mobPatch.getOriginal().getAttributeValue(StarAttributes.HIT_STAMINA_LOSE.get()));
            } else {
                int buffer = mobPatch.getOriginal().getEffect(Effect.TOUGHNESS.get()).getAmplifier() + 1;
                if (((float) buffer/10  + (1.0f - (mobPatch.getOriginal().getAttributeValue(StarAttributes.HIT_STAMINA_LOSE.get()))) >= 1.0f)) {
                    reduceX = 1.0f;
                } else {
                    reduceX = (float) (buffer/10  + (1.0f - (mobPatch.getOriginal().getAttributeValue(StarAttributes.HIT_STAMINA_LOSE.get()))));
                }
            }
            iac.setStamina((iac.getStamina() - ((this.lastGetImpact * this.staminaLoseMultiply) * reduceX)));
            if (iac.getStamina() < (this.lastGetImpact * this.staminaLoseMultiply) * reduceX) {
                stunType = StunType.NEUTRALIZE;
                mobPatch.playSound(EpicFightSounds.NEUTRALIZE_MOBS.get(), -0.05F, 0.1F);
                if (this.lastAttacker != null)
                    EpicFightParticles.AIR_BURST.get().spawnParticleWithArgument((ServerLevel) mobPatch.getOriginal().level(), mobPatch.getOriginal(), lastAttacker);
                iac.setStamina(iac.getMaxStamina());
            }
        }

        if(mobPatch instanceof IAnimationEventCapability iec && iec.getEventManager().hasStunEvent()){
            if(mobPatch.getHitAnimation(stunType) != null){
                for(LivingEntityPatchEvent.StunEvent event: iec.getEventManager().getStunEvents()) {
                    event.testAndExecute(mobPatch, lastAttacker, stunType.ordinal());
                    if(!mobPatch.getOriginal().isAlive() || !iec.getEventManager().hasStunEvent()){break;}
                }
            }
        }

        if(stunType != StunType.NONE) {
            resetWhenStunned();
        }
        return stunType;
    }
    @Shadow
    private void resetWhenStunned() {
    }
    @Inject(method = "tryProcess",at = @At("HEAD"), cancellable = true)
    private void tryProcess(DamageSource damageSource, float amount, CallbackInfoReturnable<AttackResult> cir) {
        EpicFightDamageSource epicFightDamageSource = StarDataUnit.getEpicFightDamageSources(damageSource);
        if (((MobPatch<?>) this.mobPatch instanceof IAdvancedCapability iac) && epicFightDamageSource != null && StarDataUnit.isNoGuard(epicFightDamageSource.getAnimation())) {
            cir.setReturnValue(new AttackResult(AttackResult.ResultType.SUCCESS, amount));
            cir.cancel();
        }
    }
}
