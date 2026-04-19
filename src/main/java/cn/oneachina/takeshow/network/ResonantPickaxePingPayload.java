package cn.oneachina.takeshow.network;

import cn.oneachina.takeshow.Takeshow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ResonantPickaxePingPayload(int durationTicks, List<BlockPos> positions) implements CustomPacketPayload {

    public static final Type<ResonantPickaxePingPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Takeshow.MOD_ID, "resonant_pickaxe_ping"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResonantPickaxePingPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeVarInt(payload.durationTicks);
                buf.writeVarInt(payload.positions.size());
                for (BlockPos pos : payload.positions) {
                    buf.writeBlockPos(pos);
                }
            },
            buf -> {
                int duration = buf.readVarInt();
                int count = buf.readVarInt();
                List<BlockPos> positions = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    positions.add(buf.readBlockPos());
                }
                return new ResonantPickaxePingPayload(duration, positions);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

