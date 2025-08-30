package com.guhao.stars.client.particle.par;


import com.guhao.stars.api.ParticleRenderTypeN;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class Dangers extends TextureSheetParticle {
    @OnlyIn(Dist.CLIENT)
    public static DangerParticleProvider provider(SpriteSet spriteSet) {
        return new DangerParticleProvider(spriteSet);
    }
    public static class DangerParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;
        public DangerParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new Dangers(worldIn, x, y, z,this.spriteSet);
        }
    }
    protected Dangers(ClientLevel world, double x, double y, double z,SpriteSet spriteSet) {
        super(world, x, y, z);
        this.setSize(2.5f, 2.5f);
        this.quadSize *= 2.85f;
        this.lifetime = 25;
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
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypeN.PARTICLE_SHEET_LIT_NO_CULL;
    }
    @Override
    public void render(@NotNull VertexConsumer vertexBuffer, Camera camera, float pt) {
        super.render(vertexBuffer, camera, pt);
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            x = player.getX();
            y = player.getEyeY() + 0.9;
            z = player.getZ();
            this.setPos(x, y, z);
        }
    }
    @Override
    public void tick() {
        super.tick();
    }

}
