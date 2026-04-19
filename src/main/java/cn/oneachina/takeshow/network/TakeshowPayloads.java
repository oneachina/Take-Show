package cn.oneachina.takeshow.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class TakeshowPayloads {
    private TakeshowPayloads() {
    }

    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(ResonantPickaxePingPayload.TYPE, ResonantPickaxePingPayload.STREAM_CODEC);
    }
}

