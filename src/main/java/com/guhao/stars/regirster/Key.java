package com.guhao.stars.regirster;

import com.guhao.GuhaoMod;
import com.guhao.network.BloodBurstMessage;
import com.guhao.network.EnderMessage;
import com.guhao.network.RedFistMessage;
import com.guhao.stars.network.InspectMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.guhao.GuhaoMod.PACKET_HANDLER;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class Key {
    public static final KeyMapping INSPECT = new KeyMapping("key.star.inspect", GLFW.GLFW_KEY_V, "key.categories.star") {

        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                GuhaoMod.PACKET_HANDLER.sendToServer(new RedFistMessage(0, 0));
                if (Minecraft.getInstance().player != null) {
                    InspectMessage.pressAction(Minecraft.getInstance().player, 0, 0);
                }
            }
            isDownOld = isDown;
        }
    };



    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(INSPECT);

    }



    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen == null) {
                INSPECT.consumeClick();

            }
        }
    }
}
