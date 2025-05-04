package com.guhao.stars.client.particle.par;

import com.guhao.stars.api.ParticleRenderTypeN;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class Cai extends TextureSheetParticle {
    @OnlyIn(Dist.CLIENT)
    public static CaiParticleProvider provider(SpriteSet spriteSet) {
        return new CaiParticleProvider(spriteSet);
    }
    public static class CaiParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;
        public CaiParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new Cai(worldIn, x, y, z,this.spriteSet);
        }
    }
    protected Cai(ClientLevel world, double x, double y, double z, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.setSize(2.5f, 2.5f);
        this.quadSize *= 2.85f;
        this.lifetime = 50;
        this.gravity = 0f;
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
    }
    @Override
    public boolean shouldCull() {
        return false;
    }
    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderTypeN.PARTICLE_SHEET_LIT_NO_CULL;
    }
    @Override
    public void tick() {
        super.tick();
    }


}
