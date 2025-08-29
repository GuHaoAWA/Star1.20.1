package com.guhao.stars.client.particle.par;


import com.guhao.stars.regirster.ParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;

@OnlyIn(Dist.CLIENT)
public class SparkParticle extends TextureSheetParticle {
    private final SparkParticle.PhysicsType physicsType;
    private final float baseSize;
    private float rotationSpeed;
    public SparkParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SparkParticle.PhysicsType physicsType) {
        super(level, x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;

        // 初始颜色设置为橙黄色(钢铁火花的典型颜色)
        this.rCol = 1.0F;
        this.gCol = 0.7F + random.nextFloat() * 0.3F;
        this.bCol = 0.2F + random.nextFloat() * 0.2F;

        // 根据类型设置大小
        this.baseSize = physicsType == SparkParticle.PhysicsType.NORMAL ?
                (this.random.nextFloat() * 0.02F + 0.01F)*0.4F :
                (this.random.nextFloat() * 0.03F + 0.02F)*0.4F;
        this.quadSize = baseSize;

        // 生命周期设置
        this.lifetime = (physicsType == SparkParticle.PhysicsType.NORMAL ? 20 : 5) + this.random.nextInt(10);
        this.hasPhysics = true; // 所有粒子都启用物理

        // 重力设置 - 更真实的钢铁火花重力
        this.gravity = physicsType == SparkParticle.PhysicsType.NORMAL ?
                9.8F : // 正常重力
                (physicsType == PhysicsType.EXPANSIVE ? 0.4F : 1.2F); // 扩张型较轻，收缩型较重

        // 初始旋转
        this.roll = this.random.nextFloat() * 360.0F;
        this.oRoll = this.roll;
        this.rotationSpeed = (random.nextFloat() - 0.5f) * 0.5f; // 随机旋转速度

        // 初始速度
        Vec3 deltaMovement = physicsType.function.getDeltaMovement(xd, yd, zd);
        this.xd = deltaMovement.x * (0.7 + random.nextDouble() * 0.6);
        this.yd = deltaMovement.y * (0.7 + random.nextDouble() * 0.6);
        this.zd = deltaMovement.z * (0.7 + random.nextDouble() * 0.6);

        this.physicsType = physicsType;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }


    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.LIGHTNING;
    }

    @Override
    public void tick() {
        super.tick();

        // 旋转效果
        this.oRoll = this.roll;
        this.roll += this.rotationSpeed;

        // 根据生命周期改变大小和颜色
        float lifeProgress = (float)this.age / (float)this.lifetime;

        // 大小变化
        if (physicsType == PhysicsType.EXPANSIVE) {
            this.quadSize = baseSize * (1.0f + lifeProgress * 0.8f);
        } else {
            this.quadSize = baseSize * (1.0f - lifeProgress * 0.5f);
        }

        // 颜色变化 - 从亮黄色到暗红色
        this.gCol = 0.7F * (1.0f - lifeProgress * 0.9f);
        this.bCol = 0.2F * (1.0f - lifeProgress * 0.7f);

        // 物理行为变化
        if (this.physicsType == SparkParticle.PhysicsType.EXPANSIVE) {
            // 扩张型粒子 - 空气阻力更大
            this.xd *= 0.88;
            this.yd *= 0.88;
            this.zd *= 0.88;
        } else if (this.physicsType == SparkParticle.PhysicsType.CONTRACTIVE) {
            // 收缩型粒子 - 空气阻力较小
            this.xd *= 0.95;
            this.yd *= 0.95;
            this.zd *= 0.95;
        } else {
            // 普通粒子 - 中等空气阻力
            this.xd *= 0.92;
            this.yd *= 0.92;
            this.zd *= 0.92;
        }

        // 添加一些随机运动变化，模拟空气湍流
        if (random.nextInt(8) == 0) {
            this.xd += (random.nextDouble() - 0.5) * 0.015;
            this.yd += (random.nextDouble() - 0.5) * 0.015;
            this.zd += (random.nextDouble() - 0.5) * 0.015;
        }
    }
    public static enum PhysicsType {
        EXPANSIVE(Vec3::new),
        CONTRACTIVE((dx, dy, dz) -> new Vec3(dx * 0.02, dy * 0.02, dz * 0.02)),
        NORMAL(Vec3::new);

        final SparkParticle.DeltaMovementFunction function;

        private PhysicsType(SparkParticle.DeltaMovementFunction function) {
            this.function = function;
        }
    }
    @FunctionalInterface
    interface DeltaMovementFunction {
        Vec3 getDeltaMovement(double var1, double var3, double var5);
    }

    @OnlyIn(Dist.CLIENT)
    public static class NormalDustProvider implements ParticleProvider<SimpleParticleType> {
        protected SpriteSet sprite;

        public NormalDustProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle SparkParticle = new SparkParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, PhysicsType.NORMAL);
            SparkParticle.pickSprite(this.sprite);
            return SparkParticle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ContractiveDustProvider implements ParticleProvider<SimpleParticleType> {
        protected SpriteSet sprite;

        public ContractiveDustProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle SparkParticle = new SparkParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, PhysicsType.CONTRACTIVE);
            SparkParticle.pickSprite(this.sprite);
            return SparkParticle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ExpansiveDustProvider implements ParticleProvider<SimpleParticleType> {
        protected SpriteSet sprite;

        public ExpansiveDustProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle SparkParticle = new SparkParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, PhysicsType.EXPANSIVE);
            SparkParticle.pickSprite(this.sprite);
            return SparkParticle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ContractiveMetaParticle extends NoRenderParticle {
        private final double radius;
        private final int density;

        public ContractiveMetaParticle(ClientLevel level, double x, double y, double z, double radius, int lifetime, int density) {
            super(level, x, y, z);
            this.radius = radius;
            this.lifetime = lifetime;
            this.density = density;
        }

        public void tick() {
            super.tick();

            for(int x = -1; x <= 1; x += 2) {
                for(int y = -1; y <= 1; y += 2) {
                    for(int z = -1; z <= 1; z += 2) {
                        for(int i = 0; i < this.density; ++i) {
                            Vec3 rand = (new Vec3(Math.random() * (double)x, Math.random() * (double)y, Math.random() * (double)z)).normalize().scale(this.radius);
                            this.level.addParticle(ParticleType.SPARK_CONTRACTIVE.get(), this.x + rand.x, this.y + rand.y, this.z + rand.z, -rand.x, -rand.y, -rand.z);
                        }
                    }
                }
            }

        }

        @OnlyIn(Dist.CLIENT)
        public static class Provider implements ParticleProvider<SimpleParticleType> {
            public Provider() {
            }

            public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                SparkParticle.ContractiveMetaParticle particle = new SparkParticle.ContractiveMetaParticle(worldIn, x, y, z, xSpeed, (int)Double.doubleToLongBits(ySpeed), (int)Double.doubleToLongBits(zSpeed));
                return particle;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ExpansiveMetaParticle extends NoRenderParticle {
        public ExpansiveMetaParticle(ClientLevel level, double x, double y, double z, double radius, int density) {
            super(level, x, y, z);

            for(int vx = -1; vx <= 1; vx += 2) {
                for(int vz = -1; vz <= 1; vz += 2) {
                    for(int i = 0; i < density; ++i) {
                        Vec3 rand = (new Vec3(Math.random() * (double)vx, Math.random(), Math.random() * (double)vz)).normalize().scale(radius);
                        level.addParticle(ParticleType.SPARK_EXPANSIVE.get(), x, y, z, rand.x, rand.y, rand.z);
                    }
                }
            }

        }

        @OnlyIn(Dist.CLIENT)
        public static class Provider implements ParticleProvider<SimpleParticleType> {
            public Provider() {
            }

            public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                SparkParticle.ExpansiveMetaParticle particle = new SparkParticle.ExpansiveMetaParticle(worldIn, x, y, z, xSpeed, (int)Double.doubleToLongBits(ySpeed));
                return particle;
            }
        }
    }

}
