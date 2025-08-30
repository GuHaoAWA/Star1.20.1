// AnimationEffectManager.java
package com.guhao.stars.utils.dangerAnimSystem;

import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.gameasset.Animations;
import net.corruptdog.cdm.gameasset.CorruptAnimations;

import java.util.Arrays;
import java.util.List;

public class AnimationEffectManager {

    // 红危:不可防御，不可招架的动画列表
    private static final List<StaticAnimation> NO_GUARD_ANIMATIONS = Arrays.asList(
            Animations.TSUNAMI_REINFORCED.get(),
            Animations.WRATHFUL_LIGHTING.get(),
            Animations.REVELATION_TWOHAND.get(),
            Animations.RUSHING_TEMPO3.get(),
            CorruptAnimations.LETHAL_SLICING_ONCE1.get(),
            CorruptAnimations.KATANA_SHEATHING_DASH_DAWN.get(),
            CorruptAnimations.FATAL_DRAW_DAWN.get(),
            CorruptAnimations.BLADE_RUSH1_DAWN.get(),
            CorruptAnimations.BLADE_RUSH3_DAWN.get(),
            CorruptAnimations.YAMATO_DAWN_DAWN.get()
    );

    // 黄危:不可防御的动画列表
    private static final List<StaticAnimation> NO_PARRY_ANIMATIONS = Arrays.asList(
            Animations.SPEAR_DASH.get(),
            Animations.LONGSWORD_DASH.get(),
            Animations.REVELATION_ONEHAND.get(),
            CorruptAnimations.SSPEAR_DASH.get(),
            CorruptAnimations.LONGSWORD_OLD_DASH.get(),
            CorruptAnimations.UCHIGATANA_DASH.get(),
            CorruptAnimations.UCHIGATANA_HEAVY1.get(),
            CorruptAnimations.DUAL_TACHI_DASH.get(),
            CorruptAnimations.BLADE_RUSH4.get(),
            CorruptAnimations.BLADE_RUSH_FINISHER.get(),
            CorruptAnimations.YAMATO_POWER3_FINISH.get()
    );

    // 蓝危:不可闪避的动画列表
    private static final List<StaticAnimation> NO_DODGE_ANIMATIONS = Arrays.asList(
            CorruptAnimations.LETHAL_SLICING_ONCE1.get(),
            CorruptAnimations.KATANA_SHEATHING_DASH_DAWN.get(),
            CorruptAnimations.FATAL_DRAW_DAWN.get(),
            CorruptAnimations.BLADE_RUSH1_DAWN.get(),
            CorruptAnimations.BLADE_RUSH3_DAWN.get(),
            CorruptAnimations.YAMATO_DAWN_DAWN.get()
    );

    // 紫危：不可闪避不可招架不可防御的动画列表
    private static final List<StaticAnimation> NO_DODGE_GUARD_ANIMATIONS = Arrays.asList(
            CorruptAnimations.LETHAL_SLICING_ONCE1.get(),
            CorruptAnimations.KATANA_SHEATHING_DASH_DAWN.get(),
            CorruptAnimations.FATAL_DRAW_DAWN.get(),
            CorruptAnimations.BLADE_RUSH1_DAWN.get(),
            CorruptAnimations.BLADE_RUSH3_DAWN.get(),
            CorruptAnimations.YAMATO_DAWN_DAWN.get()
    );

    private static boolean shouldBypassGuard(StaticAnimation animation) {
        return animation != null && NO_GUARD_ANIMATIONS.contains(animation);
    }

    private static boolean shouldBypassParry(StaticAnimation animation) {
        return animation != null && NO_PARRY_ANIMATIONS.contains(animation);
    }

    private static boolean shouldBypassDodge(StaticAnimation animation) {
        return animation != null && NO_DODGE_ANIMATIONS.contains(animation);
    }

    private static boolean shouldBypassAll(StaticAnimation animation) {
        return animation != null && NO_DODGE_GUARD_ANIMATIONS.contains(animation);
    }

    /**
     * 处理伤害源，添加相应的运行时标签
     * 现在对所有执行者（攻击者）都生效
     */
    public static void processDamageSource(EpicFightDamageSource damageSource) {
        if (damageSource.getAnimation() == null) return;

        StaticAnimation animation = damageSource.getAnimation().get();

        if (shouldBypassGuard(animation)) {
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.UNBLOCKALBE);
        }

        if (shouldBypassParry(animation)) {
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.GUARD_PUNCTURE);
        }

        if (shouldBypassDodge(animation)) {
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.BYPASS_DODGE);
        }

        if (shouldBypassAll(animation)) {
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.BYPASS_DODGE);
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.UNBLOCKALBE);
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.GUARD_PUNCTURE);
        }
    }

    // 提供获取方法以便其他地方使用
    public static List<StaticAnimation> getNoGuardAnimations() {
        return NO_GUARD_ANIMATIONS;
    }

    public static List<StaticAnimation> getNoParryAnimations() {
        return NO_PARRY_ANIMATIONS;
    }

    public static List<StaticAnimation> getNoDodgeAnimations() {
        return NO_DODGE_ANIMATIONS;
    }

    public static List<StaticAnimation> getNoDodgeGuardAnimations() {
        return NO_DODGE_GUARD_ANIMATIONS;
    }
}
