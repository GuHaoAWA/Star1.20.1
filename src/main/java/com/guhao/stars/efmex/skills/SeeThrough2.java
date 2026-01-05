package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.entity.StarAttributes;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.corruptdog.cdm.gameasset.CorruptAnimations;
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
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("removal")
// TODO 中级识破
public class SeeThrough2 extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("4e189a70-e24f-4e6f-b433-ac98b80281c4");


    public SeeThrough2(Builder builder) {
        super(builder);
    }

    public static Builder createSeeThrough2Builder() {
        return (new Builder())
                .setCategory(StarSkillCategories.COUNTER)
                .setActivateType(ActivateType.DURATION)
                .setResource(Resource.NONE);
    }

    public void onRemoved(SkillContainer container) {
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TARGET_INDICATOR_ALERT_CHECK_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CAST_EVENT, EVENT_UUID);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        if (container.getDataManager().getDataValue(StarSkillDataKeys.COUNTER_TICK.get()) > 0 && container.getExecutor().getOriginal() instanceof ServerPlayer serverPlayer) {
            container.getDataManager().setDataSync(StarSkillDataKeys.COUNTER_TICK.get(),
                    container.getDataManager().getDataValue(StarSkillDataKeys.COUNTER_TICK.get()) - 1.0f, serverPlayer);
        }
    }

    @Override
    public void onInitiate(SkillContainer container) {
        PlayerEventListener listener = container.getExecutor().getEventListener();


        if (!container.getDataManager().hasData(StarSkillDataKeys.COUNTER_TICK.get())) {
            container.getDataManager().registerData(StarSkillDataKeys.COUNTER_TICK.get());
            container.getDataManager().setData(StarSkillDataKeys.COUNTER_TICK.get(), 0.0f);
        }


        listener.addEventListener(PlayerEventListener.EventType.TARGET_INDICATOR_ALERT_CHECK_EVENT, EVENT_UUID, (event) -> {
            DynamicAnimation animation = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().get();
            LivingEntity target = event.getPlayerPatch().getTarget();
            if (animation instanceof StaticAnimation staticAnimation && staticAnimation == CorruptAnimations.RECOGNITION && target != null) {
                event.setCanceled(false);
            }
        });

        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.SKILL_CAST_EVENT,
                EVENT_UUID,
                (SkillCastEvent event) -> {
                    carryout(event, container);
                }
        );


        // 危反攻击
//        listener.addEventListener(BASIC_ATTACK_EVENT, EVENT_UUID, (event) -> {
////            获取玩家当前正在播放的动画的资源位置
//            ResourceLocation rl = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().get().getRegistryName();
//            if (rl == CorruptAnimations.PARRY_BREAK1.get().getRegistryName()) { //特殊招架硬直
//                event.setCanceled(true);        // 取消普通攻击
//                System.out.println("2222222222222");
//                //使用强化攻击
//                event.getPlayerPatch().playAnimationSynchronized(CorruptAnimations.LETHAL_SLICING_ONCE, 0.1F);
//            }
//        });

        //踩刀
        listener.addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, (event) -> {
            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getDirectEntity(), LivingEntityPatch.class);
            if (entitypatch == null || entitypatch.getAnimator() == null) {
                return;
            }
            ///////////////////////////////////////
            AdvancedCustomHumanoidMobPatch<?> longpatch = EpicFightCapabilities.getEntityPatch(event.getDamageSource().getDirectEntity(), AdvancedCustomHumanoidMobPatch.class);
////////////////////////////////////////////////
            DynamicAnimation animation = Objects.requireNonNull(event.getPlayerPatch().getAnimator().getPlayerFor(null)).getAnimation().get();
            DynamicAnimation targetanimation = Objects.requireNonNull(entitypatch.getAnimator().getPlayerFor(null)).getAnimation().get();

            DamageSource damagesource = event.getDamageSource();
            Vec3 sourceLocation = damagesource.getSourcePosition();
            StaticAnimation[] attackAnimations = AnimationEffectManager.getSpecialSeethroughAnimations().toArray(new StaticAnimation[0]);
            StaticAnimation[] dodgeAnimations = new StaticAnimation[]{
                    Animations.BIPED_STEP_FORWARD.get(),
                    CorruptAnimations.STEP_FORWARD.get(),
                    CorruptAnimations.SSTEP_FORWARD.get()
            };
            for (StaticAnimation attackAnim : attackAnimations) {
                if (targetanimation == attackAnim) {
                    if (sourceLocation != null) {
                        Vec3 playerPosition = event.getPlayerPatch().getOriginal().position();
                        Vec3 viewVector = event.getPlayerPatch().getOriginal().getViewVector(1.0F);
                        Vec3 toSourceLocation = sourceLocation.subtract(playerPosition).normalize();
                        double dotProduct = toSourceLocation.dot(viewVector);
                        if (dotProduct > Math.cos(Math.toRadians(180))) {
                            for (StaticAnimation dodgeAnim : dodgeAnimations) {
                                if (animation == dodgeAnim) {
                                    event.setCanceled(true);
                                    Player player = event.getPlayerPatch().getOriginal();
                                    Vec3 entityViewVector = entitypatch.getOriginal().getViewVector(1.0F);
                                    player.teleportTo(entitypatch.getOriginal().getX() + entityViewVector.x() * 2.0, entitypatch.getOriginal().getY(), entitypatch.getOriginal().getZ() + entityViewVector.z() * 2.0);
                                    event.getPlayerPatch().playAnimationSynchronized(CorruptAnimations.RECOGNITION, 0F);
                                    event.getPlayerPatch().getOriginal().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 3));
                                    event.setResult(AttackResult.ResultType.MISSED);
                                    entitypatch.playAnimationSynchronized(CorruptAnimations.RECOGNIZED, 0.1F);
                                    ////////////////////////////////////////////////////////////
                                    container.getExecutor().setStamina(container.getExecutor().getStamina() + (float)event.getPlayerPatch().getOriginal().getAttributeValue(StarAttributes.SEETHROUGH_REGEN.get()));
                                    if (longpatch != null) {
                                        longpatch.setStamina(longpatch.getStamina() - 2.0f - longpatch.getMaxStamina() * 0.08f);
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

    //执行招架危反
    private void carryout(SkillCastEvent event, SkillContainer container) {
        //按下武器技能键时触发强化技能
        if(event.getSkillContainer() != event.getPlayerPatch().getSkill(SkillSlots.WEAPON_INNATE))return;
        var rl = event.getPlayerPatch().getAnimator().getPlayerFor(null).getAnimation().get().getRealAnimation();
        if(rl == CorruptAnimations.PARRY_BREAK2) { //特殊招架硬直
//            取消技能
            event.setCanceled(true);
            // 使用强化技能
            event.getPlayerPatch().playAnimationSynchronized(CorruptAnimations.LETHAL_SLICING_ONCE, 0.1F);
        }
        else if(rl == CorruptAnimations.PARRY_BREAK3) { //特殊招架硬直
//            取消技能
            event.setCanceled(true);
            // 使用强化技能
            event.getPlayerPatch().playAnimationSynchronized(CorruptAnimations.LETHAL_SLICING_TWICE, 0.1F);
        }
    }

    public static class Builder extends SkillBuilder<SeeThrough2> {
    }
}