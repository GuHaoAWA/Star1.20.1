package com.guhao.stars.regirster;

import com.guhao.stars.StarsMod;
import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.efmex.skills.SeeThrough1;
import com.guhao.stars.efmex.skills.SeeThrough2;
import com.guhao.stars.efmex.skills.ShadowPassive;
import net.corruptdog.cdm.skill.identity.SeeThrough;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

@Mod.EventBusSubscriber(modid = StarsMod.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class StarSkill {
    public StarSkill() {}
    public static Skill SHADOW_PASSIVE;
    public static Skill DOTE;
    public static Skill SEE_THROUGH_1;
    public static Skill SEE_THROUGH_2;

    @SubscribeEvent
    public static void registerSkills(SkillBuildEvent event) {
        SkillBuildEvent.ModRegistryWorker modRegistry = event.createRegistryWorker(StarsMod.MODID);
        SHADOW_PASSIVE = modRegistry.build("shadow_passive", ShadowPassive::new, Skill.createBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        DOTE = modRegistry.build("dote", DOTEPassive::new, Skill.createBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.NONE).setCategory(StarSkillCategories.DOTE));
        SEE_THROUGH_1 = modRegistry.build("counter_danger", SeeThrough1::new, SeeThrough1.createSeeThroughSkillBuilder());
        SEE_THROUGH_2 = modRegistry.build("counter_danger_reinforce", SeeThrough2::new, SeeThrough2.createSeeThroughSkillBuilder());

    }
}
