package com.guhao.stars.regirster;

import com.guhao.stars.StarsMod;
import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.skills.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

@Mod.EventBusSubscriber(modid = StarsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StarSkill {
    public static Skill SHADOW_PASSIVE;
    public static Skill TIME_STOP_PASSIVE;
    public static Skill DOTE;
    public static Skill SEE_THROUGH_1;
    public static Skill SEE_THROUGH_2;
    public static Skill SEE_THROUGH_3;

    public static Skill WUSONG_PASSIVE;
    public static Skill WUSONG_SKILL;
    public static Skill THE_WORLD;
    public static Skill SUPER_PUNCH;
    public static Skill SUPER_PUNCH_PASSIVE;
    public static Skill  AIR_STRIKE;
    public static Skill JUMP_STRIKE;
    public StarSkill() {
    }

    @SubscribeEvent
    public static void registerSkills(SkillBuildEvent event) {
        SkillBuildEvent.ModRegistryWorker modRegistry = event.createRegistryWorker(StarsMod.MODID);
        SHADOW_PASSIVE = modRegistry.build("shadow_passive", ShadowPassive::new, ShadowPassive.createShadowPassiveBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        TIME_STOP_PASSIVE = modRegistry.build("time_stop_passive", TimeStopPassive::new, TimeStopPassive.createTimeStopPassiveBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        SUPER_PUNCH_PASSIVE = modRegistry.build("super_punch_passive", SuperPunchPassive::new, SuperPunchPassive.createSuperPunchPassiveBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
//        WUSONG_PASSIVE = modRegistry.build("wusong_passive", WuSongPassive::new, WuSongPassive.createWuSongPassiveBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.COOLDOWN).setCategory(SkillCategories.WEAPON_PASSIVE));
        /*WUSONG_SKILL = modRegistry.build("wusong_skill", WuSongSkill::new, WeaponInnateSkill.createWeaponInnateBuilder());*/
        // TODO 破防回满耐力，无惩罚
        DOTE = modRegistry.build("dote", DOTEPassive::new, DOTEPassive.createDOTEPassiveBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.NONE).setCategory(StarSkillCategories.DOTE));
//        水平突刺识破
        SEE_THROUGH_1 = modRegistry.build("counter_danger", SeeThrough1::new, SeeThrough1.createSeeThrough1Builder());
//      水平+向下突刺识破
        SEE_THROUGH_2 = modRegistry.build("counter_danger2", SeeThrough2::new, SeeThrough2.createSeeThrough2Builder());
        // T高级识破，水平下砸突刺，下砸
        SEE_THROUGH_3 = modRegistry.build("counter_danger3", SeeThrough3::new, SeeThrough3.createSeeThrough3Builder());



//        高级紫危危反
        AIR_STRIKE = modRegistry.build("air_strike", AirStrike::new, AirStrike.createAirStrikeBuilder());
//        踩头，低级紫危危反
        JUMP_STRIKE = modRegistry.build("jump_strike", JumpStrike::new, JumpStrike.createJumpStrikeBuilder());

        // TODO 招架黄危强力反斩，闪避黄危用体术，暂时禁用，后续再考虑修改反击功能
//        SEE_THROUGH_2 = modRegistry.build("counter_danger_reinforce", SeeThrough2::new, SeeThrough2.createSeeThrough2Builder());
        /*THE_WORLD = modRegistry.build("the_world", TimeStop::new, WeaponInnateSkill.createWeaponInnateBuilder());
        SUPER_PUNCH = modRegistry.build("super_punch", SuperPunch::new, WeaponInnateSkill.createWeaponInnateBuilder());*/
    }
}
