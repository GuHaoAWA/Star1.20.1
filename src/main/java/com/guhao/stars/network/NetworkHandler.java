/*
package com.guhao.stars.network;

import com.guhao.stars.StarsMod;
import com.guhao.stars.network.timestop.TimeStopSyncPacket;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = StarsMod.PACKET_HANDLER;
    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(packetId++,
                TimeStopSyncPacket.class,
                TimeStopSyncPacket::encode,
                TimeStopSyncPacket::decode,
                TimeStopSyncPacket::handle);
    }
}*/
