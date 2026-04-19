package cn.oneachina.takeshow.registry;

import cn.oneachina.takeshow.Takeshow;
import cn.oneachina.takeshow.item.EchoCharmItem;
import cn.oneachina.takeshow.item.ResonantPickaxeItem;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public final class TakeshowItems {

    public static final Item ECHO_CHARM = registerItem("echo_charm", EchoCharmItem::new, new Item.Properties().stacksTo(1));
    public static final Item RESONANT_PICKAXE = registerItem(
            "resonant_pickaxe",
            ResonantPickaxeItem::new,
            new Item.Properties().pickaxe(ToolMaterial.IRON, 1.0f, -2.8f)
    );

    private TakeshowItems() {
    }

    public static void register() {
    }

    private static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemFactory, Item.Properties properties) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Takeshow.MOD_ID, name));
        T item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }
}
