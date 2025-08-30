package com.guhao.stars.efmex;

import com.guhao.stars.efmex.skills.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ImpactGuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;

import java.util.function.Supplier;

import static com.guhao.stars.StarsMod.MODID;

@SuppressWarnings("removal")
public class StarSkillDataKeys {
    // 使用正确的注册表构建器
    private static final Supplier<RegistryBuilder<SkillDataKey<?>>> BUILDER =
            () -> new RegistryBuilder<SkillDataKey<?>>().addCallback(SkillDataKey.getRegistryCallback());

    // 使用正确的资源位置和注册表
    public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS =
            DeferredRegister.create(ResourceLocation.fromNamespaceAndPath("epicfight", "skill_data_keys"), MODID);

    public static final Supplier<IForgeRegistry<SkillDataKey<?>>> REGISTRY = DATA_KEYS.makeRegistry(BUILDER);

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

    public static final RegistryObject<SkillDataKey<Float>> COUNTER_TICK =
            DATA_KEYS.register("counter_tick", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.FLOAT, 0.0f, true,
                    SeeThrough1.class
            ));

    public static final RegistryObject<SkillDataKey<Float>> COUNTER_TICK2 =
            DATA_KEYS.register("counter_tick2", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.FLOAT, 0.0f, true,
                    SeeThrough2.class
            ));

    public static final RegistryObject<SkillDataKey<Float>> COUNTER_TICK3 =
            DATA_KEYS.register("counter_tick3", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.FLOAT, 0.0f, true,
                    SeeThrough2.class
            ));

    //雾凇
    public static final RegistryObject<SkillDataKey<Boolean>> WUSONG_SHEATH =
            DATA_KEYS.register("wusong_sheath", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.BOOLEAN, false, false,
                    WuSongPassive.class
            ));

    public static final RegistryObject<SkillDataKey<Integer>> CHECK1 =
            DATA_KEYS.register("check1", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER, 0, true,
                    WuSongPassive.class
            ));

    public static final RegistryObject<SkillDataKey<Integer>> CHECK2 =
            DATA_KEYS.register("check2", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER, 0, true,
                    WuSongPassive.class
            ));

    public static final RegistryObject<SkillDataKey<Integer>> CHECK3 =
            DATA_KEYS.register("check3", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER, 0, true,
                    WuSongPassive.class
            ));

    //THE WORLD
    public static final RegistryObject<SkillDataKey<Integer>> TIME_TICK =
            DATA_KEYS.register("time_tick", () -> SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER, 900, true,
                    TimeStopPassive.class
            ));
}
