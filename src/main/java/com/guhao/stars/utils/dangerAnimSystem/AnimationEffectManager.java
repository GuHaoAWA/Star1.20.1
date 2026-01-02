package com.guhao.stars.utils.dangerAnimSystem;

import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("removal")
public class AnimationEffectManager {

    public static final TagKey<DamageType> BYPASS_GUARD_ONLY = create("star_bypass_guard");     //无视格挡，能招架（黄)
    public static final TagKey<DamageType> BYPASS_PARRY = create("star_bypass_parry");
    public static final TagKey<DamageType> BYPASS_DODGE = create("star_bypass_dodge");     //无视闪避，能招架(蓝)
    // 可踩刀识破的动画
    private static final List<StaticAnimation> SPECIAL_SEETHROUGH_ANIMATIONS = Arrays.asList(
            CorruptAnimations.SSPEAR_DASH.get(),
            CorruptAnimations.LONGSWORD_OLD_DASH.get(),
            CorruptAnimations.UCHIGATANA_DASH.get(),
            CorruptAnimations.UCHIGATANA_HEAVY1.get(),
            CorruptAnimations.DUAL_TACHI_DASH.get(),
            CorruptAnimations.BLADE_RUSH4.get(),
            CorruptAnimations.BLADE_RUSH_FINISHER.get()
    );
    // 红危:不可防御，不可招架的动画列表
    private static final List<StaticAnimation> NO_BLOCK_ANIMATIONS = Arrays.asList(
//            WOMAnimations.STRONG_KICK.get(),
//            AnimsHerrscher.GESETZ_SPRENGKOPF.get(),
//            AnimsSolar.SOLAR_BRASERO_CREMATORIO.get(),
//            AnimsSolar.SOLAR_BRASERO_INFIERNO.get(),
//            AnimsRuine.RUINE_CHATIMENT.get(),
//            AnimsSolar.SOLAR_QUEMADURA.get(),
//            AnimsSolar.SOLAR_AUTO_2_POLVORA.get(),
//            AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_LAYED.get(),
//            AnimsEnderblaster.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT.get(),
//            AnimsEnderblaster.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT.get(),
//            AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_DASH.get(),
            Animations.TSUNAMI_REINFORCED.get(),
            Animations.WRATHFUL_LIGHTING.get(),
            Animations.REVELATION_TWOHAND.get(),
            CorruptAnimations.LETHAL_SLICING_ONCE1.get(),
            CorruptAnimations.KATANA_SHEATHING_DASH_DAWN.get(),
            CorruptAnimations.FATAL_DRAW_DAWN.get(),
            CorruptAnimations.BLADE_RUSH1_DAWN.get(),
            CorruptAnimations.BLADE_RUSH3_DAWN.get(),
            CorruptAnimations.YAMATO_DAWN_DAWN.get()
    );
    // 黄危:不可防御的动画列表
    private static final List<StaticAnimation> NO_GUARD_ANIMATIONS = Arrays.asList(
//            WOMAnimations.STAFF_KINKONG.get(),
//            WOMAnimations.STRONG_PUNCH.get(),
//            AnimsHerrscher.HERRSCHER_AUTO_2.get(),
//            AnimsHerrscher.GESETZ_AUTO_3.get(),
//            AnimsSolar.SOLAR_HORNO.get(),
//            AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_3.get(),
//            AnimsEnderblaster.ENDERBLASTER_TWOHAND_SHOOT_4.get(),
//            AnimsRuine.RUINE_REDEMPTION.get(),
//            AnimsRuine.RUINE_COMET.get(),
//            AnimsAgony.AGONY_AUTO_1.get(),
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
//            WOMAnimations.TORMENT_AUTO_1.get(),
//            AnimsRuine.RUINE_CHATIMENT.get(),
//            AnimsSolar.SOLAR_QUEMADURA.get(),
//            AnimsSolar.SOLAR_AUTO_2_POLVORA.get(),
//            AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_LAYED.get(),
//            AnimsEnderblaster.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT.get(),
//            AnimsEnderblaster.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT.get(),
//            AnimsEnderblaster.ENDERBLASTER_ONEHAND_SHOOT_DASH.get(),
            CorruptAnimations.LETHAL_SLICING_ONCE1.get(),
            CorruptAnimations.KATANA_SHEATHING_DASH_DAWN.get(),
            CorruptAnimations.FATAL_DRAW_DAWN.get(),
            CorruptAnimations.BLADE_RUSH1_DAWN.get(),
            CorruptAnimations.BLADE_RUSH3_DAWN.get(),
            CorruptAnimations.YAMATO_DAWN_DAWN.get()
    );

    //
    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("star", name));
    }


    // 红危：不可格挡不可招架不可闪避的动画列表
    private static boolean shouldBypassBlock(StaticAnimation animation) {
        return animation != null && NO_BLOCK_ANIMATIONS.contains(animation);
    }


    // 黄危：不可格挡的动画列表
    private static boolean shouldBypassGuard(StaticAnimation animation) {
        return animation != null && NO_GUARD_ANIMATIONS.contains(animation);
    }


    // 蓝危：不可闪避的动画列表
    static boolean shouldBypassDodge(StaticAnimation animation) {
        return animation != null && NO_DODGE_ANIMATIONS.contains(animation);
    }

    // 紫危：不可闪避不可招架不可格挡的动画列表
    public static boolean shouldBypassAll(StaticAnimation animation) {
        return animation != null && NO_DODGE_GUARD_ANIMATIONS.contains(animation);
    }

    //不可闪避不可招架不可防御的动画列表
    static boolean specialSeeThrough(StaticAnimation animation) {
        return animation != null && SPECIAL_SEETHROUGH_ANIMATIONS.contains(animation);
    }

    //不可闪避
    public static boolean isNoDodgeAnimation(StaticAnimation animation) {
        return shouldBypassDodge(animation) || shouldBypassAll(animation);
    }

    //无视防御无视招架
    public static boolean isNoGuardParryAnimation(StaticAnimation animation) {
        return shouldBypassBlock(animation) || shouldBypassAll(animation);
    }

    //只无视防御
    public static boolean isNoGuardAnimation(StaticAnimation animation) {
        return shouldBypassGuard(animation) || shouldBypassAll(animation);
    }

    //可识破踩刀
    public static boolean isSpecialSeeThroughAnimation(StaticAnimation animation) {
        return specialSeeThrough(animation);
    }

    //
    public static void processDamageSource(EpicFightDamageSource damageSource) {
        if (damageSource.getAnimation() == null) return;

        StaticAnimation animation = damageSource.getAnimation().get();

        if (shouldBypassBlock(animation)) {
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.GUARD_PUNCTURE);
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.UNBLOCKALBE);
            return;
        }

        if (shouldBypassGuard(animation)) {
            damageSource.addRuntimeTag(AnimationEffectManager.BYPASS_GUARD_ONLY);
        }

        if (shouldBypassDodge(animation)) {
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.BYPASS_DODGE);
            damageSource.addRuntimeTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
        }

        if (shouldBypassAll(animation)) {
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.UNBLOCKALBE);
            damageSource.addRuntimeTag(EpicFightDamageTypeTags.BYPASS_DODGE);
            damageSource.addRuntimeTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
        }
    }

    public static EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }

    public static List<StaticAnimation> getNoBlockAnimations() {
        return NO_BLOCK_ANIMATIONS;
    }

    public static List<StaticAnimation> getNoGuardAnimations() {
        return NO_GUARD_ANIMATIONS;
    }

    public static List<StaticAnimation> getNoDodgeAnimations() {
        return NO_DODGE_ANIMATIONS;
    }

    public static List<StaticAnimation> getNoDodgeGuardAnimations() {
        return NO_DODGE_GUARD_ANIMATIONS;
    }

    public static List<StaticAnimation> getSpecialSeethroughAnimations() {
        return SPECIAL_SEETHROUGH_ANIMATIONS;
    }
}
