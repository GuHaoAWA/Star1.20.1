package com.guhao.stars.mixins.minimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.hud.Hud;
import xaero.hud.render.HudRenderer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

@Mixin(value = HudRenderer.class,remap = false)
public class HudRendererMixin {
    @Inject(method = "render",at = @At("HEAD"), cancellable = true)
    public void render(Hud hud, GuiGraphics guiGraphics, float partialTicks, CallbackInfo ci) {
        LocalPlayerPatch pp = EpicFightCapabilities.getEntityPatch(Minecraft.instance.player, LocalPlayerPatch.class);
        if (pp != null && pp.isEpicFightMode()) {
            ci.cancel();
        }
    }
}
