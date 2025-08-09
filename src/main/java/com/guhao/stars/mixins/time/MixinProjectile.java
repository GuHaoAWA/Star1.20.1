package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class MixinProjectile extends Entity {

    @Shadow
    public boolean hasBeenShot;
    @Shadow
    public abstract Entity getOwner();
    public MixinProjectile(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        Entity self = this;
        if (StarDataUnit.isTimeStopped()) {
            if (!this.hasBeenShot) {
                if (this.getType() == EntityType.ARROW || self instanceof Projectile) {
                    return;
                }
                this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
                this.hasBeenShot = true;
            }
            ci.cancel();
        }
    }
    @Inject(method = "updateRotation", at = @At("HEAD"), cancellable = true)
    protected void updateRotation(CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped() && hasBeenShot) {
            ci.cancel();
        }
    }
    @Inject(method = "lerpMotion", at = @At("HEAD"), cancellable = true)
    public void lerpMotion(double p_37279_, double p_37280_, double p_37281_, CallbackInfo ci) {
        Entity self = this;
        if (StarDataUnit.isTimeStopped() && hasBeenShot) {
            if (this.getType() == EntityType.ARROW || self instanceof Projectile) {
                return;
            }
            ci.cancel();
        }
    }
}