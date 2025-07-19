package com.guhao.stars.efmex.skills;

import com.google.common.collect.Maps;
import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.regirster.Effect;
import com.guhao.stars.units.StarArrayUnit;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiFunction;

import static yesman.epicfight.world.entity.eventlistener.PlayerEventListener.EventType.BASIC_ATTACK_EVENT;


public class SeeThrough1 extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("550e8400-e29b-41d4-a716-496655470020");
    public SeeThrough1(Builder builder) {
        super(builder);
    }
    public static class Builder extends Skill.Builder<SeeThrough1> {
        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>> motions = Maps.newHashMap();


        public SeeThrough1.Builder setActivateType(ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public SeeThrough1.Builder setResource(Resource resource) {
            this.resource = resource;
            return this;
        }

    }
    public static SeeThrough1.Builder createSeeThroughSkillBuilder() {
        return (Builder) (new Builder())
                .setCategory(StarSkillCategories.COUNTER)
                .setActivateType(ActivateType.DURATION)
                .setResource(Resource.NONE);
    }
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.TARGET_INDICATOR_ALERT_CHECK_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID);

    }
    @Override
    public void updateContainer(SkillContainer container) {
        if (container.getDataManager().getDataValue(StarSkillDataKeys.COUNTER_TICK.get()) > 0 && container.getExecuter().getOriginal() instanceof ServerPlayer serverPlayer) {
            container.getDataManager().setDataSync(StarSkillDataKeys.COUNTER_TICK.get(),
                    container.getDataManager().getDataValue(StarSkillDataKeys.COUNTER_TICK.get()) - 1.0f, serverPlayer);
        }
    }

    @Override
    public void onInitiate(SkillContainer container) {
        PlayerEventListener listener = container.getExecuter().getEventListener();


        listener.addEventListener(PlayerEventListener.EventType.TARGET_INDICATOR_ALERT_CHECK_EVENT, EVENT_UUID, (event) -> {
            int animationId = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().getId();
            PlayerPatch<?> playerPatch = container.getExecuter();
            LivingEntity target = playerPatch.getTarget();
            if (animationId == CorruptAnimations.RECOGNITION.getId()  && target != null) {
                event.setCanceled(false);
            }
        });

        listener.addEventListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID, (event) -> {
            if (event.getSkillContainer() != event.getPlayerPatch().getSkill(SkillSlots.WEAPON_INNATE)) {
                return;
            }
            if (container.getDataManager().getDataValue(StarSkillDataKeys.COUNTER_TICK.get()) > 0.0f) {
                event.setCanceled(true);
                if (event.getPlayerPatch().getOriginal() instanceof LocalPlayer serverPlayer) {
                    event.getPlayerPatch().getSkill(this).getDataManager().setDataSync(StarSkillDataKeys.COUNTER_TICK.get(), 0.0f, serverPlayer);
                }
                container.getExecuter().setStamina(container.getExecuter().getStamina() + 1.0f);
                container.getExecuter().playAnimationSynchronized(Animations.RUSHING_TEMPO2,0.0f);
            }
        },-10);

        listener.addEventListener(BASIC_ATTACK_EVENT, EVENT_UUID, (event) -> {
            float stamina = event.getPlayerPatch().getStamina();
            int costStamina = 3;
            ResourceLocation rl = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().getRegistryName();
            if (stamina < 3) {
                if (rl == CorruptAnimations.RECOGNITION.getRegistryName()) {
                    event.setCanceled(true);
                }
            }
            if (rl == CorruptAnimations.RECOGNITION.getRegistryName()) {
                event.setCanceled(true);
                event.getPlayerPatch().playAnimationSynchronized(CorruptAnimations.LETHAL_SLICING_ONCE, 0.1F);
                event.getPlayerPatch().setStamina(stamina -  costStamina);
            }
        });
        listener.addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, (event) -> {
            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getDirectEntity(), LivingEntityPatch.class);
            if (entitypatch == null || entitypatch.getAnimator() == null) {
                return;
            }
            ///////////////////////////////////////
            AdvancedCustomHumanoidMobPatch<?> longpatch = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getDirectEntity(), AdvancedCustomHumanoidMobPatch.class);
            EpicFightDamageSource epicFightDamageSource = StarArrayUnit.getEpicFightDamageSources(event.getDamageSource());
            if (epicFightDamageSource != null && StarArrayUnit.isNoParry(epicFightDamageSource.getAnimation()) && event.isParried()) {
                container.getDataManager().setDataSync(StarSkillDataKeys.COUNTER_TICK.get(), 60.0f, event.getPlayerPatch().getOriginal());
                event.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(Effect.ORANGE_GLOW.get(), 60, 1, true, true));
            }












////////////////////////////////////////////////
            int animationId = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().getId();
            int targetanimationId = entitypatch.getAnimator().getPlayerFor(null).getAnimation().getId();

            DamageSource damagesource = event.getDamageSource();
            Vec3 sourceLocation = damagesource.getSourcePosition();
            StaticAnimation[] attackAnimations = StarArrayUnit.getcaidao();
            StaticAnimation[] dodgeAnimations = new StaticAnimation[]{
                    Animations.BIPED_STEP_FORWARD,
                    CorruptAnimations.STEP_FORWARD,
                    CorruptAnimations.SSTEP_FORWARD
            };
            for (StaticAnimation attackAnim : attackAnimations) {
                if (targetanimationId == attackAnim.getId()) {
                    if (sourceLocation != null) {
                        Vec3 playerPosition = event.getPlayerPatch().getOriginal().position();
                        Vec3 viewVector = event.getPlayerPatch().getOriginal().getViewVector(1.0F);
                        Vec3 toSourceLocation = sourceLocation.subtract(playerPosition).normalize();
                        double dotProduct = toSourceLocation.dot(viewVector);
                        if (dotProduct > Math.cos(Math.toRadians(120))) {
                            for (StaticAnimation dodgeAnim : dodgeAnimations) {
                                if (animationId == dodgeAnim.getId()) {
                                    event.setCanceled(true);
                                    Player player = event.getPlayerPatch().getOriginal();
                                    Vec3 entityViewVector = entitypatch.getOriginal().getViewVector(1.0F);
                                    player.teleportTo(entitypatch.getOriginal().getX() + entityViewVector.x() * 2.0, entitypatch.getOriginal().getY(), entitypatch.getOriginal().getZ() + entityViewVector.z() * 2.0);
                                    event.getPlayerPatch().playAnimationSynchronized(CorruptAnimations.RECOGNITION, 0F);
                                    event.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 3));
                                    event.setResult(AttackResult.ResultType.MISSED);
                                    entitypatch.playAnimationSynchronized(CorruptAnimations.RECOGNIZED, 0.1F);
                                    ////////////////////////////////////////////////////////////
                                    container.getExecuter().setStamina(container.getExecuter().getStamina() + 2.5f);
                                    if (longpatch != null) {
                                        longpatch.setStamina(longpatch.getStamina()-2.0f-longpatch.getMaxStamina()*0.02f);
                                    }
////////////////////////////////////////////////////////////
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });
    }
}