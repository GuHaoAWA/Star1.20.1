/*
package com.guhao.stars.event.key_appear;

import com.mafuyu404.smartkeyprompts.util.PromptUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import static com.guhao.stars.StarsMod.MODID;

@Mod.EventBusSubscriber(modid= MODID, value= Dist.CLIENT)
public class AvtiveKey {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = Minecraft.getInstance().player;
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
        if (Minecraft.getInstance().screen != null) return;
        if (pp != null && pp.isEpicFightMode()) return;
        PromptUtils.show("epicfight", "key.epicfight.switch_mode");
    }
}
*/
