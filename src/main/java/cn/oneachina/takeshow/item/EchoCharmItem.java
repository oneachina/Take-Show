package cn.oneachina.takeshow.item;

import cn.oneachina.takeshow.registry.TakeshowStatusEffects;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.particles.ParticleTypes;
import org.jspecify.annotations.NonNull;

public class EchoCharmItem extends Item {

    private static final int ECHO_DISTORTION_DURATION_TICKS = 120;
    private static final int ECHO_CHARM_COOLDOWN_TICKS = 100;
    private static final int ECHO_PING_GLOW_TICKS = 60;
    private static final double ECHO_PING_RADIUS = 16.0;

    public EchoCharmItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel) {
            player.getCooldowns().addCooldown(stack, ECHO_CHARM_COOLDOWN_TICKS);
            player.addEffect(new MobEffectInstance(TakeshowStatusEffects.ECHO_DISTORTION, ECHO_DISTORTION_DURATION_TICKS, 0, false, true, true));

            AABB box = player.getBoundingBox().inflate(ECHO_PING_RADIUS);
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive());
            for (LivingEntity entity : entities) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, ECHO_PING_GLOW_TICKS, 0, false, false, true));
            }

            serverLevel.sendParticles(ParticleTypes.SCULK_CHARGE_POP, player.getX(), player.getY() + 1.0, player.getZ(), 18, 0.35, 0.45, 0.35, 0.02);
            serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, player.getX(), player.getY() + 1.0, player.getZ(), 8, 0.25, 0.35, 0.25, 0.01);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.8f, 1.0f);
        }

        return InteractionResult.SUCCESS;
    }
}
