package com.guhao.stars.mixins.time.millis;

import com.guhao.stars.units.StarDataUnit;
import com.guhao.stars.units.data.TimeContext;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class GuiMixin {
    @ModifyVariable(method = "render", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float partial(float value) {
        if (StarDataUnit.isTimeStopped())
            value = TimeContext.Client.timer.partialTick;
        return value;
    }
}
