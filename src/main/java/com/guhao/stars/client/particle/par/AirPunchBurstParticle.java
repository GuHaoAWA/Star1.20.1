package com.guhao.stars.client.particle.par;

import com.guhao.stars.regirster.ParticleType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import yesman.epicfight.api.client.model.MeshProvider;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.RawMesh;
import yesman.epicfight.client.particle.AirBurstParticle;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;
import yesman.epicfight.client.particle.TexturedCustomModelParticle;
import yesman.epicfight.particle.EpicFightParticles;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class AirPunchBurstParticle extends TexturedCustomModelParticle {



    public AirPunchBurstParticle(ClientLevel level, double x, double y, double z,
                                 double xd, double yd, double zd,
                                 MeshProvider<RawMesh> particleMesh, ResourceLocation texture) {
        super(level, x, y, z, xd, yd, zd, particleMesh, texture);
        Random r = new Random();
        this.scale = r.nextFloat(0.1F, 0.225F);
        this.scaleO = this.scale;
        this.lifetime = zd < 0.0 ? 2 : (int)zd;

        // 模仿ATTACKER_XY_ROTATION的计算方式
        // xd = 攻击者的pitch (getXRot)
        // yd = 180 - 攻击者的yaw (getYRot)
        // zd = -1 (保留参数)
        this.pitch = (float)xd;  // 直接使用传入的pitch
        this.yaw = (float)yd;    // 已经处理为180-yaw
        this.roll = (float)zd;   // 保留参数

        // 应用90度X轴修正
        this.pitch -= 90f;

        this.pitchO = this.pitch;
        this.yawO = this.yaw;
        this.oRoll = this.roll;

        // 添加随机子粒子
        level.addParticle(ParticleType.OLA.get(), true,
                x + r.nextFloat(-2F, 2F),
                y + 1.85f + r.nextFloat(-0.85F, 0.85F),
                z + r.nextFloat(-2F, 2F),
                r.nextFloat(), r.nextFloat(), r.nextFloat());

        level.addParticle(EpicFightParticles.HIT_BLUNT.get(),
                x, y-0.1, z, 0.0, 0.0, 0);
    }


    @Override
    public void tick() {
        super.tick();

    }

    @Override
    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {
        // 插值旋转角度
        float pitch = Mth.lerp(partialTicks, this.pitchO, this.pitch);
        float yaw = Mth.lerp(partialTicks, this.yawO, this.yaw);
        float roll = Mth.lerp(partialTicks, this.oRoll, this.roll);

        // 构建旋转四元数 (Y-X顺序)
        Quaternionf rotation = new Quaternionf()
                .rotateY((float)Math.toRadians(yaw))  // 先Y轴旋转
                .rotateX((float)Math.toRadians(pitch)); // 再X轴旋转(已包含-90度修正)

        Vec3 cameraPos = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - cameraPos.z());
        float scale = Mth.lerp(partialTicks, this.scaleO, this.scale);

        poseStack.translate(x, y, z);
        poseStack.mulPose(rotation);
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.PARTICLE_MODEL_NO_NORMAL;
    }



    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        super.render(vertexConsumer, camera, partialTicks);
        this.scale += 0.056F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(SpriteSet spriteSet) {
        }

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                Vec3 lookVec = player.getLookAngle();
                return new AirPunchBurstParticle(level, x, y, z,
                        lookVec.x, lookVec.y, lookVec.z,
                        Meshes.AIR_BURST, AirBurstParticle.AIR_BURST_PARTICLE);
            }
            return null;
        }
    }
    @Override
    public boolean shouldCull() {
        return false;
    }

}