package com.guhao.stars.regirster;

import com.guhao.stars.StarsMod;
import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.skills.*;
import net.corruptdog.cdm.skill.identity.SeeThrough;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

@Mod.EventBusSubscriber(modid = StarsMod.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class StarSkill {
    public StarSkill() {}
    public static Skill SHADOW_PASSIVE;
    public static Skill DOTE;
    public static Skill SEE_THROUGH_1;
    public static Skill SEE_THROUGH_2;
    public static Skill WUSONG_PASSIVE;
    public static Skill WUSONG_SKILL;

    @SubscribeEvent
    public static void registerSkills(SkillBuildEvent event) {
        SkillBuildEvent.ModRegistryWorker modRegistry = event.createRegistryWorker(StarsMod.MODID);
        SHADOW_PASSIVE = modRegistry.build("shadow_passive", ShadowPassive::new, Skill.createBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        WUSONG_PASSIVE = modRegistry.build("wusong_passive", WuSongPassive::new, Skill.createBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.COOLDOWN).setCategory(SkillCategories.WEAPON_PASSIVE));
        WUSONG_SKILL = modRegistry.build("wusong_skill", WuSongSkill::new, WeaponInnateSkill.createWeaponInnateBuilder());
        DOTE = modRegistry.build("dote", DOTEPassive::new, Skill.createBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.NONE).setCategory(StarSkillCategories.DOTE));
        SEE_THROUGH_1 = modRegistry.build("counter_danger", SeeThrough1::new, SeeThrough1.createSeeThroughSkillBuilder());
        SEE_THROUGH_2 = modRegistry.build("counter_danger_reinforce", SeeThrough2::new, SeeThrough2.createSeeThroughSkillBuilder());

    }
}
