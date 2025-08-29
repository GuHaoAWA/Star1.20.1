package com.guhao.stars.mixins.epicfight;

import com.guhao.stars.entity.StarAttributes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.AttackSpeedModifyEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

@Mixin(value = PlayerPatch.class ,remap = false)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {
    @Shadow(
            remap = false
    )
    protected PlayerEventListener eventListeners;
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
    @Inject(
            method = {"getModifiedAttackSpeed(Lyesman/epicfight/world/capabilities/item/CapabilityItem;F)F"},
            at = {@At("HEAD")},
            remap = false,
            cancellable = true
    )
    private void onGetAttackSpeedPenalty(CapabilityItem itemCapability, float baseSpeed, CallbackInfoReturnable<Float> cir) {
        AttackSpeedModifyEvent event = new AttackSpeedModifyEvent((PlayerPatch<?>) (Object)this, itemCapability, baseSpeed);
        this.eventListeners.triggerEvents(PlayerEventListener.EventType.MODIFY_ATTACK_SPEED_EVENT, event);
        float weight = this.getWeight();
        Player player = this.getOriginal();
        double currentburden = player.getAttribute(StarAttributes.BURDEN.get()).getValue() + 40.0;
        if ((double)weight > currentburden) {
            float attenuation = (float)Mth.clamp(player.level.getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
            cir.setReturnValue((float)((double)event.getAttackSpeed() + -0.17499999701976776 * ((double)weight / currentburden) * (double)(Math.max(event.getAttackSpeed() - 0.8F, 0.0F) * 1.5F) * (double)attenuation));
        } else {
            cir.setReturnValue(event.getAttackSpeed());
        }

        cir.cancel();
    }

    @Inject(
            method = {"getModifiedStaminaConsume(F)F"},
            at = {@At("HEAD")},
            remap = false,
            cancellable = true
    )
    private void getStaminarConsumePenalty(float amount, CallbackInfoReturnable<Float> cir) {
        Player player = this.getOriginal();
        double currentburden = player.getAttribute(StarAttributes.BURDEN.get()).getValue() + 40.0;
        float weight = this.getWeight();
        float attenuation = (float)Mth.clamp(player.level.getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
        cir.setReturnValue((float)(Math.max((double)weight / currentburden - 1.0, 0.0) * (double)attenuation + 1.0) * amount);
        cir.cancel();
    }
}
