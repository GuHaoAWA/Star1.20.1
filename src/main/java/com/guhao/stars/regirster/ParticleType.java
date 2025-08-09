package com.guhao.stars.regirster;


import com.guhao.stars.client.particle.par.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3d;
import yesman.epicfight.particle.HitParticleType;

import static com.guhao.stars.StarsMod.MODID;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleType {
    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLES;
    public static final RegistryObject<SimpleParticleType> DANGER;
    public static final RegistryObject<SimpleParticleType> DANGER_RED;
    public static final RegistryObject<SimpleParticleType> DANGER_BLACK;
    public static final RegistryObject<SimpleParticleType> DANGER_BLUE;
    public static final RegistryObject<SimpleParticleType> DANGER_PURPLE;
    public static final RegistryObject<SimpleParticleType> DING;
    public static final RegistryObject<SimpleParticleType> FIRE_BALL;
    public static final RegistryObject<SimpleParticleType> CAI;
    public static final RegistryObject<SimpleParticleType> EX_LASER;
    public static final RegistryObject<HitParticleType> AIR_PUNCH_BURST_PARTICLE;
    public static final RegistryObject<SimpleParticleType> OLA;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void RP(RegisterParticleProvidersEvent event) {

        
        event.registerSpriteSet(DANGER.get(), Dangers.DangerParticleProvider::new);
        event.registerSpriteSet(DANGER_RED.get(), Dangers_Red.Dangers_RedParticleProvider::new);
        event.registerSpriteSet(DANGER_BLACK.get(), Dangers_Black.Dangers_BlackParticleProvider::new);
        event.registerSpriteSet(DANGER_BLUE.get(), Dangers_Blue.Dangers_BlueParticleProvider::new);
        event.registerSpriteSet(DANGER_PURPLE.get(), Dangers_Purple.Dangers_PurpleProvider::new);
        event.registerSpriteSet(DING.get(), Ding.DangerParticleProvider::new);
        event.registerSpriteSet(CAI.get(), Cai.CaiParticleProvider::new);
        event.registerSpriteSet(FIRE_BALL.get(), Fire_Ball.Provider::new);
        event.registerSpriteSet(EX_LASER.get(), EX_Laser.Provider::new);
        event.registerSpriteSet(AIR_PUNCH_BURST_PARTICLE.get(), AirPunchBurstParticle.Provider::new);
        event.registerSpriteSet(OLA.get(), com.guhao.stars.client.particle.par.OLA.OLAParticleProvider::new);
    }
    public ParticleType() {
    }

    static {
        PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);
        DANGER = PARTICLES.register("dangers", () -> new SimpleParticleType(true));
        DANGER_RED = PARTICLES.register("dangers_red", () -> new SimpleParticleType(true));
        DANGER_BLACK = PARTICLES.register("dangers_black", () -> new SimpleParticleType(true));
        DANGER_BLUE = PARTICLES.register("dangers_blue", () -> new SimpleParticleType(true));
        DANGER_PURPLE = PARTICLES.register("dangers_purple", () -> new SimpleParticleType(true));
        FIRE_BALL = PARTICLES.register("fire_ball", () -> new SimpleParticleType(true));
        DING = PARTICLES.register("ding", () -> new SimpleParticleType(true));
        CAI = PARTICLES.register("cai", () -> new SimpleParticleType(true));
        EX_LASER = PARTICLES.register("ex_laser", () -> new SimpleParticleType(true));
        OLA = PARTICLES.register("ola", () -> new SimpleParticleType(true));
        AIR_PUNCH_BURST_PARTICLE = PARTICLES.register("air_punch_burst",
                        () -> new HitParticleType(
                                true,
                                HitParticleType.RANDOM_WITHIN_BOUNDING_BOX,
                                (target, attacker) -> {
                                    // 完全模仿ATTACKER_XY_ROTATION
                                    return new Vector3d(
                                            attacker.getXRot(),  // pitch
                                            180.0F - attacker.getYRot(),  // yaw
                                            -1.0  // 保留参数
                                    );
                                }
                        ));

    }
}
