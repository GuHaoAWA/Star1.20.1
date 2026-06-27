package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import com.nameless.indestructible.world.capability.Utils.IAdvancedCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("removal")
// TODO 低级识破 招架突刺额外削减耐力
public class SeeThrough1 extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("550e8400-e29b-41d4-a716-496655470020");


    public SeeThrough1(Builder builder) {
        super(builder);
    }

    public static Builder createSeeThrough1Builder() {
        return (new Builder())
                .setCategory(StarSkillCategories.COUNTER)
                .setActivateType(ActivateType.DURATION)
                .setResource(Resource.NONE);
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID);
    }


    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
            if(event.getResult()== AttackResult.ResultType.BLOCKED&&event.isParried()) {
                if(event.getDamageSource().getEntity() instanceof LivingEntity livingEntity) {
                    LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getEntityPatch(livingEntity, LivingEntityPatch.class);
                    if (livingEntityPatch instanceof IAdvancedCapability attackerPatch) {
                        if(event.getDamageSource().is(AnimationEffectManager.BYPASS_GUARD_ONLY)){
                            attackerPatch.setStamina(Math.max(0, (float) (attackerPatch.getStamina() - attackerPatch.getMaxStamina() * 0.03 + 1.0)));
                        }

                    }

                }
            }

        });
    }


    public static class Builder extends SkillBuilder<SeeThrough1> {
    }
}