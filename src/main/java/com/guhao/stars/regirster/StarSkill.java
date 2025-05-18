package com.guhao.stars.regirster;

import com.guhao.stars.StarsMod;
import com.guhao.stars.efmex.StarSkillCategories;
import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.efmex.skills.ShadowPassive;
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

    @SubscribeEvent
    public static void registerSkills(SkillBuildEvent event) {
        SkillBuildEvent.ModRegistryWorker modRegistry = event.createRegistryWorker(StarsMod.MODID);
        SHADOW_PASSIVE = modRegistry.build("shadow_passive", ShadowPassive::new, Skill.createBuilder().setResource(Skill.Resource.NONE).setCategory(SkillCategories.WEAPON_PASSIVE));
        DOTE = modRegistry.build("dote", DOTEPassive::new, Skill.createBuilder().setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.NONE).setCategory(StarSkillCategories.DOTE));
    }
}
