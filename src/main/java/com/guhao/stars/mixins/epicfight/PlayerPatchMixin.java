package com.guhao.stars.mixins.epicfight;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

@Mixin(value = PlayerPatch.class ,remap = false)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {
    @Shadow
    protected int tickSinceLastAction;
    @Shadow
    public float getStamina() {
        return 1.0f;
    }
    @Shadow
    public float getMaxStamina() {
        AttributeInstance maxStamina = this.original.getAttribute(EpicFightAttributes.MAX_STAMINA.get());
        return (float)(maxStamina == null ? 0.0 : maxStamina.getValue());
    }

    @Shadow
    public void setStamina(float value) {
    }
    @Shadow
    protected double xo;
    @Shadow
    protected double yo;
    @Shadow
    protected double zo;

    @Unique
    private float Star$getStaminaRecoverySpeed(float maxStamina, float staminaRegen) {
        float currentHealth = this.original.getHealth();

        float maxHealth = this.original.getMaxHealth();
        float healthRatio = currentHealth / maxHealth;
        float S;
        if (healthRatio > 0.75) {
            S = 1.0f;
        } else if (healthRatio > 0.5) {
            S = 0.8f;
        } else if (healthRatio > 0.25) {
            S = 0.6f;
        } else {
            S = 0.5f;
        }

        // 计算耐力恢复速度
        return (0.12f + maxStamina * 0.005f) * staminaRegen * S;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        if (this.state.canBasicAttack()) {
            ++this.tickSinceLastAction;
        }

        float stamina = this.getStamina();
        float maxStamina = this.getMaxStamina();
        float staminaRegen = (float) this.original.getAttributeValue(EpicFightAttributes.STAMINA_REGEN.get());
//        原计算公式
//        int regenStandbyTime = 900 / (int)(30.0F * staminaRegen);

        // 计算恢复耐力所需的等待间隔时间 t
        float t = Math.min(Math.max(6.0f, 12.0f / staminaRegen), 20.0f);

        float staminaRecoverySpeed = Star$getStaminaRecoverySpeed(maxStamina, staminaRegen);
        if (stamina < maxStamina && this.tickSinceLastAction > t) {
            this.setStamina(stamina + staminaRecoverySpeed);
        }

        if (maxStamina < stamina) {
            this.setStamina(maxStamina);
        }

        this.xo = this.original.getX();
        this.yo = this.original.getY();
        this.zo = this.original.getZ();
    }

}
