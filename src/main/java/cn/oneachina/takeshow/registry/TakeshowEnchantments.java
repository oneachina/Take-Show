package cn.oneachina.takeshow.registry;

import cn.oneachina.takeshow.Takeshow;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public final class TakeshowEnchantments {

    public static final Identifier LOOK_WHAT_ID = Identifier.fromNamespaceAndPath(Takeshow.MOD_ID, "look_what");
    public static final ResourceKey<Enchantment> LOOK_WHAT_KEY = ResourceKey.create(Registries.ENCHANTMENT, LOOK_WHAT_ID);

    private TakeshowEnchantments() {
    }

    public static void register() {
    }

    public static Holder<Enchantment> lookup(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(LOOK_WHAT_KEY);
    }
}
