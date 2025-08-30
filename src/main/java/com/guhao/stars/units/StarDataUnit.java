package com.guhao.stars.units;

import com.guhao.stars.StarsMod;
import com.guhao.stars.client.particle.par.SparkParticle;
import com.guhao.stars.network.ParticlePacket;
import com.guhao.stars.regirster.StarsParticleType;
import net.corruptdog.cdm.gameasset.CorruptAnimations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.Arrays;

public record StarDataUnit() {
    private static boolean timeStopped = false;

    // 获取当前时停状态
    public static boolean isTimeStopped() {
        return timeStopped;
    }

   /* public static void setTimeStopped(boolean stopped) {
        // 只在服务端执行同步
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            timeStopped = stopped;
            syncTimeStopToAll(stopped);
        }
        // 客户端直接设置状态
        else {
            timeStopped = stopped;
        }
    }
    // 同步给所有玩家
    private static void syncTimeStopToAll(boolean stopped) {
        PACKET_HANDLER.send(PacketDistributor.ALL.noArg(),
                new TimeStopSyncPacket(stopped));
    }

    // 同步给特定玩家（用于玩家加入时）
    public static void syncTimeStopToPlayer(boolean stopped, ServerPlayer player) {
        PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player),
                new TimeStopSyncPacket(stopped));
    }*/

    // 处理网络包的方法
    public static void handleTimeStopSync(boolean stopped) {
        timeStopped = stopped;
    }


    static final StaticAnimation[] GUARD;
    static final StaticAnimation[] PARRY;
    static final StaticAnimation[] DODGE;
    static final StaticAnimation[] CAIDAO;
    static final StaticAnimation[] LOCK_OFF;
    static {//无视格挡red
        GUARD = new StaticAnimation[]{
                Animations.TSUNAMI_REINFORCED.get(),
                Animations.WRATHFUL_LIGHTING.get(),
                Animations.REVELATION_TWOHAND.get(),
               /* WOMAnimations.GESETZ_SPRENGKOPF,
                WOMAnimations.SOLAR_BRASERO_CREMATORIO,
                WOMAnimations.SOLAR_BRASERO_INFIERNO,
                WOMAnimations.STRONG_KICK,*/
////////////////////////////////////////////////////////////////zi
//                WOMAnimations.RUINE_CHATIMENT,
//                WOMAnimations.SOLAR_QUEMADURA,
                /*WOMAnimations.SOLAR_AUTO_2_POLVORA,*/
//                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_LAYED,
//                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
//                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
//                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,
               CorruptAnimations.LETHAL_SLICING_ONCE1.get(),
               CorruptAnimations.KATANA_SHEATHING_DASH_DAWN.get(),
               CorruptAnimations.FATAL_DRAW_DAWN.get(),
               CorruptAnimations.BLADE_RUSH1_DAWN.get(),
               CorruptAnimations.BLADE_RUSH3_DAWN.get(),
               CorruptAnimations.YAMATO_DAWN_DAWN.get(),
////////////////////////////////////////////////////////////////
/*                StarAnimations.SCRATCH,
                StarAnimations.EVIL_BLADE,*/
/*                GuHaoAnimations.NB_ATTACK,
                GuHaoAnimations.GUHAO_BATTOJUTSU_DASH,
                GuHaoAnimations.GUHAO_BIU,
                GuHaoAnimations.BLOOD_JUDGEMENT,
                GuHaoAnimations.DENG_LONG,*/
        };//只能完美org
        PARRY = new StaticAnimation[]{
                Animations.SPEAR_DASH.get(),
                Animations.LONGSWORD_DASH.get(),
                Animations.REVELATION_ONEHAND.get(),
/*                WOMAnimations.HERRSCHER_AUTO_2,
                WOMAnimations.STAFF_KINKONG,
                WOMAnimations.SOLAR_HORNO,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                WOMAnimations.GESETZ_AUTO_3,
                WOMAnimations.RUINE_REDEMPTION,
                WOMAnimations.RUINE_COMET,
                WOMAnimations.STRONG_PUNCH,
                WOMAnimations.AGONY_AUTO_1,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4,*/
                CorruptAnimations.SSPEAR_DASH.get(),
                CorruptAnimations.LONGSWORD_OLD_DASH.get(),
                CorruptAnimations.UCHIGATANA_DASH.get(),
                CorruptAnimations.UCHIGATANA_HEAVY1.get(),
                CorruptAnimations.DUAL_TACHI_DASH.get(),
                CorruptAnimations.BLADE_RUSH4.get(),
                CorruptAnimations.BLADE_RUSH_FINISHER.get(),
                CorruptAnimations.YAMATO_POWER3_FINISH.get(),
        };//purple 无视格挡+闪避的下段紫危：
        DODGE = new StaticAnimation[]{
/*                StarAnimations.KILL,
                WOMAnimations.TORMENT_AUTO_1,
                WOMAnimations.RUINE_CHATIMENT,
                WOMAnimations.SOLAR_QUEMADURA,
                WOMAnimations.SOLAR_AUTO_2_POLVORA,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_LAYED,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_LEFT,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_LAYED_RIGHT,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_DASH,*/
                CorruptAnimations.LETHAL_SLICING_ONCE1.get(),
                CorruptAnimations.KATANA_SHEATHING_DASH_DAWN.get(),
                CorruptAnimations.FATAL_DRAW_DAWN.get(),
                CorruptAnimations.BLADE_RUSH1_DAWN.get(),
                CorruptAnimations.BLADE_RUSH3_DAWN.get(),
                CorruptAnimations.YAMATO_DAWN_DAWN.get(),
        };
        CAIDAO = new StaticAnimation[]{
/*                Animations.SPEAR_DASH.get(),
                Animations.LONGSWORD_DASH.get(),
                WOMAnimations.HERRSCHER_AUTO_2,
                WOMAnimations.STAFF_KINKONG,
                WOMAnimations.ENDERBLASTER_ONEHAND_SHOOT_3,
                WOMAnimations.RUINE_COMET,
                WOMAnimations.AGONY_AUTO_1,
                WOMAnimations.ENDERBLASTER_TWOHAND_SHOOT_4,*/
                CorruptAnimations.SSPEAR_DASH.get(),
                CorruptAnimations.LONGSWORD_OLD_DASH.get(),
                CorruptAnimations.UCHIGATANA_DASH.get(),
                CorruptAnimations.UCHIGATANA_HEAVY1.get(),
                CorruptAnimations.DUAL_TACHI_DASH.get(),
                CorruptAnimations.BLADE_RUSH4.get(),
                CorruptAnimations.BLADE_RUSH_FINISHER.get(),
        };
        LOCK_OFF = new StaticAnimation[]{
/*                StarAnimations.FIST_AUTO_1,
                StarAnimations.FIST_AUTO_2,
                StarAnimations.FIST_AUTO_3,
                StarAnimations.FIST_AUTO_4,
                StarAnimations.THE_WORLD,
                WOMAnimations.KATANA_AUTO_1,
                WOMAnimations.KATANA_AUTO_2,
                WOMAnimations.KATANA_AUTO_3,*/
        };
    }

    public static EpicFightDamageSource getEpicFightDamageSources(DamageSource damageSource) {
        if (damageSource instanceof EpicFightDamageSource epicfightDamageSource) {
            return epicfightDamageSource;
        } else {
            return null;
        }
    }
    public static StaticAnimation[] getGuard() {
        return GUARD;
    }
    public static StaticAnimation[] getParry() {
        return PARRY;
    }
    public static StaticAnimation[] getcaidao() {
        return CAIDAO;
    }
    public static boolean isNoGuard(StaticAnimation staticAnimation) {
        return Arrays.asList(GUARD).contains(staticAnimation);
    }
    public static boolean isNoParry(StaticAnimation staticAnimation) {
        return Arrays.asList(PARRY).contains(staticAnimation);
    }
    public static boolean isNoDodge(StaticAnimation staticAnimation) {
        return Arrays.asList(DODGE).contains(staticAnimation);
    }
    public static boolean canCaiDAO(StaticAnimation staticAnimation) {
        return Arrays.asList(CAIDAO).contains(staticAnimation);
    }
    public static boolean isLockOff(StaticAnimation staticAnimation) {
        return Arrays.asList(LOCK_OFF).contains(staticAnimation);
    }



    public static void spawnBurst(Level level, Vec3 center, float radius, int count,
                                  SparkParticle.PhysicsType type, LivingEntity source) {
        // 确保只在服务端执行
        if (level.isClientSide) return;

        for (int i = 0; i < count; i++) {
            Vec3 direction = new Vec3(
                    (level.random.nextDouble() - 0.5) * 2.0,
                    (level.random.nextDouble() - 0.5) * 2.0,
                    (level.random.nextDouble() - 0.5) * 2.0
            ).normalize();

            Vec3 pos = center.add(direction.scale(radius * 0.2));
            Vec3 velocity = direction.scale(
                    0.05 + level.random.nextDouble() * 0.25 *
                            (type == SparkParticle.PhysicsType.EXPANSIVE ? 3.0 : 1.0)
            );

            // 发送给所有追踪该实体的客户端
            StarsMod.PACKET_HANDLER.send(
                    PacketDistributor.TRACKING_ENTITY.with(() -> source),
                    new ParticlePacket(
                            getParticleType(type),
                            pos.x, pos.y, pos.z,
                            velocity.x, velocity.y, velocity.z
                    )
            );
        }
    }

    private static ParticleOptions getParticleType(SparkParticle.PhysicsType type) {
        return switch (type) {
            case NORMAL -> StarsParticleType.NORMAL_SPARK.get();
            case EXPANSIVE -> StarsParticleType.SPARK_EXPANSIVE.get();
            case CONTRACTIVE -> StarsParticleType.SPARK_CONTRACTIVE.get();
        };
    }
}
