package com.guhao.stars.units;

import com.dfdyz.epicacg.client.screeneffect.HsvFilterEffect;
import com.dfdyz.epicacg.event.ScreenEffectEngine;
import com.guhao.init.ParticleType;
import com.guhao.stars.StarsMod;
import com.guhao.stars.regirster.Sounds;
import com.guhao.stars.regirster.StarSkill;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector3f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class StarBattleUnits {

    public static void time_stop(LivingEntityPatch<?> ep) {
        if (!ep.isLogicalClient()) {ep.playSound(Sounds.THE_WORLD.get(),1.0F, 1.0F, 1.0F);}
        for (int i = 0; i < 100; i++) {
            // 随机位置（以玩家为中心，半径5格）
            double x = ep.getOriginal().getX() + (Math.random() - 0.5) * 10;
            double y = ep.getOriginal().getY() + Math.random() * 5;
            double z = ep.getOriginal().getZ() + (Math.random() - 0.5) * 10;

            // 生成静止粒子（速度=0）
            ep.getOriginal().level().addParticle(
                    ParticleTypes.END_ROD, // 粒子类型
                    x, y, z,              // 位置
                    0, 0, 0               // 速度（0表示静止）
            );
        }
        double radius = 10.0;
        for (double theta = 0; theta < Math.PI * 2; theta += 0.2) {
            for (double phi = 0; phi < Math.PI; phi += 0.2) {
                double x = ep.getOriginal().getX() + radius * Math.sin(theta) * Math.cos(phi);
                double y = ep.getOriginal().getY() + radius * Math.sin(phi);
                double z = ep.getOriginal().getZ() + radius * Math.cos(theta) * Math.cos(phi);

                ep.getOriginal().level().addParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        x, y, z,
                        0, 0.1, 0
                );

                ep.getOriginal().level().addParticle(
                        ParticleTypes.REVERSE_PORTAL,
                        x * 0.9, y * 0.9, z * 0.9,
                        0, 0, 0
                );
            }
        }
        double centerX = ep.getOriginal().getX();
        double centerZ = ep.getOriginal().getZ();
        for (double r = 0; r <= 3; r += 0.5) {
            for (double angle = 0; angle < 360; angle += 10) {
                double x = centerX + r * Math.cos(Math.toRadians(angle));
                double z = centerZ + r * Math.sin(Math.toRadians(angle));

                ep.getOriginal().level().addParticle(
                        new DustParticleOptions(new Vector3f(0.2f, 0.6f, 1.0f), 1.0f),
                        x, ep.getOriginal().getY(), z,
                        0, 0, 0
                );
            }
        }
        ShakeUnit.shake(40,10,10,ep.getOriginal().position,10);
        if (ep.getOriginal().level() instanceof ServerLevel _level) {
            _level.sendParticles(ParticleType.Y_CONQUEROR_HAKI.get(), ep.getOriginal().getX(), ep.getOriginal().getY() + 1.0, ep.getOriginal().getZ(), 1, 0.1, 0.1, 0.1, 0);
            _level.sendParticles(ParticleType.Y_CONQUEROR_HAKI_FLOOR.get(), ep.getOriginal().getX(), ep.getOriginal().getY() + 1.0, ep.getOriginal().getZ(), 1, 0.1, 0.1, 0.1, 0);
        }
        StarsMod.queueServerWork(8, () -> {
            StarDataUnit.setTimeStopped(true);
            if (!ep.isLogicalClient()) {ep.playSound(Sounds.TIME_S.get(),1.6F, 1.0F, 1.0F);}
            HsvFilterEffect effect = new HsvFilterEffect(ep.getOriginal().position(),180);
            ScreenEffectEngine.PushScreenEffect(effect);
        });
        StarsMod.queueServerWork(188, () -> {
            StarDataUnit.setTimeStopped(false);
            if (ep instanceof ServerPlayerPatch playerPatch && playerPatch.getSkill(StarSkill.THE_WORLD) != null) {
                playerPatch.getSkill(StarSkill.THE_WORLD).getSkill().setStackSynchronize(playerPatch,0);
            }
            if (!ep.isLogicalClient()) {
                ep.playSound(Sounds.TIME_R.get(), 1.6F, 1.0F, 1.0F);
            }
        });

    }
}
