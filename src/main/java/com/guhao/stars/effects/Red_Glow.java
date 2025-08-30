package com.guhao.stars.effects;

import com.guhao.stars.regirster.StarsEffect;
import com.guhao.stars.units.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;

public class Red_Glow extends MobEffect {
    private static int getColor(float hue, float saturation, float brightness) {
        return 0xFFFF0000;
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(Entity entity) {
        Level level = entity.level();
        float pTick = Minecraft.getInstance().getPartialTick();
        Player player = Proxy.getClientPlayer();
        Vec3 pos = entity.getPosition(pTick).subtract(player.getPosition(pTick));
        double dis = Math.atan2(pos.x, pos.z) / Math.PI * 180 / 6;
        float tick = level.getGameTime() + Minecraft.getInstance().getPartialTick() + (float) dis;
        return getColor(tick % 1, 1, 1);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean shouldRender(Entity self) {
        // 只对特定实体类型生效
        if (!(self instanceof ItemEntity) &&
                !(self instanceof Projectile) &&
                !(self instanceof LivingEntity) &&
                !(self instanceof PartEntity)) {
            return false;
        }

        // 检查实体自身是否拥有效果
        if (self instanceof LivingEntity livingEntity) {
            return livingEntity.hasEffect(StarsEffect.RED_GLOW.get());
        }

        return false;

    }

    public Red_Glow() {
        super(MobEffectCategory.NEUTRAL, 9740385);
    }
}

