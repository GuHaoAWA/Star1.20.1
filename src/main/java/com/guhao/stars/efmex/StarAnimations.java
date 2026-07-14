package com.guhao.stars.efmex;

import com.asanginxst.epicfightx.client.sound.EFXSounds;
import com.guhao.stars.StarsMod;
import com.guhao.stars.regirster.StarSkill;
import com.guhao.stars.regirster.StarsEffect;
import com.guhao.stars.regirster.StarsKey;
import com.guhao.stars.regirster.StarsSounds;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import com.hm.efn.EFNClientConfig;
import com.hm.efn.animations.types.EFNGuardAnimation;
import com.hm.efn.client.effek.BurstRedEffek;
import com.hm.efn.entity.EFNVFXManagers;
import com.hm.efn.gameasset.EFNAnimations;
import com.hm.efn.gameasset.EFNExtraDamageInstance;
import com.hm.efn.registries.EFNMobEffectRegistry;
import com.hm.efn.util.EffectConditionParticleTrail;
import com.hm.efn.util.EffectEntityInvoker;
import com.hm.efn.util.EffekUnits;
import com.merlin204.avalon.epicfight.animations.AvalonAttackAnimation;
import com.merlin204.avalon.util.AvalonAnimationUtils;
import com.merlin204.avalon.util.AvalonEventUtils;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mod.EventBusSubscriber(
        modid = StarsMod.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class StarAnimations {
    public static AnimationAccessor<ActionAnimation> BIPED_PHANTOM_ASCENT_FORWARD_NEW;
    public static AnimationAccessor<ActionAnimation> BIPED_PHANTOM_ASCENT_BACKWARD_NEW;
    public static AnimationManager.AnimationAccessor<StaticAnimation> JUMP;
    public static AnimationManager.AnimationAccessor<EFNGuardAnimation> EFN_GUARD_ACTIVE_HIT3;
    //名刀横居合boss专用(下段)
    public static AnimationManager.AnimationAccessor<AttackAnimation> MOONVEIL_HORIZONTAL_BOSS;
    //关刀下段
    public static AnimationManager.AnimationAccessor<AttackAnimation> FALCHION_EX2;
    public static AnimationManager.AnimationAccessor<AvalonAttackAnimation> NF_MEEN_CHARGE2;
    public static AnimationManager.AnimationAccessor<AvalonAttackAnimation> NF_TACHI_BLOODLUST;
    //长剑突刺
    public static AnimationManager.AnimationAccessor<AttackAnimation> SWEEPING_EDGE;
    //旧突刺
    public static AnimationManager.AnimationAccessor<AttackAnimation> OLD_THRUST;
    public static AnimationManager.AnimationAccessor<AttackAnimation> LONGSWORD_BACK_TSUNAMI;


    public static StaticAnimation FIRE_BALL;
    public static StaticAnimation AB_FIRE_BALL;
    public static StaticAnimation SCRATCH;
    public static StaticAnimation KILL;
    public static StaticAnimation EVIL_BLADE;
    public static StaticAnimation HANGDANG;
    public static StaticAnimation AOXUE;
    public static StaticAnimation AOXUE2;
    public static StaticAnimation THE_WORLD;
    public static StaticAnimation FIST_AUTO_1;
    public static StaticAnimation FIST_AUTO_2;
    public static StaticAnimation FIST_AUTO_3;
    public static StaticAnimation FIST_AUTO_4;
    public static StaticAnimation OLA;

    public StarAnimations() {
    }

    @SubscribeEvent
    public static void registerAnimations(AnimationManager.AnimationRegistryEvent event) {
        event.newBuilder(StarsMod.MODID, StarAnimations::build);
    }

    private static void build(AnimationManager.AnimationBuilder builder) {
//        HumanoidArmature biped = Armatures.BIPED.get();
        FALCHION_EX2 = builder.nextAccessor("biped/falchion/falchion_ex2", (accessor) ->
                new AttackAnimation(0.05F, 0.05F, 0.95F, 1.05F, 1.2F, StarNewColliderPreset.FALCHION_EX2, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.6F))
                        .addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 5)
                        .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.05F)
                        .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.5F)
                        .addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER)
                        .setResourceLocation("efn", "biped/falchion/falchion_ex2"));



        MOONVEIL_HORIZONTAL_BOSS = builder.nextAccessor("biped/boss/moonveil_horizontal_boss", (accessor) ->
                new AttackAnimation(0.01F, 0.01F, 0.19F, 0.51F, 1.05F, StarNewColliderPreset.MOONVEIL_HORIZONTAL_BOSS, Armatures.BIPED.get().rootJoint, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.95F))         //伤害倍率
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(1.5F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, ((dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1.1F)));




        EFN_GUARD_ACTIVE_HIT3 = builder.nextAccessor("biped/nf_skill/biped_flashblock3", (accessor) ->
                new EFNGuardAnimation(0.1F, 1.1F, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                        .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(new float[]{0.05F, 0.4F}))
                        .setResourceLocation("efn", "biped/nf_skill/biped_flashblock3"));
//                        .addEvents(new AnimationEvent[]{AvalonEventUtils.simpleCameraShake(3, 11, 6.0F, 3.0F, 6.0F)}));



        JUMP = builder.nextAccessor("biped/living/jump", (accessor) ->
                new StaticAnimation(0.1F, false, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true));


        BIPED_PHANTOM_ASCENT_FORWARD_NEW = builder.nextAccessor("biped/living/phantom_ascent_forward_new", (accessor) ->
                new ActionAnimation(0.05F, 0.7F, accessor, Armatures.BIPED)
                        .addStateRemoveOld(EntityState.MOVEMENT_LOCKED, false)
                        .newTimePair(0.0F, 0.5F)
                        .addStateRemoveOld(EntityState.INACTION, true)
                        .addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS,
                                AnimationEvent.SimpleEvent.create((entityPatch, animation, params) -> {
                                    LivingEntity original = entityPatch.getOriginal();
                                    Vec3 footPos = original.position();
                                    double radius = 5.5;
                                    AABB boundingBox = new AABB(
                                            footPos.x - radius, footPos.y - radius, footPos.z - radius,
                                            footPos.x + radius, footPos.y + radius, footPos.z + radius
                                    );
                                    List<LivingEntity> entitiesInRange = original.level().getEntitiesOfClass(LivingEntity.class, boundingBox);

                                    for(LivingEntity entity : entitiesInRange) {
                                        if (entity == original) continue;
                                        double dx = entity.getX() - footPos.x;
                                        double dy = entity.getY() - footPos.y;
                                        double dz = entity.getZ() - footPos.z;
                                        double distanceSq = dx * dx + dy * dy + dz * dz;

                                        if(distanceSq <= radius * radius) {

                                            LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
                                            if (livingEntityPatch != null) {
                                                if(entity.hasEffect(StarsEffect.UNSTABLE.get())) {
                                                    if(entity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())){
                                                        entity.removeEffect(EpicFightMobEffects.STUN_IMMUNITY.get());
                                                    }
                                                    if(entity.hasEffect(EFNMobEffectRegistry.SIN_STUN_IMMUNITY.get())){
                                                        entity.removeEffect(EFNMobEffectRegistry.SIN_STUN_IMMUNITY.get());
                                                    }
                                                    entityPatch.getOriginal().level().playSound(null, entityPatch.getOriginal().blockPosition(), StarsSounds.PENG.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                                                    livingEntityPatch.applyStun(StunType.LONG, 10.0f);
                                                    entity.removeEffect(StarsEffect.UNSTABLE.get());
                                                    AdvancedCustomHumanoidMobPatch<?> attackerPatch = EpicFightCapabilities.getEntityPatch(entity, AdvancedCustomHumanoidMobPatch.class);
                                                    if (attackerPatch != null) {
                                                        float currentStamina = attackerPatch.getStamina();
                                                        float maxStamina = attackerPatch.getMaxStamina();
                                                        float staminaDecrease = maxStamina * 0.06F + 8.0F;
                                                        attackerPatch.setStamina(Math.max(0.1f, currentStamina - staminaDecrease));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }, AnimationEvent.Side.SERVER),
                                AnimationEvent.SimpleEvent.create((livingEntityPatch, animation, params) -> {
                                    Vec3 pos = livingEntityPatch.getOriginal().position();
                                    livingEntityPatch.playSound(EpicFightSounds.TUMBLE.get(), 0.0F, 0.0F);
                                    livingEntityPatch.getOriginal().level().addAlwaysVisibleParticle(
                                            EpicFightParticles.AIR_BURST.get(),
                                            pos.x,
                                            pos.y + livingEntityPatch.getOriginal().getBbHeight() * 0.5D,
                                            pos.z,
                                            0, -1, 2
                                    );
                                }, AnimationEvent.Side.CLIENT)
                        )
        );


        BIPED_PHANTOM_ASCENT_BACKWARD_NEW = builder.nextAccessor("biped/living/phantom_ascent_backward_new", (accessor) ->
                new ActionAnimation(0.05F, 1.3F, accessor, Armatures.BIPED)

                        .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                        .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0F, 0.9F))
                        .addStateRemoveOld(EntityState.MOVEMENT_LOCKED, false)
                        .newTimePair(0.0F, 0.9F).addStateRemoveOld(EntityState.INACTION, true)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (animation, entitypatch, speed, prevElapsed, elapsed) -> 1.0F)
                        .addEvents(AnimationEvent.InTimeEvent.create(0.33F, (entityPatch, animation, params) -> {
                                    LivingEntity livingEntity = entityPatch.getOriginal();

                                    Vec3 footPos = livingEntity.position();
                                    double radius = 5.5;
                                    AABB boundingBox = new AABB(
                                            footPos.x - radius, footPos.y - radius, footPos.z - radius,
                                            footPos.x + radius, footPos.y + radius, footPos.z + radius
                                    );
                                    List<LivingEntity> entitiesInRange = livingEntity.level().getEntitiesOfClass(LivingEntity.class, boundingBox);

                                    for(LivingEntity entity : entitiesInRange) {
                                        if (entity == livingEntity) continue;
                                        double dx = entity.getX() - footPos.x;
                                        double dy = entity.getY() - footPos.y;
                                        double dz = entity.getZ() - footPos.z;
                                        double distanceSq = dx * dx + dy * dy + dz * dz;
                                        if(distanceSq <= radius * radius) {
                                            LivingEntityPatch<?> livingEntityPatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
                                            if (livingEntityPatch != null) {
                                                if(entity.hasEffect(StarsEffect.UNSTABLE.get())) {
                                                    if(entity.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())){
                                                        entity.removeEffect(EpicFightMobEffects.STUN_IMMUNITY.get());
                                                    }
                                                    if(entity.hasEffect(EFNMobEffectRegistry.SIN_STUN_IMMUNITY.get())){
                                                        entity.removeEffect(EFNMobEffectRegistry.SIN_STUN_IMMUNITY.get());
                                                    }
                                                    entityPatch.getOriginal().level().playSound(null, entityPatch.getOriginal().blockPosition(), StarsSounds.PENG.get(), SoundSource.BLOCKS, 10.0f, 1.0f);
                                                    livingEntityPatch.applyStun(StunType.LONG, 10.0f);
                                                    entity.removeEffect(StarsEffect.UNSTABLE.get());
                                                    AdvancedCustomHumanoidMobPatch<?> attackerPatch = EpicFightCapabilities.getEntityPatch(entity, AdvancedCustomHumanoidMobPatch.class);
                                                    if (attackerPatch != null) {
                                                        float currentStamina = attackerPatch.getStamina();
                                                        float maxStamina = attackerPatch.getMaxStamina();
                                                        float staminaDecrease = maxStamina * 0.06F + 8.0F;
                                                        attackerPatch.setStamina(Math.max(0.1f, currentStamina - staminaDecrease));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }, AnimationEvent.Side.SERVER),
                                AnimationEvent.InTimeEvent.create(0.33F, (livingEntityPatch, animation, params) -> {
                                    Vec3 pos = livingEntityPatch.getOriginal().position();
                                    livingEntityPatch.playSound(EpicFightSounds.TUMBLE.get(), 0.0F, 0.0F);
                                    livingEntityPatch.getOriginal().level().addAlwaysVisibleParticle(
                                            EpicFightParticles.AIR_BURST.get(),
                                            pos.x,
                                            pos.y,
                                            pos.z,
                                            0, -1, 2
                                    );
                                }, AnimationEvent.Side.CLIENT)
                        )
        );



        NF_MEEN_CHARGE2 = builder.nextAccessor("biped/nf_meen/nf_meen_charge2", (accessor) ->
                (new AvalonAttackAnimation(0.1F, accessor, Armatures.BIPED, 1.1F, 1.0F, new AvalonAttackAnimation.AvalonPhase[]{
                        AvalonAnimationUtils.createSimplePhase(38, 56, 75, InteractionHand.MAIN_HAND, 2.0F, 2.0F, Armatures.BIPED.get().toolR, null)}))
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND,  EpicFightSounds.WHOOSH_BIG.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                        .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.setter(100.0F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10.0F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.EVISCERATE)
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create(), EFNExtraDamageInstance.LOST_HEALTH_DAMAGE_WITH_SCALING_CAP.create(new float[]{0.15F, 40.0F, 100.0F})))
                        .newTimePair(0.0F, Float.MAX_VALUE)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, EFNAnimations.ATTACK_SPEED_CAP_MEEN)
                        .newTimePair(0.0F, 0.2F)
                        .addStateRemoveOld(EntityState.ATTACK_RESULT, DodgeAnimation.DODGEABLE_SOURCE_VALIDATOR)
                        .addEvents(
                                new AnimationEvent[]{EffectEntityInvoker.clearFireWind(75), AnimationEvent.InTimeEvent.create(0.1F, (entitypatch, self, params) ->
                                        (
                                                entitypatch.getOriginal()).addEffect(new MobEffectInstance(EFNMobEffectRegistry.SIN_STUN_IMMUNITY.get(), 20, 10, false, false, false)), AnimationEvent.Side.SERVER), AnimationEvent.InTimeEvent.create(0.1F, (entitypatch, animation, params) -> {
                                            LivingEntity entity =entitypatch.getOriginal();
                                            entity.level().addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()),0.0F,0.0F);
                                        },
                                        AnimationEvent.Side.CLIENT),
                                        AvalonEventUtils.simpleCameraShake(43, 40, 3.0F, 3.0F, 3.0F)})
        );



        NF_TACHI_BLOODLUST = builder.nextAccessor("biped/nf_tachi/nf_tachi_bloodlust", (accessor) ->
                (
                        new AvalonAttackAnimation(0.1F, accessor, Armatures.BIPED, 1.0F, 1.0F, new AvalonAttackAnimation.AvalonPhase[]{
                                AvalonAnimationUtils.createSimplePhase(38, 48, 55, InteractionHand.MAIN_HAND, 1.7F, 1.7F,  Armatures.BIPED.get().rootJoint, StarNewColliderPreset.MOONVEIL_HORIZONTAL_BOSS)
                        }))
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create(new float[0])))
                        .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, EFNAnimations.ATTACK_SPEED_CAP_TACHI)
                        .setResourceLocation("efn", "biped/nf_tachi/nf_tachi_bloodlust"));

        SWEEPING_EDGE= builder.nextAccessor("biped/skill/sweeping_edge", (accessor) ->
                new AttackAnimation(0.05F, 0.0F, 0.15F, 0.6F, 1.0F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))        //吃横扫之刃
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(1.0F))          //额外冲击
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.6F))         //伤害倍率
                        .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(8.0F)) //穿甲
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)

                        .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F)
                        .addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
                        .newTimePair(0.0F, 0.6F)
                        .addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));

        OLD_THRUST = builder.nextAccessor("biped/skill/old_thrust", (accessor) ->
                new AttackAnimation(0.05F, 0.0F, 0.1F, 0.6F, 1.0F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EFXSounds.TACHI_SWING_1.get())     //添加挥剑音效
                        .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))        //吃横扫之刃
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.8F))         //伤害倍率
                        .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.multiplier(15.0F)) //穿甲
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
                        .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.4F)
                        .newTimePair(0.0F, 0.7F)
                        .addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));

        LONGSWORD_BACK_TSUNAMI = builder.nextAccessor("biped/skill/longsword_back_tsunami", (accessor) ->
                new AttackAnimation(0.05F, 0.0F, 0.35F, 0.52F, 1.86F, null, Armatures.BIPED.get().toolR, accessor, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get())     //添加挥剑音效
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.adder(0.5F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.6F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                        .addProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, Set.of(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()))
                        .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.00F)
                        .newTimePair(0.0F, 1.2F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false)
        );








//        BIPED_PHANTOM_ASCENT_BACKWARD = builder.nextAccessor("biped/skill/phantom_ascent_backward", (accessor) ->
//                new ActionAnimation(0.05F, 0.7F, accessor, Armatures.BIPED)
//                        .addStateRemoveOld(EntityState.MOVEMENT_LOCKED, false)
//                        .newTimePair(0.0F, 0.5F)
//                        .addStateRemoveOld(EntityState.INACTION, true)
//                        .addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.SimpleEvent.create((entitypatch, animation, params) -> {
//                            Vec3 pos = entitypatch.getOriginal().position();
//
//                            entitypatch.playSound(EpicFightSounds.TUMBLE.get(), 0, 0);
//                            entitypatch.getOriginal().level().addAlwaysVisibleParticle(EpicFightParticles.AIR_BURST.get(), pos.x, pos.y + entitypatch.getOriginal().getBbHeight() * 0.5D, pos.z, 0, -1, 2);
//                        }, AnimationEvent.Side.CLIENT)));
//
//
//
//        TACHI_AIR_SLASH = builder.nextAccessor("biped/combat/tachi_airslash", (accessor) -> (AirSlashAnimation)
//                (new AirSlashAnimation(0.1F, 0.425F, 0.5F, 1.0F, (Collider)null, ((HumanoidArmature)Armatures.BIPED.get()).toolR, accessor, Armatures.BIPED))
//                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, (SoundEvent) EFXSounds.TACHI_SWING_5.get())
//                        .newTimePair(0.5F, Float.MAX_VALUE).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, false).newTimePair(0.0F, 0.7F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false)
//                        .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 1.2F));





    }

        /*HANGDANG = (new SpecialAttackAnimation(0.2F, 0.35F, 0.35F, 0.75F, 1.05F, WOMWeaponColliders.FATAL_DRAW_DASH, biped.rootJoint, "biped/hangdang", biped))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET);
        AOXUE = new AirSlashAnimation(0.1F, 0.15F, 0.26F, 0.5F, ColliderPreset.DUAL_SWORD_AIR_SLASH, biped.torso, "biped/aoxue", biped);
        AOXUE2 = new DashAttackAnimation(0.06F, 0.08F, 0.05F, 0.11F, 0.65F, StarNewColliderPreset.LETHAL_SLICING1, biped.rootJoint, "biped/new/katana_sheath_dash", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(50.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE,StunType.KNOCKDOWN)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F)).addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get());

        THE_WORLD = (new SpecialAttackNoRotAnimation(0.1F,"biped/timepause", biped,
                new AttackAnimation.Phase(0.0F,0.5F,0.7F,Float.MAX_VALUE,Float.MAX_VALUE,biped.rootJoint,GuHaoColliderPreset.BIG_ATTACK))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(0.1f))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL,true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0F, 1.09F))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(a,b,c,d,e)-> 0.66F)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.51f, (livingEntityPatch, staticAnimation, objects) -> StarBattleUnits.time_stop(livingEntityPatch), AnimationEvent.TimeStampedEvent.Side.BOTH)}));
        FIST_AUTO_1 = new AttackAnimation(0.1F,"biped/boxing_a",biped,
                new AttackAnimation.Phase(0.0F,0.17F,0.15F,0.22F,0.3F,Float.MAX_VALUE, InteractionHand.OFF_HAND, biped.toolL,StarNewColliderPreset.SUPER_FIST))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 3.0F)
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOC_TARGET);
        FIST_AUTO_2 = new AttackAnimation(0.1F,"biped/boxing_b",biped,
                new AttackAnimation.Phase(0.0F,0.25F,0.22F,0.35F,0.4F,Float.MAX_VALUE,InteractionHand.MAIN_HAND,biped.toolR,StarNewColliderPreset.SUPER_FIST))
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 3.0F)
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOC_TARGET);
        FIST_AUTO_3 = new AttackAnimation(0.1F,"biped/boxing_c",biped,
                new AttackAnimation.Phase(0.0F,0.14F,0.13F,0.25F,0.72F,0.3F,biped.legL,StarNewColliderPreset.SUPER_FIST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE),
                new AttackAnimation.Phase(0.301F,0.33F,0.29F,0.4F,0.72F,0.45F,biped.legR,StarNewColliderPreset.SUPER_FIST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE),
                new AttackAnimation.Phase(0.451F,0.51F,0.48F,0.62F,0.72F,Float.MAX_VALUE,biped.toolR,StarNewColliderPreset.SUPER_FIST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE)
        )
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 3.0F)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL,true)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOC_TARGET);
        FIST_AUTO_4 = new AttackAnimation(0.1F,"biped/boxing_d",biped,
                new AttackAnimation.Phase(0.0F,0.20F,0.19F,0.25F,0.28F,Float.MAX_VALUE, InteractionHand.OFF_HAND,biped.toolL,StarNewColliderPreset.SUPER_FIST))
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, Sounds.PUNCH_HARD.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.25F))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 3.0F)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET).addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOC_TARGET);
        OLA = new SpecialMoveAttackAnimation(0.1F, "biped/ola", biped,
                new SpecialMoveAttackAnimation.Phase(0.00F, 0.01F, 0.05F, 2.35F, 0.05F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.05F, 0.05F, 0.10F, 2.35F, 0.10F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.10F, 0.10F, 0.15F, 2.35F, 0.15F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.15F, 0.15F, 0.20F, 2.35F, 0.20F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.20F, 0.20F, 0.25F, 2.35F, 0.25F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.25F, 0.25F, 0.30F, 2.35F, 0.30F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.30F, 0.30F, 0.35F, 2.35F, 0.35F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.35F, 0.35F, 0.40F, 2.35F, 0.40F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.40F, 0.40F, 0.45F, 2.35F, 0.45F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.45F, 0.45F, 0.50F, 2.35F, 0.50F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.50F, 0.50F, 0.55F, 2.35F, 0.55F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.55F, 0.55F, 0.60F, 2.35F, 0.60F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.60F, 0.60F, 0.65F, 2.35F, 0.65F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.65F, 0.65F, 0.70F, 2.35F, 0.70F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.70F, 0.70F, 0.75F, 2.35F, 0.75F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.75F, 0.75F, 0.80F, 2.35F, 0.80F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.80F, 0.80F, 0.85F, 2.35F, 0.85F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.85F, 0.85F, 0.90F, 2.35F, 0.90F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.90F, 0.90F, 0.95F, 2.35F, 0.95F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(0.95F, 0.95F, 1.00F, 2.35F, 1.00F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.00F, 1.00F, 1.05F, 2.35F, 1.05F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.05F, 1.05F, 1.10F, 2.35F, 1.10F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.10F, 1.10F, 1.15F, 2.35F, 1.15F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.15F, 1.15F, 1.20F, 2.35F, 1.20F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.20F, 1.20F, 1.25F, 2.35F, 1.25F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.25F, 1.25F, 1.30F, 2.35F, 1.30F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.30F, 1.30F, 1.35F, 2.35F, 1.35F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.35F, 1.35F, 1.40F, 2.35F, 1.40F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.40F, 1.40F, 1.45F, 2.35F, 1.45F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.45F, 1.45F, 1.50F, 2.35F, 1.50F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.50F, 1.50F, 1.55F, 2.35F, 1.55F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.55F, 1.55F, 1.60F, 2.35F, 1.60F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.60F, 1.60F, 1.65F, 2.35F, 1.65F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.65F, 1.65F, 1.70F, 2.35F, 1.70F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.70F, 1.70F, 1.75F, 2.35F, 1.75F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.75F, 1.75F, 1.80F, 2.35F, 1.80F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.80F, 1.80F, 1.85F, 2.35F, 1.85F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.85F, 1.85F, 1.90F, 2.35F, 1.90F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.90F, 1.90F, 1.95F, 2.35F, 1.95F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new SpecialMoveAttackAnimation.Phase(1.95F, 1.95F, 2.00F, 2.35F, 2.00F, InteractionHand.OFF_HAND, biped.toolL, StarNewColliderPreset.SUPER_FIST).addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE).addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.2F)).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD),
                new AttackAnimation.Phase(2.00F, 2.20F, 2.25F, 2.35F, 2.35F, InteractionHand.MAIN_HAND, biped.toolR, StarNewColliderPreset.SUPER_FIST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, ParticleType.AIR_PUNCH_BURST_PARTICLE)
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, Sounds.PUNCH_HARD.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.2F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0F))
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
        )
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(EpicFightDamageType.WEAPON_INNATE))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.setter(10.0F))
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 4)
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOCROT_TARGET)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, MoveCoordFunctions.TRACE_LOC_TARGET)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER,(a,b,c,d,e)-> 1.0F)

                .addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    AttributeInstance speed = entitypatch.getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
                    if (speed != null) {
                        speed.removeModifier(UUID.fromString("0885cf99-5c63-4121-b229-e8f18339ef94"));
                    }
                }, AnimationEvent.Side.SERVER))
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS,new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(2.00f, (livingEntityPatch, staticAnimation, objects) -> {
                            AttributeInstance speed = livingEntityPatch.getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
                            if (speed != null) {
                                speed.removeModifier(UUID.fromString("0885cf99-5c63-4121-b229-e8f18339ef94"));
                            }
                        }, AnimationEvent.TimeStampedEvent.Side.SERVER)})
                .newTimePair(0.0F,2.00F)
                .addStateRemoveOld(EntityState.MOVEMENT_LOCKED,false)
                .newTimePair(2.00F,2.35F)
                .addStateRemoveOld(EntityState.MOVEMENT_LOCKED,true);
    }*/


}
