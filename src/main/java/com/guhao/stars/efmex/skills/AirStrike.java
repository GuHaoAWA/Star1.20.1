package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.DealDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

import java.util.UUID;
// TODO 高级的紫危处理，跳a与二段跳踩头功能都有
public class AirStrike extends Skill {
    private static final UUID AIR_STRIKE_UUID = UUID.fromString("071dda48-0cdd-4c92-9787-c0efb1524e8b");
    private static final UUID SKILL_CAST_UUID = UUID.fromString("7776e296-4528-4baf-ab62-fd2f48b93bca");


    private static SkillDataKey<Integer> getAirStrikeKey() {
        return com.guhao.stars.efmex.StarSkillDataKeys.AIR_STRIKEKEY.get();
    }
    private static final int EMPOWERED_TIME = 60; // 40 tick = 2秒


    public AirStrike(AirStrike.Builder builder) {
        super(builder);
    }


    public static Builder createAirStrikeBuilder() {
        return (new AirStrike.Builder())
                .setCategory(SkillCategories.MOVER)
                .setActivateType(ActivateType.DURATION)
                .setResource(Resource.NONE);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);

//        攻击命中后的事件
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_DAMAGE,
                AIR_STRIKE_UUID,
                (event) -> {
                    handleAirStrikeDamage(event, container);
                },
                0 // 优先级
        );
//        技能施放监听器
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.SKILL_CAST_EVENT,
                SKILL_CAST_UUID,
                (event) -> {
                    handleSkillCast(event, container);
                },
                0
        );
    }

    private void handleAirStrikeDamage(DealDamageEvent.Damage event, SkillContainer container) {
        //检查是否是跳A攻击
//        System.out.println("4444444444");
        if(!isJumpAttack(event))return;
//        被攻击实体
        LivingEntity target = event.getTarget();
        if(target == null)return;
//        event.getDamageSource().getEntity()  伤害源实体
//        检查目标当前是否在播放紫危动画
        if(!isTargetPlayingBypassAllAnimation(target))return;
        EntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(target, EntityPatch.class);
        boolean isEpicFightEntity = entityPatch != null;
        if(isEpicFightEntity) {
            if (entityPatch instanceof LivingEntityPatch<?> livingPatch) {
                livingPatch.playAnimationSynchronized(Animations.BIPED_KNOCKDOWN, 0.1F);
            }
        }


        //设置强化技能时间
        container.getDataManager().setDataSync(getAirStrikeKey(), EMPOWERED_TIME);

    }


    //技能施放
    private void handleSkillCast(SkillCastEvent event, SkillContainer container) {
        Integer remainingTime = container.getDataManager().getDataValue(getAirStrikeKey());
        if (remainingTime!=null&&remainingTime>0) {
            PlayerPatch<?> playerPatch = event.getPlayerPatch();
            playerPatch.playAnimationSynchronized(Animations.GREATSWORD_AIR_SLASH, -0.1F);
            container.getDataManager().setDataSync(getAirStrikeKey(), 0);


//          取消原技能施放
            event.setCanceled(true);


        }
    }



    //检测是否是跳A攻击
    private boolean isJumpAttack(DealDamageEvent.Damage event) {
        if(!(event.getDamageSource().getEntity() instanceof LivingEntity attacker)) {
            return false;
        }
        LivingEntityPatch<?> attackerPatch = EpicFightCapabilities.getEntityPatch(attacker, LivingEntityPatch.class);
        if(attackerPatch == null||attackerPatch.getAnimator() == null)return false;
//        获取当前播放的动画
        var animPlayer = attackerPatch.getAnimator().getPlayerFor(null);
        if(animPlayer == null) return false;
        var animation = animPlayer.getAnimation();
        if(animation == null)return false;
        StaticAnimation currentAnim = animation.get().getRealAnimation().get();
        return currentAnim instanceof AirSlashAnimation;
    }



    //检查攻击目标是否在播放紫危动画
    private boolean isTargetPlayingBypassAllAnimation(LivingEntity target) {
        LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(target, LivingEntityPatch.class);
        if(entityPatch != null && entityPatch.getAnimator() != null) {
            var currentAnim = entityPatch.getAnimator().getPlayerFor(null).getAnimation().get().getRealAnimation().get();
            if(currentAnim != null) {
                return AnimationEffectManager.shouldBypassAll(currentAnim);
            }
        }
        return false;
    }

    //移除技能
    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        //移除事件监听器
        container.getExecutor().getEventListener().removeListener(
                PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_DAMAGE,
                AIR_STRIKE_UUID
        );
//        移除技能施放监听器
        container.getExecutor().getEventListener().removeListener(
                PlayerEventListener.EventType.SKILL_CAST_EVENT,
                SKILL_CAST_UUID
        );
        container.getDataManager().setData(getAirStrikeKey(), 0);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        Integer remainingTime = container.getDataManager().getDataValue(getAirStrikeKey());
        if(remainingTime != null && remainingTime > 0) {
            int newTime = remainingTime - 1;
            container.getDataManager().setDataSync(getAirStrikeKey(), newTime);
            if(newTime <= 0) {
                container.getDataManager().setDataSync(getAirStrikeKey(), 0);
            }
        }
    }

    //构建器类
    public static class Builder extends SkillBuilder<AirStrike> {}
}