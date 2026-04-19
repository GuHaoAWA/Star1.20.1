package com.guhao.stars.efmex.skills;

import com.guhao.stars.efmex.StarAnimations;
import com.guhao.stars.efmex.StarSkillDataKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.entity.eventlistener.MovementInputEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
// TODO 低级的紫危处理，只有二段跳踩头功能
public class JumpStrike extends Skill {
    private static final UUID SKILL_CAST_UUID = UUID.fromString("7776e296-4528-4baf-ab62-fd2f48b93bca");
    private static final UUID PHANTOM_ASCENT_UUID = UUID.fromString("e4893864-ae77-4297-9fb5-92967ef2a69d");

    private final List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> phantomAnimations = new ArrayList<>(2);
    private int extraJumps = 1; // 默认额外跳跃次数
    private double jumpPower = 0.42; // 默认跳跃力度
    private float consumption = 0.2F; // 技能消耗

    public JumpStrike(JumpStrike.Builder builder) {
        super(builder);
        //初始化二段跳动画
        this.phantomAnimations.add(StarAnimations.BIPED_PHANTOM_ASCENT_FORWARD_NEW);
        this.phantomAnimations.add(StarAnimations.BIPED_PHANTOM_ASCENT_BACKWARD_NEW);
    }

    public static Builder createJumpStrikeBuilder() {
        return (new JumpStrike.Builder())
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
        //移动输入事件监听
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT,
                PHANTOM_ASCENT_UUID,
                (event) -> {
                    handleJumpStrike(event, container);
                },
                0
        );

        //取消下次坠落伤害
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_HURT,
                PHANTOM_ASCENT_UUID,
                (event) -> {
                    if (event.getDamageSource().is(DamageTypeTags.IS_FALL) &&
                            container.getDataManager().getDataValue(StarSkillDataKeys.PROTECT_NEXT_FALL1.get())) {
                        float damage = event.getDamage();
                        if(damage > 0.0F) {
                            event.attachValueModifier(ValueModifier.setter(0.0F));
                        }
                        container.getDataManager().setData(StarSkillDataKeys.PROTECT_NEXT_FALL1.get(), false);
                    }
                },
                0
        );

        //重置跳跃计数器
        container.getExecutor().getEventListener().addEventListener(
                PlayerEventListener.EventType.FALL_EVENT,
                PHANTOM_ASCENT_UUID,
                (event) -> {
                    container.getDataManager().setData(StarSkillDataKeys.JUMP_COUNT1.get(), 0);

                    if (event.getPlayerPatch().isLogicalClient()) {
                        container.getDataManager().setData(StarSkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK1.get(), false);
                    }
                },
                0
        );

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
        boolean jumpPressedPrev = container.getDataManager().getDataValue(StarSkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK1.get());

        if (jumpPressed && !jumpPressedPrev) {
            int jumpCounter = container.getDataManager().getDataValue(StarSkillDataKeys.JUMP_COUNT1.get());

            if (jumpCounter > 0 || event.getPlayerPatch().currentLivingMotion == LivingMotions.FALL) {
                if (jumpCounter < (this.extraJumps + 1)) {
                    SkillCastEvent skillexecuteevent = new SkillCastEvent(container.getExecutor(), container, null);
                    container.getExecutor().getEventListener().triggerEvents(PlayerEventListener.EventType.SKILL_CAST_EVENT, skillexecuteevent);

                    if (skillexecuteevent.isCanceled()) {
                        return;
                    }

                    container.setResource(0.0F);

                    //更新跳跃计数器
                    if (jumpCounter == 0 && event.getPlayerPatch().currentLivingMotion == LivingMotions.FALL) {
                        container.getDataManager().setData(StarSkillDataKeys.JUMP_COUNT1.get(), 2);
                    } else {
                        container.getDataManager().setDataF(StarSkillDataKeys.JUMP_COUNT1.get(), (v) -> v + 1);
                    }

                    container.getDataManager().setDataSync(StarSkillDataKeys.PROTECT_NEXT_FALL1.get(), true);

                    //计算跳跃方向
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
                container.getDataManager().setData(StarSkillDataKeys.JUMP_COUNT1.get(), 1);
            }
        }

        // 更新上次跳跃键状态
        container.getDataManager().setData(StarSkillDataKeys.JUMP_KEY_PRESSED_LAST_TICK1.get(), jumpPressed);
    }

    @Override
    public boolean canExecute(SkillContainer container) {
        return false;
    }

    //移除技能
    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        //移除二段跳事件监听器
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, PHANTOM_ASCENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_HURT, PHANTOM_ASCENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.FALL_EVENT, PHANTOM_ASCENT_UUID);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Object> getTooltipArgsOfScreen(List<Object> list) {
        list.add(this.extraJumps);
        return list;
    }

    //构建器类
    public static class Builder extends SkillBuilder<JumpStrike> {}
}