package com.guhao.stars.mixins;

import com.guhao.stars.effects.Orange_Glow;
import com.guhao.stars.effects.Red_Glow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Shadow private Level level;

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
}