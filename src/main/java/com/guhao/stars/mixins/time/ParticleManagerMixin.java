/*
package com.guhao.stars.mixins.time;

import com.dfdyz.epicacg.client.particle.BloomTrailParticle;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Lists;
import com.guhao.client.particle.par.BloomTrailParticleGuhao;
import com.guhao.client.particle.par.SpaceTrailParticle;
import com.guhao.stars.client.particle.par.AirPunchBurstParticle;
import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TrackingEmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.particle.TrailParticle;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class ParticleManagerMixin {

    @Shadow public Map<ParticleRenderType, Queue<Particle>> particles;
    @Shadow
    private void tickParticleList(Collection<Particle> p_107385_) {}
    @Shadow
    public ClientLevel level;
    @Shadow
    public Queue<TrackingEmitter> trackingEmitters;
    @Shadow
    public Queue<Particle> particlesToAdd;
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped()) {
            this.particles.forEach((p_288249_, p_288250_) -> {
                this.level.getProfiler().push(p_288249_.toString());
                this.tickParticleList(p_288250_);
                this.level.getProfiler().pop();
            });
            if (!this.trackingEmitters.isEmpty()) {
                List<TrackingEmitter> list = Lists.newArrayList();

                for (TrackingEmitter trackingemitter : this.trackingEmitters) {
                    trackingemitter.tick();
                    if (!trackingemitter.isAlive()) {
                        list.add(trackingemitter);
                    }
                }

                this.trackingEmitters.removeAll(list);
            }
            Particle particle;
            if (!this.particlesToAdd.isEmpty()) {
                while((particle = this.particlesToAdd.poll()) != null && stars$isAllowedParticle(particle)) {
                    this.particles.computeIfAbsent(particle.getRenderType(), (p_107347_) -> EvictingQueue.create(16384)).add(particle);
                }
            }
            ci.cancel();
        }
    }


    @Unique
    private boolean stars$isAllowedParticle(Particle particle) {
        return (particle instanceof TrailParticle || particle instanceof BloomTrailParticle || particle instanceof BloomTrailParticleGuhao || particle instanceof SpaceTrailParticle || particle instanceof AirPunchBurstParticle);
    }

}
*/
