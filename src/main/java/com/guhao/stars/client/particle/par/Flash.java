package com.guhao.stars.client.particle.par;

import com.guhao.api.ParticleRenderTypeN;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class Flash extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    public Flash(ClientLevel level, double x, double y, double z,
                 double xSpeed, double ySpeed, double zSpeed,
                 SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = sprites;
        this.quadSize = 1.2F;
        this.lifetime = 5;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.setSpriteFromAge(sprites);
    }

    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSprite(this.spriteSet.get(this.age / 1 % 6 + 1, 6));
        }

    }
    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderTypeN.PARTICLE_SHEET_LIT_NO_CULL2;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }


    @OnlyIn(Dist.CLIENT)
    public static Flash.FlashParticleProvider provider(SpriteSet spriteSet) {
        return new Flash.FlashParticleProvider(spriteSet);
    }

    public static class FlashParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public FlashParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new Flash(worldIn, x, y, z, xSpeed, ySpeed, zSpeed,
                    this.spriteSet);
        }
    }
}