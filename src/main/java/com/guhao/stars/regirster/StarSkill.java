package com.guhao.stars.regirster;

import com.guhao.stars.StarsMod;
import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.skills.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

@Mod.EventBusSubscriber(modid = StarsMod.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class StarSkill {
    public StarSkill() {}
    public static Skill SHADOW_PASSIVE;
    public static Skill TIME_STOP_PASSIVE;
    public static Skill DOTE;
    public static Skill SEE_THROUGH_1;
    public static Skill SEE_THROUGH_2;
    public static Skill WUSONG_PASSIVE;
    public static Skill WUSONG_SKILL;
    public static Skill THE_WORLD;
    public static Skill SUPER_PUNCH;
    public static Skill SUPER_PUNCH_PASSIVE;

    @SubscribeEvent
    public static void registerSkills(SkillBuildEvent event) {
        SkillBuildEvent.ModRegistryWorker modRegistry = event.createRegistryWorker(StarsMod.MODID);
        SHADOW_PASSIVE = modRegistry.build("shadow_passive", ShadowPassive::new, ShadowPassive.createShadowPassiveBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        TIME_STOP_PASSIVE = modRegistry.build("time_stop_passive", TimeStopPassive::new, TimeStopPassive.createTimeStopPassiveBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        SUPER_PUNCH_PASSIVE = modRegistry.build("super_punch_passive", SuperPunchPassive::new, SuperPunchPassive.createSuperPunchPassiveBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        WUSONG_PASSIVE = modRegistry.build("wusong_passive", WuSongPassive::new, WuSongPassive.createWuSongPassiveBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.COOLDOWN).setCategory(SkillCategories.WEAPON_PASSIVE));
        /*WUSONG_SKILL = modRegistry.build("wusong_skill", WuSongSkill::new, WeaponInnateSkill.createWeaponInnateBuilder());*/
        DOTE = modRegistry.build("dote", DOTEPassive::new, DOTEPassive.createDOTEPassiveBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.NONE).setCategory(StarSkillCategories.DOTE));
        SEE_THROUGH_1 = modRegistry.build("counter_danger", SeeThrough1::new, SeeThrough1.createSeeThrough1Builder());
        SEE_THROUGH_2 = modRegistry.build("counter_danger_reinforce", SeeThrough2::new, SeeThrough2.createSeeThrough2Builder());
        /*THE_WORLD = modRegistry.build("the_world", TimeStop::new, WeaponInnateSkill.createWeaponInnateBuilder());
        SUPER_PUNCH = modRegistry.build("super_punch", SuperPunch::new, WeaponInnateSkill.createWeaponInnateBuilder());*/
    }
}
