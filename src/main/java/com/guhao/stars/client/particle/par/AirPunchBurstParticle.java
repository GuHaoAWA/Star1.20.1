/*
package com.guhao.stars.client.particle.par;

import com.guhao.stars.api.ParticleRenderTypeN;
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
import yesman.epicfight.api.utils.math.QuaternionUtils;
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
        this.scale = r.nextFloat(0.1F,0.225F);
        this.scaleO = r.nextFloat(0.1F,0.225F);
        this.lifetime = zd < 0.0 ? 2 : (int)zd;

        // 移除初始-90度旋转，直接使用视线方向
        Vec3 lookVec = new Vec3(xd, yd, zd).normalize();
        this.yaw = (float)Math.toDegrees(Math.atan2(lookVec.x, lookVec.z));
        this.yawO = this.yaw;

        // 计算俯仰角（上下方向）
        float horizontalDistance = Mth.sqrt((float)(lookVec.x * lookVec.x + lookVec.z * lookVec.z));
        this.pitch = -(float)Math.toDegrees(Math.atan2(lookVec.y, horizontalDistance));
        this.pitch -= 90f;
        this.pitchO = this.pitch;

        // 添加随机子粒子
        level.addParticle(ParticleType.OLA.get(), true,
                x + r.nextFloat(-2F, 2F),
                y + 1.85f + r.nextFloat(-0.85F, 0.85F),
                z + r.nextFloat(-2F, 2F),
                r.nextFloat() * 0.2f, r.nextFloat() * 0.2f, r.nextFloat() * 0.2f);

        level.addParticle(EpicFightParticles.HIT_BLUNT.get(),
                x, y-0.1, z, 0.0, 0.0, 0);
    }


    @Override
    public void tick() {
        super.tick();

    }
    @Override
    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {
        // 保持原有结构，仅调整旋转顺序
        Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);

        // 使用插值平滑过渡
        float roll = Mth.lerp(partialTicks, this.oRoll, this.roll);
        float pitch = Mth.lerp(partialTicks, this.pitchO, this.pitch);
        float yaw = Mth.lerp(partialTicks, this.yawO, this.yaw);

        // 修改旋转顺序为Y->X->Z
        rotation.mul(QuaternionUtils.YP.rotationDegrees(yaw));
        rotation.mul(QuaternionUtils.XP.rotationDegrees(pitch));
        rotation.mul(QuaternionUtils.ZP.rotationDegrees(roll));

        Vec3 vec3 = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
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
        PoseStack poseStack = new PoseStack();
        this.setupPoseStack(poseStack, camera, partialTicks);
        this.scale += 0.032F;
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
    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }
}*/
