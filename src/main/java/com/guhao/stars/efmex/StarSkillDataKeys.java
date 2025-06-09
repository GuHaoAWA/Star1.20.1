package com.guhao.stars.efmex;

import com.guhao.stars.efmex.skills.DOTEPassive;
import com.guhao.stars.efmex.skills.SeeThrough1;
import com.guhao.stars.efmex.skills.SeeThrough2;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ImpactGuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;

import static com.guhao.stars.StarsMod.MODID;

public class StarSkillDataKeys {
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(new ResourceLocation("epicfight", "skill_data_keys"), MODID);
    public StarSkillDataKeys() {
    }
    public static final RegistryObject<SkillDataKey<Float>> WEAKNESS = DATA_KEYS.register("weakness", () -> SkillDataKey.createFloatKey(0.0f,true, DOTEPassive.class, GuardSkill.class, ParryingSkill.class, ImpactGuardSkill.class));
    public static final RegistryObject<SkillDataKey<Float>> WEAKNESS_COUNT_2 = DATA_KEYS.register("weakness_count_2", () -> SkillDataKey.createFloatKey(0.0f,true, DOTEPassive.class, GuardSkill.class, ParryingSkill.class, ImpactGuardSkill.class));
    public static final RegistryObject<SkillDataKey<Float>> COUNTER_TICK = DATA_KEYS.register("counter_tick", () -> SkillDataKey.createFloatKey(0.0f,true, DOTEPassive.class, SeeThrough1.class, SeeThrough2.class));
    public static final RegistryObject<SkillDataKey<Float>> COUNTER_TICK2 = DATA_KEYS.register("counter_tick2", () -> SkillDataKey.createFloatKey(0.0f,true, DOTEPassive.class, SeeThrough2.class));


}
