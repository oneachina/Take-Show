package cn.oneachina.takeshow.event;

import cn.oneachina.takeshow.registry.TakeshowEnchantments;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class LookWhatEvents {

    private static final int DEBUFF_DURATION_TICKS = 100;

    private LookWhatEvents() {
    }

    public static void register() {
        ServerLivingEntityEvents.AFTER_DAMAGE.register(LookWhatEvents::afterDamage);
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                tickBlocking(player);
            }
        });
    }

    private static void afterDamage(LivingEntity victim, DamageSource source, float amount, float newAmount, boolean blocked) {
        if (!(victim instanceof ServerPlayer player)) {
            return;
        }

        ServerLevel level = (ServerLevel) player.level();
        Holder<Enchantment> enchantment = TakeshowEnchantments.lookup(player.registryAccess());
        int enchantLevel = getEnchantmentLevel(player, enchantment);
        if (enchantLevel <= 0) {
            return;
        }

        if (!(source.getEntity() instanceof LivingEntity attacker)) {
            return;
        }

        if (attacker == player) {
            return;
        }

        if (enchantLevel >= 1) {
            attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, DEBUFF_DURATION_TICKS, 0, false, true, true));
            attacker.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, DEBUFF_DURATION_TICKS, 0, false, true, true));
        }

        if (enchantLevel >= 2) {
            if (player.getRandom().nextFloat() < 0.3f) {
                attacker.hurtServer(level, level.damageSources().magic(), 2.0f);
            }
        }
    }

    private static void tickBlocking(ServerPlayer player) {
        if (!player.isBlocking()) {
            return;
        }

        ItemStack useItem = player.getUseItem();
        if (!useItem.is(Items.SHIELD)) {
            return;
        }

        Holder<Enchantment> enchantment = TakeshowEnchantments.lookup(player.registryAccess());
        int enchantLevel = getEnchantmentLevel(player, enchantment);
        if (enchantLevel < 3) {
            return;
        }

        ServerLevel level = (ServerLevel) player.level();
        AABB box = player.getBoundingBox().inflate(3.0);
        Vec3 look = player.getLookAngle().normalize();

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
            if (!(entity instanceof Mob mob)) {
                continue;
            }

            if (mob.getTarget() != player) {
                continue;
            }

            Vec3 delta = entity.position().subtract(player.position());
            if (delta.lengthSqr() > 9.0) {
                continue;
            }

            Vec3 dir = delta.normalize();
            if (dir.dot(look) < 0.5) {
                continue;
            }

            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, DEBUFF_DURATION_TICKS, 0, false, true, true));
        }
    }

    private static int getEnchantmentLevel(Player player, Holder<Enchantment> enchantment) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        int level = EnchantmentHelper.getItemEnchantmentLevel(enchantment, chest);

        ItemStack offhand = player.getOffhandItem();
        if (offhand.is(Items.SHIELD)) {
            level = Math.max(level, EnchantmentHelper.getItemEnchantmentLevel(enchantment, offhand));
        }

        ItemStack mainhand = player.getMainHandItem();
        if (mainhand.is(Items.SHIELD)) {
            level = Math.max(level, EnchantmentHelper.getItemEnchantmentLevel(enchantment, mainhand));
        }

        return level;
    }
}
