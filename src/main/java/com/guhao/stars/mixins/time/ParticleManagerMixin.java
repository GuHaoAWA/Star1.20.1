package com.guhao.stars.mixins.time;

import com.dfdyz.epicacg.client.particle.BloomTrailParticle;
import com.guhao.client.particle.par.BloomTrailParticleGuhao;
import com.guhao.client.particle.par.SpaceTrailParticle;
import com.guhao.stars.client.particle.par.OLA;
import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.particle.TrailParticle;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class ParticleManagerMixin {

    @Shadow public Map<ParticleRenderType, Queue<Particle>> particles;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped()) {
            boolean shouldCancel = particles.values().stream()
                    .flatMap(Queue::stream)
                    .noneMatch(this::stars$isAllowedParticle);
            if (shouldCancel) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(method = "render*", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float render(float value) {
        if (StarDataUnit.isTimeStopped()) {
            boolean shouldCancel = particles.values().stream()
                    .flatMap(Queue::stream)
                    .noneMatch(this::stars$isAllowedParticle);
            if (shouldCancel) {
                return Minecraft.getInstance().timer.partialTick;
            }
        }
        return value;
    }
    @Unique
    private boolean stars$isAllowedParticle(Particle particle) {
        return (particle instanceof TrailParticle || particle instanceof BloomTrailParticle || particle instanceof BloomTrailParticleGuhao || particle instanceof SpaceTrailParticle || particle instanceof OLA);
    }

}
