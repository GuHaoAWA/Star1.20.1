package com.guhao.stars.regirster;

import com.guhao.stars.effects.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.guhao.stars.StarsMod.MODID;

public class StarsEffect {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final RegistryObject<MobEffect> DEFENSE = REGISTRY.register("defense", Defense::new);
    public static final RegistryObject<MobEffect> UNSTABLE = REGISTRY.register("unstable", Unstable::new);
    public static final RegistryObject<MobEffect> EXECUTE = REGISTRY.register("execute", Execute::new);
    public static final RegistryObject<MobEffect> EXECUTED = REGISTRY.register("executed", Executed::new);
    public static final RegistryObject<MobEffect> REALLY_STUN_IMMUNITY = REGISTRY.register("really_stun_immunity", Really_stun_immunity::new);
    public static final RegistryObject<MobEffect> SLOW_TIME = REGISTRY.register("slowtime", Slow_Time::new);
    public static final RegistryObject<MobEffect> STA = REGISTRY.register("parry_stamina_lose", StaminaReduce::new);
    public static final RegistryObject<MobEffect> TOUGHNESS = REGISTRY.register("toughness", Toughness::new);
    public static final RegistryObject<MobEffect> IMPACT_ENHANCE = REGISTRY.register("impact_enhance", ImpactEnhance::new);
    public static final RegistryObject<MobEffect> ORANGE_GLOW = REGISTRY.register("orange_glow", Orange_Glow::new);
    public static final RegistryObject<MobEffect> RED_GLOW = REGISTRY.register("red_glow", Red_Glow::new);
}
