package com.guhao.stars.mixins.time;

import com.guhao.stars.units.StarDataUnit;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level{
    ClientLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void animateTick(int p_104785_, int p_104786_, int p_104787_, CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped()) ci.cancel();
    }

    @Inject(method = "tickTime", at = @At("HEAD"), cancellable = true)
    private void tickTime(CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped()) ci.cancel();
    }

    @Inject(method = "setGameTime", at = @At("HEAD"), cancellable = true)
    private void setGameTime(long p_104638_, CallbackInfo ci) {
        if (StarDataUnit.isTimeStopped()) {
            if (p_104638_ - levelData.getGameTime() > 0) {
                ci.cancel();
            }
        }
    }

}
