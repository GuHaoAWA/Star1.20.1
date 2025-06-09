package com.guhao.stars.units;

import com.guhao.epicfight.GuHaoAnimations;
import com.guhao.stars.efmex.StarAnimations;
import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.minecraft.world.damagesource.DamageSource;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.Arrays;

public record StarArrayUnit() {


    static final StaticAnimation[] GUARD;
    static final StaticAnimation[] PARRY;
    static final StaticAnimation[] DODGE;
    static final StaticAnimation[] CAIDAO;
    static {//无视格挡red
        GUARD = new StaticAnimation[]{
                Animations.TSUNAMI_REINFORCED,
                Animations.WRATHFUL_LIGHTING,
                Animations.REVELATION_TWOHAND,
                WOMAnimations.GESETZ_SPRENGKOPF,
                WOMAnimations.TORMENT_AUTO_1,
                WOMAnimations.SOLAR_BRASERO_CREMATORIO,
                WOMAnimations.SOLAR_BRASERO_INFIERNO,
                WOMAnimations.STRONG_KICK,
////////////////////////////////////////////////////////////////zi
                WOMAnimations.TORMENT_AUTO_1,
                WOMAnimations.RUINE_CHATIMENT,
                WOMAnimations.SOLAR_QUEMADURA,
                WOMAnimations.SOLAR_AUTO_2_POLVORA,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_LAYED,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
                CorruptAnimations.LETHAL_SLICING_ONCE1,
                CorruptAnimations.KATANA_SHEATHING_DASH_DAWN,
                CorruptAnimations.FATAL_DRAW_DAWN,
                CorruptAnimations.BLADE_RUSH1_DAWN,
                CorruptAnimations.BLADE_RUSH3_DAWN,
                CorruptAnimations.YAMATO_DAWN_DAWN,
//                CorruptAnimations.YAMATO_POWER_DASH_DAWN,
////////////////////////////////////////////////////////////////
                StarAnimations.SCRATCH,
                StarAnimations.KILL,
                StarAnimations.EVIL_BLADE,
                GuHaoAnimations.NB_ATTACK,
                GuHaoAnimations.GUHAO_BATTOJUTSU_DASH,
                GuHaoAnimations.GUHAO_BIU,
                GuHaoAnimations.BLOOD_JUDGEMENT,
                GuHaoAnimations.DENG_LONG,
        };//只能完美org
        PARRY = new StaticAnimation[]{
                Animations.SPEAR_DASH,
                Animations.LONGSWORD_DASH,
                Animations.REVELATION_ONEHAND,
                WOMAnimations.HERRSCHER_AUTO_2,
                WOMAnimations.STAFF_KINKONG,
                WOMAnimations.SOLAR_HORNO,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                WOMAnimations.GESETZ_AUTO_3,
                WOMAnimations.RUINE_REDEMPTION,
                WOMAnimations.RUINE_COMET,
                WOMAnimations.STRONG_PUNCH,
                WOMAnimations.AGONY_AUTO_1,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4,
                CorruptAnimations.SSPEAR_DASH,
                CorruptAnimations.LONGSWORD_OLD_DASH,
                CorruptAnimations.UCHIGATANA_DASH,
                CorruptAnimations.UCHIGATANA_HEAVY1,
                CorruptAnimations.DUAL_TACHI_DASH,
                CorruptAnimations.BLADE_RUSH4,
                CorruptAnimations.BLADE_RUSH_FINISHER,
                CorruptAnimations.YAMATO_POWER3_FINISH,
        };//purple 无视格挡+闪避的下段紫危：
        DODGE = new StaticAnimation[]{
                StarAnimations.KILL,
                WOMAnimations.TORMENT_AUTO_1,
                WOMAnimations.RUINE_CHATIMENT,
                WOMAnimations.SOLAR_QUEMADURA,
                WOMAnimations.SOLAR_AUTO_2_POLVORA,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_LAYED,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
                CorruptAnimations.LETHAL_SLICING_ONCE1,
                CorruptAnimations.KATANA_SHEATHING_DASH_DAWN,
                CorruptAnimations.FATAL_DRAW_DAWN,
                CorruptAnimations.BLADE_RUSH1_DAWN,
                CorruptAnimations.BLADE_RUSH3_DAWN,
                CorruptAnimations.YAMATO_DAWN_DAWN,
//                CorruptAnimations.YAMATO_POWER_DASH_DAWN,
        };
        CAIDAO = new StaticAnimation[]{
                Animations.SPEAR_DASH,
                Animations.LONGSWORD_DASH,
                WOMAnimations.HERRSCHER_AUTO_2,
                WOMAnimations.STAFF_KINKONG,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                WOMAnimations.RUINE_COMET,
                WOMAnimations.AGONY_AUTO_1,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4,
                CorruptAnimations.SSPEAR_DASH,
                CorruptAnimations.LONGSWORD_OLD_DASH,
                CorruptAnimations.UCHIGATANA_DASH,
                CorruptAnimations.UCHIGATANA_HEAVY1,
                CorruptAnimations.DUAL_TACHI_DASH,
                CorruptAnimations.BLADE_RUSH4,
                CorruptAnimations.BLADE_RUSH_FINISHER,
        };
    }

    public static EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }
    public static StaticAnimation[] getGuard() {
        return GUARD;
    }
    public static StaticAnimation[] getParry() {
        return PARRY;
    }
    public static StaticAnimation[] getcaidao() {
        return CAIDAO;
    }
    public static boolean isNoGuard(StaticAnimation staticAnimation) {
        return Arrays.asList(GUARD).contains(staticAnimation);
    }
    public static boolean isNoParry(StaticAnimation staticAnimation) {
        return Arrays.asList(PARRY).contains(staticAnimation);
    }
    public static boolean isNoDodge(StaticAnimation staticAnimation) {
        return Arrays.asList(DODGE).contains(staticAnimation);
    }
    public static boolean canCaiDAO(StaticAnimation staticAnimation) {
        return Arrays.asList(CAIDAO).contains(staticAnimation);
    }
}
