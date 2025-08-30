package com.guhao.stars.mixins;

import com.guhao.stars.effects.Orange_Glow;
import com.guhao.stars.effects.Red_Glow;
import com.guhao.stars.units.StarDataUnit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Level level();

    @Shadow
    public Level level;

    @Inject(at = @At("HEAD"), method = "getTeamColor", cancellable = true)
    public void star$getTeamColor$renderShiny(CallbackInfoReturnable<Integer> cir) {
        Entity self = (Entity) (Object) this;
        if (this.level.isClientSide() && Orange_Glow.shouldRender(self)) {
            cir.setReturnValue(Orange_Glow.getColor(self));
        }
        if (this.level.isClientSide() && Red_Glow.shouldRender(self)) {
            cir.setReturnValue(Red_Glow.getColor(self));
        }
    }

    @Inject(at = @At("HEAD"), method = "isCurrentlyGlowing", cancellable = true)
    public void star$isCurrentlyGlowing$renderShinny(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        if (this.level.isClientSide()) {
            if (Orange_Glow.shouldRender(self)) {
                cir.setReturnValue(true);
            }
        }
        if (this.level.isClientSide()) {
            if (Red_Glow.shouldRender(self)) {
                cir.setReturnValue(true);
            }
        }
    }
    @Inject(method = "baseTick", at = @At("HEAD"), cancellable = true)
    public void baseTick(CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && !(self instanceof Player)) {
            ci.cancel();
        }
    }
    /*@Inject(method = "setRemoved", at = @At("HEAD"), cancellable = true)
    private void setRemoved(Entity.RemovalReason p_146876_, CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped()) {
            if (((Object) this) instanceof LivingEntity living) {
                if (!level().isClientSide) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    if (serverLevel.players().isEmpty())
                        StarDataUnit.setTimeStopped(false);;
                }
            }
        }
    }*/
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && !(self instanceof Player)) {
            ci.cancel();
        }
    }
    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType p_19973_, Vec3 p_19974_, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && !(self instanceof Player)) {
            ci.cancel();
        }
    }
    @Inject(method = "moveTo*", at = @At("HEAD"), cancellable = true)
    public void moveTo(CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && !(self instanceof Player)) {
            ci.cancel();
        }
    }

    @Inject(method = "getX*", at = @At("HEAD"), cancellable = true)
    public void getX(CallbackInfoReturnable<Double> cir) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && self instanceof Projectile projectile && projectile.hasBeenShot) {
            cir.setReturnValue(projectile.xo);
        }
    }
    @Inject(method = "getY*", at = @At("HEAD"), cancellable = true)
    public void getY(CallbackInfoReturnable<Double> cir) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && self instanceof Projectile projectile && projectile.hasBeenShot) {
            cir.setReturnValue(projectile.yo);
        }
    }
    @Inject(method = "getZ*", at = @At("HEAD"), cancellable = true)
    public void getZ(CallbackInfoReturnable<Double> cir) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && self instanceof Projectile projectile && projectile.hasBeenShot) {
            cir.setReturnValue(projectile.zo);
        }
    }
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void turn(double p_19885_, double p_19886_, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && !(self instanceof Player)) {
            ci.cancel();
        }
    }
    @Inject(method = "addDeltaMovement", at = @At("HEAD"), cancellable = true)
    public void addDeltaMovement(Vec3 p_250128_, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (StarDataUnit.isTimeStopped() && !(self instanceof Player)) {
            ci.cancel();
        }
    }

}