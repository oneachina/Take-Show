package cn.oneachina.takeshow.registry;

import cn.oneachina.takeshow.Takeshow;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class TakeshowCreativeTabs {

    public static final ResourceKey<CreativeModeTab> TAKESHOW_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(Takeshow.MOD_ID, "takeshow")
    );

    public static final CreativeModeTab TAKESHOW_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(TakeshowItems.ECHO_CHARM))
            .title(Component.translatable("creativeTab.takeshow"))
            .displayItems((params, output) -> {
                output.accept(TakeshowItems.ECHO_CHARM);
                output.accept(TakeshowItems.RESONANT_PICKAXE);
            })
            .build();

    private TakeshowCreativeTabs() {
    }

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAKESHOW_TAB_KEY, TAKESHOW_TAB);
    }
}
