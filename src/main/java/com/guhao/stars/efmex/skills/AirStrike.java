package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.efmex.StarSkillDataKeys;
import com.guhao.stars.utils.dangerAnimSystem.AnimationEffectManager;
import com.hm.efn.gameasset.animations.EFNGreatSwordAnimations;
import com.p1nero.invincible.api.animation.types.MultiPhaseAirSlashAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.DealDamageEvent;
import yesman.epicfight.world.entity.eventlistener.MovementInputEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
// TODO 高级的紫危处理，跳a与二段跳踩头功能都有
public class AirStrike extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("071dda48-0cdd-4c92-9787-c0efb1524e8b");



    private final List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> phantomAnimations = new ArrayList<>(2);
    private int extraJumps = 1; // 默认额外跳跃次数
    private double jumpPower = 0.42; // 默认跳跃力度
    private float consumption = 0.2F; // 技能消耗


    private static SkillDataKey<Integer> getAirStrikeKey() {
        return StarSkillDataKeys.AIR_STRIKEKEY.get();
    }
    private static final int EMPOWERED_TIME = 60; // 40 tick = 2秒


    public AirStrike(AirStrike.Builder builder) {
        super(builder);
//        初始化二段跳动画
        this.phantomAnimations.add(StarAnimations.BIPED_PHANTOM_ASCENT_FORWARD_NEW);
        this.phantomAnimations.add(StarAnimations.BIPED_PHANTOM_ASCENT_BACKWARD_NEW);
    }


    public static Builder createAirStrikeBuilder() {
        return (new AirStrike.Builder())
                .setCategory(SkillCategories.MOVER)
                .setActivateType(ActivateType.DURATION)
                .setResource(Resource.NONE);


    }

    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        this.extraJumps = parameters.getInt("extra_jumps");
        this.consumption = 0.2F;
        this.jumpPower = parameters.getDouble("jump_power");
    }



    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.setStack(1);
//        攻击命中后的事件
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_DAMAGE,
                EVENT_UUID,
                (event) -> {
                    handleAirStrikeDamage(event, container);
                },
                0 // 优先级
        );



        //空袭技能释放
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SKILL_CAST_EVENT, EVENT_UUID, (event) -> {
            if (container.getExecutor().isLogicalClient()) {
                Skill skill = event.getSkillContainer().getSkill();
                if (skill.getCategory() == SkillCategories.WEAPON_INNATE) {
                    Integer remainingTime = container.getDataManager().getDataValue(getAirStrikeKey());
                    if(remainingTime!=null&&remainingTime>0) {
                        PlayerPatch<?> playerPatch = event.getPlayerPatch();
                        playerPatch.playAnimationSynchronized(EFNGreatSwordAnimations.NG_GREATSWORD_AIRSLASH, 0.0F);
                        container.getDataManager().setDataSync(getAirStrikeKey(), 0);
                        event.setCanceled(true);
                    }
                }


            }
        });


        //移动输入事件监听
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT,
                EVENT_UUID,
                (event) -> {
                    handleJumpStrike(event, container);
                },
                0
        );

        //取消下次坠落伤害
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_HURT,
                EVENT_UUID,
                (event) -> {
                    if (event.getDamageSource().is(DamageTypeTags.IS_FALL) &&
                            container.getDataManager().getDataValue(SkillDataKeys.PROTECT_NEXT_FALL.get())) {
                        float damage = event.getDamage();

                        if(damage < 2.5F) {
                            event.attachValueModifier(ValueModifier.setter(0.0F));
                        }

                        container.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), false);
                    }
                },
                0
        );

//        重置跳跃计数器
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.FALL_EVENT,
                EVENT_UUID,
                (event) -> {
                    container.getDataManager().setData(SkillDataKeys.JUMP_COUNT.get(), 0);

                    if (event.getPlayerPatch().isLogicalClient()) {
                        container.getDataManager().setData(SkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK.get(), false);
                    }
                },
                0
        );

        SkillDataManager skillDataManager = container.getDataManager();
        skillDataManager.registerData(SkillDataKeys.JUMP_COUNT.get());
        skillDataManager.registerData(SkillDataKeys.PROTECT_NEXT_FALL.get());
        skillDataManager.registerData(SkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK.get());

    }

    private void handleAirStrikeDamage(DealDamageEvent.Damage event, SkillContainer container) {
        //检查是否是跳A攻击
        if(!isJumpAttack(event))return;
        LivingEntity target = event.getTarget();
        if(target == null)return;
//        event.getDamageSource().getEntity()  伤害源实体
//        检查目标当前是否在播放紫危动画
        if(!isTargetPlayingBypassAllAnimation(target))return;
        EntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(target, EntityPatch.class);
        boolean isEpicFightEntity = entityPatch != null;
        if(isEpicFightEntity) {
//            System.out.println("3333333333     ");
            if (entityPatch instanceof LivingEntityPatch<?> livingPatch) {
                //击倒目标
                livingPatch.playAnimationSynchronized(Animations.BIPED_KNOCKDOWN, 0.1F);
            }
        }
        //设置强化技能时间
        container.getDataManager().setDataSync(getAirStrikeKey(), EMPOWERED_TIME);

    }



    //检测是否是跳A攻击
    private boolean isJumpAttack(DealDamageEvent.Damage event) {
        if(event.getDamageSource().getEntity() instanceof LivingEntity attacker) {
            LivingEntityPatch<?> attackerPatch = EpicFightCapabilities.getEntityPatch(attacker, LivingEntityPatch.class);
            if(attackerPatch == null||attackerPatch.getAnimator() == null)return false;
//        获取当前播放的动画
            var animPlayer = attackerPatch.getAnimator().getPlayerFor(null);
            if(animPlayer == null) return false;
            var animation = animPlayer.getAnimation();
            if(animation == null)return false;
            StaticAnimation currentAnim = animation.get().getRealAnimation().get();
            boolean p=(currentAnim instanceof AirSlashAnimation)||(currentAnim instanceof  MultiPhaseAirSlashAnimation);
            return p;
        }
        return false;

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


    //二段跳
    private void handleJumpStrike(MovementInputEvent event, SkillContainer container) {

        if (event.getPlayerPatch().getOriginal().getVehicle() != null ||
                !event.getPlayerPatch().isEpicFightMode() ||
                event.getPlayerPatch().getOriginal().getAbilities().flying ||
                event.getPlayerPatch().isHoldingAny() ||
                event.getPlayerPatch().getEntityState().inaction()) {
            return;
        }


        boolean jumpPressed = Minecraft.getInstance().options.keyJump.isDown();
        boolean jumpPressedPrev = container.getDataManager().getDataValue(SkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK.get());

        if (jumpPressed && !jumpPressedPrev) {
            int jumpCounter = container.getDataManager().getDataValue(SkillDataKeys.JUMP_COUNT.get());

            if (jumpCounter > 0 || event.getPlayerPatch().currentLivingMotion == LivingMotions.FALL) {
                if (jumpCounter < (this.extraJumps + 1)) {
                    SkillCastEvent skillexecuteevent = new SkillCastEvent(container.getExecutor(), container, null);
                    container.getExecutor().getEventListener().triggerEvents(PlayerEventListener.EventType.SKILL_CAST_EVENT, skillexecuteevent);

                    if (skillexecuteevent.isCanceled()) {
                        return;
                    }

                    container.setResource(0.0F);

//                    更新跳跃计数器
                    if (jumpCounter == 0 && event.getPlayerPatch().currentLivingMotion == LivingMotions.FALL) {
                        container.getDataManager().setData(SkillDataKeys.JUMP_COUNT.get(), 2);
                    } else {
                        container.getDataManager().setDataF(SkillDataKeys.JUMP_COUNT.get(), (v) -> v + 1);
                    }

                    container.getDataManager().setDataSync(SkillDataKeys.PROTECT_NEXT_FALL.get(), true);

//                    计算跳跃方向
                    Input input = event.getMovementInput();
                    float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(container.getExecutor().getOriginal()), 0.0F, 1.0F);
                    input.tick(false, f);

                    int forward = event.getMovementInput().up ? 1 : 0;
                    int backward = event.getMovementInput().down ? -1 : 0;
                    int left = event.getMovementInput().left ? 1 : 0;
                    int right = event.getMovementInput().right ? -1 : 0;
                    int vertic = forward + backward;
                    int horizon = left + right;
                    int degree = -(90 * horizon * (1 - Math.abs(vertic)) + 45 * vertic * horizon);
                    int scale = forward == 0 && backward == 0 && left == 0 && right == 0 ? 0 : (vertic < 0 ? -1 : 1);

                    Vec3 forwardHorizontal = Vec3.directionFromRotation(new Vec2(0, container.getExecutor().getOriginal().getViewYRot(1.0F)));
                    Vec3 jumpDir = OpenMatrix4f.transform(OpenMatrix4f.createRotatorDeg(-degree, Vec3f.Y_AXIS), forwardHorizontal.scale(0.15D * scale));
                    Vec3 deltaMove = container.getExecutor().getOriginal().getDeltaMovement();

                    container.getExecutor().getOriginal().setDeltaMovement(
                            deltaMove.x + jumpDir.x,
                            this.jumpPower + container.getExecutor().getOriginal().getJumpBoostPower(),
                            deltaMove.z + jumpDir.z
                    );

                    event.getPlayerPatch().setModelYRot(container.getExecutor().getOriginal().getYRot() + degree, true);
                    event.getPlayerPatch().playAnimationSynchronized(this.phantomAnimations.get(vertic < 0 ? 1 : 0), 0.0F);

                    //释放所有按下的键
                    ClientEngine.getInstance().controlEngine.releaseAllServedKeys();
                }
            } else {
                container.getDataManager().setData(SkillDataKeys.JUMP_COUNT.get(), 1);
            }
        }

        // 更新上次跳跃键状态
        container.getDataManager().setData(SkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK.get(), jumpPressed);
    }

    @Override
    public boolean canExecute(SkillContainer container) {
        return false;
    }



    //移除技能
    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        //移除事件监听器
        container.getExecutor().getEventListener().removeListener(
                PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_DAMAGE,
                EVENT_UUID
        );
//        移除技能施放监听器
        container.getExecutor().getEventListener().removeListener(
                PlayerEventListener.EventType.SKILL_CAST_EVENT,
                EVENT_UUID
        );


        // 移除二段跳事件监听器
        container.getExecutor().getEventListener().removeListener(
                PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT,
                EVENT_UUID
        );

        container.getExecutor().getEventListener().removeListener(
                PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_HURT,
                EVENT_UUID
        );

        container.getExecutor().getEventListener().removeListener(
                PlayerEventListener.EventType.FALL_EVENT,
                EVENT_UUID
        );



        container.getDataManager().setData(getAirStrikeKey(), 0);
        container.getDataManager().setData(SkillDataKeys.JUMP_COUNT.get(), 0);
        container.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), false);
        container.getDataManager().setData(SkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK.get(), false);


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


    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Object> getTooltipArgsOfScreen(List<Object> list) {
        list.add(this.extraJumps);
        return list;
    }

    //构建器类
    public static class Builder extends SkillBuilder<AirStrike> {}
}