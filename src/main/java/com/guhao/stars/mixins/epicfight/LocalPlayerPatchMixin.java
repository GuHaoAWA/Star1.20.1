package com.guhao.stars.mixins.epicfight;

import com.teamderpy.shouldersurfing.client.ShoulderInstance;
import com.teamderpy.shouldersurfing.config.Perspective;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(value = {LocalPlayerPatch.class}, remap = false)
public class LocalPlayerPatchMixin extends AbstractClientPlayerPatch<LocalPlayer> {
    @Inject(at = {@At("TAIL")}, method = {"toBattleMode"})
    public void MixinToBattleMode(boolean synchronize, CallbackInfo callbackInfo) {
        if (this.playerMode != PlayerPatch.PlayerMode.MINING && EpicFightMod.CLIENT_CONFIGS.cameraAutoSwitch.getValue()) {
            ShoulderInstance.getInstance().changePerspective(Perspective.SHOULDER_SURFING);
        }

    }

    @Inject(at = {@At("TAIL")}, method = {"toMiningMode"})
    public void MixinToMiningMode(boolean synchronize, CallbackInfo callbackInfo) {
        if ( this.playerMode != PlayerPatch.PlayerMode.BATTLE && EpicFightMod.CLIENT_CONFIGS.cameraAutoSwitch.getValue()) {
            ShoulderInstance.getInstance().changePerspective(Perspective.FIRST_PERSON);
        }
    }
}
