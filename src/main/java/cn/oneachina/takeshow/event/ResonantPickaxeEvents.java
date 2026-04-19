package cn.oneachina.takeshow.event;

import cn.oneachina.takeshow.network.ResonantPickaxePingPayload;
import cn.oneachina.takeshow.registry.TakeshowItems;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class ResonantPickaxeEvents {

    private static final int RADIUS = 15;
    private static final int HIGHLIGHT_DURATION_TICKS = 60;
    private static final int COOLDOWN_TICKS = 10;
    private static final int MAX_RESULTS = 256;
    private static final Map<UUID, Long> LAST_PING_TICK = new HashMap<>();

    private ResonantPickaxeEvents() {
    }

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(ResonantPickaxeEvents::afterBlockBreak);
    }

    private static void afterBlockBreak(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        ItemStack stack = serverPlayer.getMainHandItem();
        if (!stack.is(TakeshowItems.RESONANT_PICKAXE)) {
            return;
        }

        if (!isStoneLike(state)) {
            return;
        }

        long gameTime = serverLevel.getGameTime();
        Long last = LAST_PING_TICK.get(serverPlayer.getUUID());
        if (last != null && gameTime - last < COOLDOWN_TICKS) {
            return;
        }
        LAST_PING_TICK.put(serverPlayer.getUUID(), gameTime);

        List<Candidate> candidates = new ArrayList<>();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dy = -RADIUS; dy <= RADIUS; dy++) {
                for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                    int distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq > RADIUS * RADIUS) {
                        continue;
                    }

                    cursor.setWithOffset(pos, dx, dy, dz);
                    BlockState bs = serverLevel.getBlockState(cursor);
                    if (!isOre(bs)) {
                        continue;
                    }
                    if (isExposedToAir(serverLevel, cursor)) {
                        continue;
                    }

                    candidates.add(new Candidate(cursor.immutable(), distSq));
                }
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        candidates.sort(Comparator.comparingInt(Candidate::distSq));
        int count = Math.min(MAX_RESULTS, candidates.size());
        List<BlockPos> positions = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            positions.add(candidates.get(i).pos());
        }

        ServerPlayNetworking.send(serverPlayer, new ResonantPickaxePingPayload(HIGHLIGHT_DURATION_TICKS, positions));
    }

    private static boolean isStoneLike(BlockState state) {
        return state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(BlockTags.BASE_STONE_NETHER)
                || state.is(BlockTags.STONE_ORE_REPLACEABLES)
                || state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    }

    private static boolean isOre(BlockState state) {
        return state.is(BlockTags.COAL_ORES)
                || state.is(BlockTags.COPPER_ORES)
                || state.is(BlockTags.IRON_ORES)
                || state.is(BlockTags.GOLD_ORES)
                || state.is(BlockTags.REDSTONE_ORES)
                || state.is(BlockTags.LAPIS_ORES)
                || state.is(BlockTags.DIAMOND_ORES)
                || state.is(BlockTags.EMERALD_ORES);
    }

    private static boolean isExposedToAir(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).isAir()) {
                return true;
            }
        }
        return false;
    }

    private record Candidate(BlockPos pos, int distSq) {
    }
}

