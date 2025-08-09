package com.guhao.stars.client.particle.par;

import com.guhao.api.ParticleRenderTypeN;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class OLA extends TextureSheetParticle {
    private final SpriteSet sprites;

    public OLA(ClientLevel level, double x, double y, double z,
               double xSpeed, double ySpeed, double zSpeed,
               SpriteSet sprites, RandomSource random) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;

        this.setSpriteFromSet(random);
        Random rand = new Random();
        float angle = (float)Math.toRadians(90.0F + (rand.nextFloat() - 0.5F) * 90.0F + (rand.nextBoolean() ? 0.0F : 180.0F));
        this.oRoll = angle;
        this.roll = angle;
        this.lifetime = 20 + random.nextInt(5);
        this.quadSize = 0.45f + random.nextFloat() * 0.2f;
    }

    private void setSpriteFromSet(RandomSource random) {
        this.setSprite(this.sprites.get(random));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderTypeN.PARTICLE_SHEET_LIT_NO_CULL;
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
    public void render(@NotNull VertexConsumer var1, @NotNull Camera var2, float var3) {
        super.render(var1, var2, var3);
        this.quadSize += 0.002F;
    }

    @OnlyIn(Dist.CLIENT)
    public static OLA.OLAParticleProvider provider(SpriteSet spriteSet) {
        return new OLA.OLAParticleProvider(spriteSet);
    }

    public static class OLAParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public OLAParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new OLA(worldIn, x, y, z, xSpeed, ySpeed, zSpeed,
                    this.spriteSet, worldIn.random);
        }
    }
}