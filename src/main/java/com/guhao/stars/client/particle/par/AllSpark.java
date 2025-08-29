package com.guhao.stars.client.particle.par;

import com.guhao.stars.regirster.ParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AllSpark extends NoRenderParticle {
    public AllSpark(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
        for (int i = 0; i < 46; i++) {
            Vec3 direction = new Vec3(
                    (level.random.nextDouble() - 0.5) * 2.0,
                    (level.random.nextDouble() - 0.5) * 2.0,
                    (level.random.nextDouble() - 0.5) * 2.0
            ).normalize();
            Vec3 pos = new Vec3(x,y,z).add(direction.scale(0.6 * 0.2));
            Vec3 velocity = direction.scale(0.05 + level.random.nextDouble() * 0.25 * 3.2);
           this. level.addParticle(ParticleType.SPARK_EXPANSIVE.get(), pos.x,pos.y,pos.z,velocity.x,velocity.y,velocity.z);
        }
        this.level.addParticle(ParticleType.FLASH.get(), x,y,z,0d,0d,0d);
    }



    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new AllSpark(worldIn, x, y, z);
        }
    }
}
