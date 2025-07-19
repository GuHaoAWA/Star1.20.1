package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.regirster.Effect;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

import java.util.Iterator;

@Mixin(value = yesman.epicfight.events.EntityEvents.class , remap = false)
public class EntityEvents {
    @Inject(method = "hurtEvent",at = @At("HEAD"), cancellable = true)
    private static void star$hurtEvent(LivingHurtEvent event, CallbackInfo ci) {
        ci.cancel();
        EpicFightDamageSource epicFightDamageSource = null;
        Entity trueSource = event.getSource().getEntity();
        LivingEntityPatch<?> attackerEntityPatch;
        if (trueSource != null) {
            attackerEntityPatch = EpicFightCapabilities.getEntityPatch(trueSource, LivingEntityPatch.class);
            float baseDamage = event.getAmount();
            DamageSource var6 = event.getSource();
            if (var6 instanceof EpicFightDamageSource) {
                epicFightDamageSource = (EpicFightDamageSource)var6;
            } else if (event.getSource().isIndirect() && event.getSource().getDirectEntity() != null) {
                ProjectilePatch<?> projectileCap = EpicFightCapabilities.getEntityPatch(event.getSource().getDirectEntity(), ProjectilePatch.class);
                if (projectileCap != null) {
                    epicFightDamageSource = projectileCap.getEpicFightDamageSource(event.getSource());
                }
            } else if (attackerEntityPatch != null) {
                epicFightDamageSource = attackerEntityPatch.getEpicFightDamageSource();
                baseDamage = attackerEntityPatch.getModifiedBaseDamage(baseDamage);
            }

            if (epicFightDamageSource != null && !epicFightDamageSource.is(EpicFightDamageType.PARTIAL_DAMAGE)) {
                LivingEntity hitEntity = event.getEntity();
                if (attackerEntityPatch instanceof ServerPlayerPatch playerpatch) {
                    DealtDamageEvent.Hurt dealDamageHurt = new DealtDamageEvent.Hurt(playerpatch, hitEntity, epicFightDamageSource, event);
                    playerpatch.getEventListener().triggerEvents(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_HURT, dealDamageHurt);
                }

                float totalDamage = epicFightDamageSource.getDamageModifier().getTotalValue(baseDamage);
                if (trueSource instanceof LivingEntity livingEntity) {
                    ExtraDamageInstance extraDamage;
                    if (epicFightDamageSource.getExtraDamages() != null) {
                        for(Iterator<ExtraDamageInstance> var8 = epicFightDamageSource.getExtraDamages().iterator(); var8.hasNext(); totalDamage += extraDamage.get(livingEntity, epicFightDamageSource.getHurtItem(), hitEntity, baseDamage)) {
                            extraDamage = var8.next();
                        }
                    }
                }

                HurtableEntityPatch<?> hitHurtableEntityPatch = EpicFightCapabilities.getEntityPatch(hitEntity, HurtableEntityPatch.class);
                LivingEntityPatch<?> hitLivingEntityPatch = EpicFightCapabilities.getEntityPatch(hitEntity, LivingEntityPatch.class);
                ServerPlayerPatch hitPlayerPatch = EpicFightCapabilities.getEntityPatch(hitEntity, ServerPlayerPatch.class);
                if (hitPlayerPatch != null) {
                    HurtEvent.Post hurtEvent = new HurtEvent.Post(hitPlayerPatch, epicFightDamageSource, totalDamage);
                    hitPlayerPatch.getEventListener().triggerEvents(PlayerEventListener.EventType.HURT_EVENT_POST, hurtEvent);
                    totalDamage = hurtEvent.getAmount();
                }

                event.setAmount(totalDamage);
                if (epicFightDamageSource.is(EpicFightDamageType.EXECUTION)) {
                    float amount = event.getAmount();
                    event.setAmount(2.14748365E9F);
                    if (hitLivingEntityPatch != null) {
                        int executionResistance = hitLivingEntityPatch.getExecutionResistance();
                        if (executionResistance > 0) {
                            hitLivingEntityPatch.setExecutionResistance(executionResistance - 1);
                            event.setAmount(amount);
                        }
                    }
                }

                if (event.getAmount() > 0.0F && hitHurtableEntityPatch != null) {
                    StunType stunType = epicFightDamageSource.getStunType();
                    float stunTime = 0.0F;
                    float knockBackAmount = 0.0F;
                    float stunShield = hitHurtableEntityPatch.getStunShield();
                    if (stunShield > epicFightDamageSource.getImpact() && (stunType == StunType.SHORT || stunType == StunType.LONG)) {
                        stunType = StunType.NONE;
                    }

                    hitHurtableEntityPatch.setStunShield(stunShield - epicFightDamageSource.getImpact());
                    boolean isLongStun;
                    switch (stunType) {
                        case SHORT:
                            stunType = StunType.NONE;
                            if (!hitEntity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get()) && hitHurtableEntityPatch.getStunShield() == 0.0F) {
                                float totalStunTime = (0.25F + epicFightDamageSource.getImpact() * 0.1F) * (1.0F - hitHurtableEntityPatch.getStunReduction());
                                if (totalStunTime >= 0.075F) {
                                    stunTime = totalStunTime - 0.1F;
                                    isLongStun = totalStunTime >= 0.83F;
                                    stunTime = isLongStun ? 0.83F : stunTime;
                                    stunType = isLongStun ? StunType.LONG : StunType.SHORT;
                                    knockBackAmount = Math.min(isLongStun ? epicFightDamageSource.getImpact() * 0.05F : totalStunTime, 2.0F);
                                }

                                stunTime = (float)((double)stunTime * (1.0 - hitEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                            }
                            break;
                        case LONG:
                            stunType = hitEntity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get()) ? StunType.NONE : StunType.LONG;
                            knockBackAmount = Math.min(epicFightDamageSource.getImpact() * 0.05F, 5.0F);
                            stunTime = 0.83F;
                            break;
                        case HOLD:
                            stunType = StunType.SHORT;
                            stunTime = epicFightDamageSource.getImpact() * 0.25F;
                            if (event.getEntity().hasEffect(Effect.REALLY_STUN_IMMUNITY.get())) {
                                stunType = StunType.NONE;
                                stunTime = 0.0f;
                            }

                            break;
                        case KNOCKDOWN:
                            stunType = hitEntity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get()) ? StunType.NONE : StunType.KNOCKDOWN;
                            knockBackAmount = Math.min(epicFightDamageSource.getImpact() * 0.05F, 5.0F);
                            stunTime = 2.0F;
                            break;
                        case NEUTRALIZE:
                            stunType = StunType.NEUTRALIZE;
                            hitHurtableEntityPatch.playSound(EpicFightSounds.NEUTRALIZE_MOBS.get(), 3.0F, 0.0F, 0.1F);
                            EpicFightParticles.AIR_BURST.get().spawnParticleWithArgument((ServerLevel)hitEntity.level(), hitEntity, event.getSource().getDirectEntity());
                            knockBackAmount = 0.0F;
                            stunTime = 2.0F;
                    }

                    Vec3 sourcePosition = epicFightDamageSource.getInitialPosition();
                    hitHurtableEntityPatch.setStunReductionOnHit(stunType);
                    isLongStun = hitHurtableEntityPatch.applyStun(stunType, stunTime);
                    if (sourcePosition != null) {
                        if (!(hitEntity instanceof Player) && isLongStun) {
                            hitEntity.lookAt(EntityAnchorArgument.Anchor.FEET, sourcePosition);
                        }

                        if (knockBackAmount > 0.0F) {
                            knockBackAmount *= 40.0F / hitHurtableEntityPatch.getWeight();
                            hitHurtableEntityPatch.knockBackEntity(sourcePosition, knockBackAmount);
                        }
                    }
                }
            }
        } else if (event.getSource().is(DamageTypes.FALL) && event.getAmount() > 1.0F && event.getEntity().level().getGameRules().getBoolean(EpicFightGamerules.HAS_FALL_ANIMATION)) {
            attackerEntityPatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), LivingEntityPatch.class);
            if (attackerEntityPatch != null && !attackerEntityPatch.getEntityState().inaction()) {
                StaticAnimation fallAnimation = attackerEntityPatch.getAnimator().getLivingAnimation(LivingMotions.LANDING_RECOVERY, attackerEntityPatch.getHitAnimation(StunType.FALL));
                if (fallAnimation != null) {
                    attackerEntityPatch.playAnimationSynchronized(fallAnimation, 0.0F);
                }
            }
        }

    }

}
