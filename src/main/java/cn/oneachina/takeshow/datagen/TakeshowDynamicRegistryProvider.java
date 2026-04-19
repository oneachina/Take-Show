package cn.oneachina.takeshow.datagen;

import cn.oneachina.takeshow.registry.TakeshowEnchantments;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

public class TakeshowDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    public TakeshowDynamicRegistryProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(TakeshowEnchantments.LOOK_WHAT_KEY, createLookWhat());
    }

    @Override
    public String getName() {
        return "TakeShowDynamicRegistryProvider";
    }

    private static Enchantment createLookWhat() {
        HolderSet.Direct<Item> supported = HolderSet.direct(
                Items.LEATHER_CHESTPLATE.builtInRegistryHolder(),
                Items.CHAINMAIL_CHESTPLATE.builtInRegistryHolder(),
                Items.IRON_CHESTPLATE.builtInRegistryHolder(),
                Items.GOLDEN_CHESTPLATE.builtInRegistryHolder(),
                Items.DIAMOND_CHESTPLATE.builtInRegistryHolder(),
                Items.NETHERITE_CHESTPLATE.builtInRegistryHolder(),
                Items.SHIELD.builtInRegistryHolder()
        );

        Enchantment.EnchantmentDefinition definition = Enchantment.definition(
                supported,
                2,
                3,
                Enchantment.dynamicCost(1, 8),
                Enchantment.dynamicCost(16, 8),
                4,
                EquipmentSlotGroup.CHEST,
                EquipmentSlotGroup.MAINHAND,
                EquipmentSlotGroup.OFFHAND
        );

        return Enchantment.enchantment(definition).build(TakeshowEnchantments.LOOK_WHAT_ID);
    }
}

