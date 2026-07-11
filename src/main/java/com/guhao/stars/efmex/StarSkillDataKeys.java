package com.guhao.stars.efmex;

import com.guhao.stars.efmex.skills.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ImpactGuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;

import static com.guhao.stars.StarsMod.MODID;

public class StarSkillDataKeys {
    // 使用正确的资源位置和注册表
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS =
            DeferredRegister.create(ResourceLocation.fromNamespaceAndPath("epicfight", "skill_data_keys"), MODID);

    //dote
    public static final RegistryObject<SkillDataKey<Float>> WEAKNESS =
            DATA_KEYS.register("weakness", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.FLOAT, 0.0f, true,
                    DOTEPassive.class, GuardSkill.class, ParryingSkill.class, ImpactGuardSkill.class
            ));

    public static final RegistryObject<SkillDataKey<Float>> WEAKNESS_COUNT_2 =
            DATA_KEYS.register("weakness_count_2", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.FLOAT, 0.0f, true,
                    DOTEPassive.class, GuardSkill.class, ParryingSkill.class, ImpactGuardSkill.class
            ));

    public static final RegistryObject<SkillDataKey<Float>> COUNTER_TICK = DATA_KEYS.register("counter_tick", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.FLOAT, 0.0f, true, SeeThrough1.class));

    public static final RegistryObject<SkillDataKey<Integer>> JUMP_COUNT1 = DATA_KEYS.register("jump_count1", () -> SkillDataKey.createSkillDataKey(
            PacketBufferCodec.INTEGER, 0, false, JumpStrike.class));
    public static final RegistryObject<SkillDataKey<Boolean>> PROTECT_NEXT_FALL1 = DATA_KEYS.register("protect_next_fall1", () -> SkillDataKey.createSkillDataKey(
            PacketBufferCodec.BOOLEAN, false, false, JumpStrike.class));
    public static final RegistryObject<SkillDataKey<Boolean>> JUMP_KEY_PRESSED_LAST_TICK1 = DATA_KEYS.register("jump_key_pressed_last_tick1", () -> SkillDataKey.createSkillDataKey(
            PacketBufferCodec.BOOLEAN, false, false, JumpStrike.class));


    public static final RegistryObject<SkillDataKey<Integer>> JUMP_COUNT2 = DATA_KEYS.register("jump_count2", () -> SkillDataKey.createSkillDataKey(
            PacketBufferCodec.INTEGER, 0, false, AirStrike.class));
    public static final RegistryObject<SkillDataKey<Boolean>> PROTECT_NEXT_FALL2 = DATA_KEYS.register("protect_next_fall2", () -> SkillDataKey.createSkillDataKey(
            PacketBufferCodec.BOOLEAN, false, false, AirStrike.class));
    public static final RegistryObject<SkillDataKey<Boolean>> JUMP_KEY_PRESSED_LAST_TICK2 = DATA_KEYS.register("jump_key_pressed_last_tick2", () -> SkillDataKey.createSkillDataKey(
            PacketBufferCodec.BOOLEAN, false, false, AirStrike.class));



    //THE WORLD
    public static final RegistryObject<SkillDataKey<Integer>> TIME_TICK =
            DATA_KEYS.register("time_tick", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER, 900, true,
                    TimeStopPassive.class
            ));
}
